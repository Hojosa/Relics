package hojosa.relics_of_old.client.gui;

import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;

import hojosa.relics_of_old.common.mana.IMana;
import hojosa.relics_of_old.common.player.PlayerManaProvider;
import hojosa.relics_of_old.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//Replaces the vanilla armor bar with a combined mana + armor diamond display.
// Based on LG2's GuiManaBar.onRenderArmorBar.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(modid = References.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManaBarOverlay {
	private static final ResourceLocation MANA_ICONS = ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "textures/gui/mana_icons.png");
	private static final Random rand = new Random();

	@SubscribeEvent
	public static void onRenderArmorOverlay(RenderGuiOverlayEvent.Pre event) {
		if (!event.getOverlay().id().equals(VanillaGuiOverlay.ARMOR_LEVEL.id()))
			return;

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null)
			return;

		player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
			int level = mana.getManaLevel();

			// Only show when mana is not full, or player is holding a spell item
			boolean holdingSpell = player.getMainHandItem().getItem() instanceof IMana;
			if (level == 20 && !holdingSpell)
				return;

			// Cancel vanilla armor bar — we draw both mana and armor ourselves
			event.setCanceled(true);

			int screenWidth = event.getWindow().getGuiScaledWidth();
			int screenHeight = event.getWindow().getGuiScaledHeight();
			int armor = player.getArmorValue();

			// Same position as vanilla armor bar
			int left = screenWidth / 2 - 91;
			int top = screenHeight - 49;
			if (player.getAbsorptionAmount() > 0.0f)
				top -= 10;

			// Seed random for consistent shake pattern per frame
			long updateCounter = Util.getMillis() / 50 % 1000;
			rand.setSeed(updateCounter);

			RenderSystem.enableBlend();

			// 10 diamond icons, each covering 2 mana points
			for (int i = 1; i < 20; i += 2) {
				// Texture row based on armor state at this position
				int vOffset = 0;
				if (i < armor)
					vOffset = 9; // full armor
				if (i == armor)
					vOffset = 18; // half armor

				// Shake when mana is low (at or below armor threshold)
				int y = top;
				if (level <= armor && updateCounter % 20 == 0) {
					y = top + (rand.nextInt(3) - 1);
				}

				// Texture column based on mana state at this position
				int uOffset;
				if (i < level)
					uOffset = 0; // full mana
				else if (i == level)
					uOffset = 9; // half mana
				else
					uOffset = 18; // empty mana

				guiGraphics(event).blit(MANA_ICONS, left, y, uOffset, vOffset, 9, 9, 256, 256);
				left += 8;
			}

			RenderSystem.disableBlend();
		});
	}

	private static GuiGraphics guiGraphics(RenderGuiOverlayEvent.Pre event) {
		return event.getGuiGraphics();
	}

}