package hojosa.relics_of_old.event;

import hojosa.relics_of_old.client.particle.BoostRippleParticle;
import hojosa.relics_of_old.client.particle.FlameParticle;
import hojosa.relics_of_old.client.particle.FlareParticle;
import hojosa.relics_of_old.client.particle.MagicScrambleParticle;
import hojosa.relics_of_old.client.particle.RuneParticle;
import hojosa.relics_of_old.client.particle.SparkleParticle;
import hojosa.relics_of_old.client.particle.SugarParticle;
import hojosa.relics_of_old.client.render.BombArrowEntityRenderer;
import hojosa.relics_of_old.client.render.BombEntityRenderer;
import hojosa.relics_of_old.client.render.EmptyEntityRenderer;
import hojosa.relics_of_old.client.render.EnderBombEntityRenderer;
import hojosa.relics_of_old.client.render.FallingStarRenderer;
import hojosa.relics_of_old.client.render.GlintBlockRenderer;
import hojosa.relics_of_old.client.render.InfusedStarstoneBlockRenderer;
import hojosa.relics_of_old.client.render.MagicBoomerangRenderer;
import hojosa.relics_of_old.client.render.MedallionEntityRenderer;
import hojosa.relics_of_old.client.render.PingEntityRenderer;
import hojosa.relics_of_old.client.render.RitualLocusBlockRenderer;
import hojosa.relics_of_old.client.render.SkybeamBlockRenderer;
import hojosa.relics_of_old.client.render.StarBeamRenderer;
import hojosa.relics_of_old.client.render.StarwellBlockRenderer;
import hojosa.relics_of_old.client.render.SwordPedestalBlockRenderer;
import hojosa.relics_of_old.client.render.SwordPedestalStoneBlockRenderer;
import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.common.item.BombBagItem;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsBlockColor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(modid = References.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RelicsClientEvents {

	private static final ResourceLocation DIGITS_ATLAS = ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "textures/gui/digits.png");

	public static final HumanoidModel.ArmPose TITAN_CARRY = HumanoidModel.ArmPose.create("TITAN_CARRY", true, (model, entity, arm) -> {
		// arms straight up: xRot = -PI is fully overhead
		if (arm == HumanoidArm.RIGHT) {
			model.rightArm.xRot = -(float) Math.PI;
			model.rightArm.yRot = 0.0F;
		} else {
			model.leftArm.xRot = -(float) Math.PI;
			model.leftArm.yRot = 0.0F;
		}
	});

	@SubscribeEvent
	public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
		event.register(new RelicsBlockColor(), RelicsBlocks.SWORD_PEDESTAL_NORMAL.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get(), RelicsBlocks.SWORD_PEDESTAL_TIME.get(),
				RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get());
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		// Nucleus colors: layer0 = color1, layer1 = color2
		registerTintedItem(event, RelicsItems.NUCLEUS_FIRE, 0xFF6600, 0xFFDD00);
		registerTintedItem(event, RelicsItems.NUCLEUS_ICE, 0x88BBFF, 0xCCEEFF);
		registerTintedItem(event, RelicsItems.NUCLEUS_LIGHTNING, 0x22FFEE, 0xFFFFB4);
		registerTintedItem(event, RelicsItems.NUCLEUS_CUT, 0xEEEEEE, 0xBBBBCC);
		registerTintedItem(event, RelicsItems.NUCLEUS_SKY, 0x0066FF, 0x22DDFF);
		registerTintedItem(event, RelicsItems.NUCLEUS_SUN, 0xFFBB00, 0xFFFFAA);
		registerTintedItem(event, RelicsItems.NUCLEUS_NAVIGATE, 0x888888, 0xFF5555);
		registerTintedItem(event, RelicsItems.NUCLEUS_DARK, 0x000011, 0x330066);
		registerTintedItem(event, RelicsItems.NUCLEUS_STAR, 0xFF77FF, 0xFFFFDD);
		registerTintedItem(event, RelicsItems.NUCLEUS_HEALTH, 0xAA0000, 0xFF4444);
		registerTintedItem(event, RelicsItems.NUCLEUS_WEAPON, 0xD8D8D8, 0x896B27);
		registerTintedItem(event, RelicsItems.NUCLEUS_WEALTH, 0x63F9AA, 0x00B038);

		// Gem colors: same pairs, layer2 untinted
		registerTintedItem(event, RelicsItems.GEM_FIRE, 0xFF6600, 0xFFDD00);
		registerTintedItem(event, RelicsItems.GEM_ICE, 0x88BBFF, 0xCCEEFF);
		registerTintedItem(event, RelicsItems.GEM_LIGHTNING, 0x22FFEE, 0xFFFFB4);
		registerTintedItem(event, RelicsItems.GEM_CUT, 0xEEEEEE, 0xBBBBCC);
		registerTintedItem(event, RelicsItems.GEM_SKY, 0x0066FF, 0x22DDFF);
		registerTintedItem(event, RelicsItems.GEM_SUN, 0xFFBB00, 0xFFFFAA);
		registerTintedItem(event, RelicsItems.GEM_NAVIGATE, 0x888888, 0xFF5555);
		registerTintedItem(event, RelicsItems.GEM_DARK, 0x000011, 0x330066);
		registerTintedItem(event, RelicsItems.GEM_STAR, 0xFF77FF, 0xFFFFDD);
		registerTintedItem(event, RelicsItems.GEM_HEALTH, 0xAA0000, 0xFF4444);
		registerTintedItem(event, RelicsItems.GEM_WEAPON, 0xD8D8D8, 0x896B27);
		registerTintedItem(event, RelicsItems.GEM_WEALTH, 0x63F9AA, 0x00B038);

		// Ring gem tinting — matching LG2 NucleusType colors
		registerTintedItem(event, RelicsItems.SPEED_RING, 0xFFFFFF, 0x22FFEE, 0xFFFFB4); // Gold + Lightning gem
		registerTintedItem(event, RelicsItems.CONVECTION_RING, 0xFFFFFF, 0xFF6600, 0xFFDD00); // Gold + Fire gem
		registerTintedItem(event, RelicsItems.SOFT_FALL_RING, 0xFFFFFF, 0x0066FF, 0x22DDFF); // Gold + Sky gem
		registerTintedItem(event, RelicsItems.COLD_FEET_RING, 0xFFFFFF, 0x88BBFF, 0xCCEEFF); // Gold + Ice gem
		registerTintedItem(event, RelicsItems.THIEF_RING, 0xFFFFFF, 0x000011, 0x330066); // Gold + Dark gem
		registerTintedItem(event, RelicsItems.MAGE_RING, 0xFFFFFF, 0xFF77FF, 0xFFFFDD); // Iron + Star gem
		registerTintedItem(event, RelicsItems.WARRIOR_RING, 0xFFFFFF, 0xD8D8D8, 0x896B27); // Iron + Weapon gem
		registerTintedItem(event, RelicsItems.FORTUNE_RING, 0xFFFFFF, 0x63F9AA, 0x00B038); // Wood + Wealth gem
		registerTintedItem(event, RelicsItems.ARROWFIND_RING, 0xFFFFFF, 0xD8D8D8, 0x896B27); // Wood + Weapon gem
		registerTintedItem(event, RelicsItems.AZUREFIND_RING, 0xFFFFFF, 0x0066FF, 0x22DDFF); // Wood + Sky gem
		registerTintedItem(event, RelicsItems.WISH_RING, 0x630AA9, 0xFF77FF, 0xFFFFDD); // Starglass(tinted) + Star gem
		registerTintedItem(event, RelicsItems.RESONANCE_RING, 0x630AA9, 0xFFBB00, 0xFFFFAA); // Starglass(tinted) + Sun gem
		registerTintedItem(event, RelicsItems.PHOENIX_RING, 0xFFDDDD, -1, -1); // Gold(tinted pink), no gem

		// Tome colors (cover + symbol + untinted pages)
		registerTintedItem(event, RelicsItems.TOME_SCYTHEWIND, 0x33AAFF, 0xEEEEEE);
		registerTintedItem(event, RelicsItems.TOME_RAYFIRE, 0xDD0055, 0xFFFFAA);
		registerTintedItem(event, RelicsItems.TOME_EXEUNT, 0x664411, 0x66AAFF);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), context -> new SwordPedestalBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY.get(), context -> new SwordPedestalBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.SWORD_PEDESTAL_STONE_BLOCK_ENTITY.get(), context -> new SwordPedestalStoneBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.GLINT_BLOCK_ENTITY.get(), GlintBlockRenderer::new);
		event.registerBlockEntityRenderer(RelicsBlockEntities.INFUSED_STARSTONE_BLOCK_ENTITY.get(), InfusedStarstoneBlockRenderer::new);
		event.registerBlockEntityRenderer(RelicsBlockEntities.SKYBEAM_BLOCK_ENTITY.get(), context -> new SkybeamBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.STARWELL_BLOCK_ENTITY.get(), context -> new StarwellBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.RITUAL_LOCUS_BLOCK_ENTITY.get(), context -> new RitualLocusBlockRenderer());

		event.registerEntityRenderer(RelicsEntities.FALLING_STAR.get(), FallingStarRenderer::new);
		event.registerEntityRenderer(RelicsEntities.STARBEAM.get(), StarBeamRenderer::new);
		event.registerEntityRenderer(RelicsEntities.MAGIC_BOOMERANG.get(), MagicBoomerangRenderer::new);
		event.registerEntityRenderer(RelicsEntities.MEDALLION.get(), MedallionEntityRenderer::new);
		event.registerEntityRenderer(RelicsEntities.FIREBLAST.get(), EmptyEntityRenderer::new);
		event.registerEntityRenderer(RelicsEntities.QUAKE.get(), EmptyEntityRenderer::new);
		event.registerEntityRenderer(RelicsEntities.ARROW_STORM.get(), EmptyEntityRenderer::new);
		event.registerEntityRenderer(RelicsEntities.ENDER_BOMB.get(), EnderBombEntityRenderer::new);
		event.registerEntityRenderer(RelicsEntities.CAPTURE_EGG.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(RelicsEntities.WHIRLWIND.get(), EmptyEntityRenderer::new);
		event.registerEntityRenderer(RelicsEntities.BOMB.get(), BombEntityRenderer::new);
		event.registerEntityRenderer(RelicsEntities.BOMB_ARROW.get(), BombArrowEntityRenderer::new);
		event.registerEntityRenderer(RelicsEntities.SPELL_EFFECT.get(), EmptyEntityRenderer::new);
		event.registerEntityRenderer(RelicsEntities.THROWN_ORB.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(RelicsEntities.PING.get(), PingEntityRenderer::new);
	}

	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(RelicsParticles.FLAME_PATTICLES.get(), FlameParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.SPARKLE_PARTICLES.get(), SparkleParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.RUNE_PARTICLE.get(), RuneParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.FLARE_PARTICLE.get(), FlareParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.MAGIC_SCRAMBLE_PARTICLE.get(), MagicScrambleParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.SUGAR_PARTICLE.get(), SugarParticle.Factory::new);
		event.registerSpriteSet(RelicsParticles.BOOST_RIPPLE_PARTICLE.get(), BoostRippleParticle.Provider::new);
	}

	@SubscribeEvent
	public static void onRegisterItemDecorations(RegisterItemDecorationsEvent event) {
		// bombBag counter
		event.register(RelicsItems.BOMB_BAG.get(), (guiGraphics, font, stack, xOffset, yOffset) -> {
			int count = BombBagItem.getBombCount(stack);
			if (count <= 0)
				return false;

			int color = count >= BombBagItem.MAX_BOMBS ? 0xFFFF55 : 0xFFFFFF;
			guiGraphics.setColor(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, (color & 0xFF) / 255f, 1f);

			guiGraphics.pose().pushPose();
			guiGraphics.pose().translate(0, 0, 200);

			// ones digit — row 0
			guiGraphics.blit(DIGITS_ATLAS, xOffset, yOffset, (count % 10) * 16, 0, 16, 16, 160, 48);

			// tens digit — row 1
			if (count >= 10) {
				guiGraphics.blit(DIGITS_ATLAS, xOffset, yOffset, (count / 10) * 16, 16, 16, 16, 160, 48);
			}
			guiGraphics.pose().popPose();
			guiGraphics.setColor(1f, 1f, 1f, 1f);
			return false;
		});
	}

	// this needs to fire on the forge event bus, so no subscribe event here
	public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
		LivingEntity entity = (LivingEntity) event.getEntity();

		// carried mob: offset upward + no sitting
		if (entity.getVehicle() instanceof Player player) {
			ItemStack bandStack = RelicsItems.TITAN_BAND.get().getEquippedStack(player);
			if (bandStack != null && bandStack.getOrCreateTag().getBoolean("TitanLift")) {
				double base = event.getRenderer().getModel() instanceof HumanoidModel ? 0.55 : 0.4;
				double offset = base - Math.min(entity.getMyRidingOffset(), 0.0);
				event.getPoseStack().translate(0.0, offset, 0.0);
			}
		}

		// carrying player: raise arms
		if (entity instanceof Player player) {
			ItemStack bandStack = RelicsItems.TITAN_BAND.get().getEquippedStack(player);
			if (bandStack != null && bandStack.getOrCreateTag().getBoolean("TitanLift")) {
				var model = event.getRenderer().getModel();
				if (model instanceof PlayerModel<?> playerModel) {
					playerModel.leftArmPose = TITAN_CARRY;
					playerModel.rightArmPose = TITAN_CARRY;
				}
			}
		}
	}

	// hide hands while a mob gets carried
	public static void onRenderHand(RenderHandEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player != null && player.getFirstPassenger() != null) {
			ItemStack bandStack = RelicsItems.TITAN_BAND.get().getEquippedStack(player);
			if (bandStack != null && bandStack.getOrCreateTag().getBoolean("TitanLift")) {
				event.setCanceled(true);
			}
		}
	}

	private static void registerTintedItem(RegisterColorHandlersEvent.Item event, RegistryObject<? extends Item> item, int... colors) {
		event.register((stack, tintIndex) -> tintIndex < colors.length ? colors[tintIndex] : 0xFFFFFF, item.get());
	}

//	private static void registerNucleusColor(RegisterColorHandlersEvent.Item event, RegistryObject<? extends Item> item, int color1, int color2) {
//		event.register((stack, tintIndex) -> tintIndex == 0 ? color1 : color2, item.get());
//	}
//
//	private static void registerGemColor(RegisterColorHandlersEvent.Item event, RegistryObject<? extends Item> item, int color1, int color2) {
//		event.register((stack, tintIndex) -> switch (tintIndex) {
//		case 0 -> color1;
//		case 1 -> color2;
//		default -> 0xFFFFFF; // layer2 (cover) untinted
//		}, item.get());
//	}
//
//	private static void registerRingColor(RegisterColorHandlersEvent.Item event, RegistryObject<? extends Item> item, int ringTint, int gemColor1, int gemColor2) {
//		event.register((stack, tintIndex) -> switch (tintIndex) {
//		case 0 -> ringTint; // ring base
//		case 1 -> gemColor1; // orb base
//		case 2 -> gemColor2; // orb fill
//		default -> 0xFFFFFF; // orb overlay, untinted
//		}, item.get());
//	}
//
//	private static void registerTomeColor(RegisterColorHandlersEvent.Item event, RegistryObject<? extends Item> item, int colorBase, int colorSymbol) {
//		event.register((stack, tintIndex) -> switch (tintIndex) {
//		case 0 -> colorBase; // book cover
//		case 1 -> colorSymbol; // symbol overlay
//		default -> 0xFFFFFF; // pages, untinted
//		}, item.get());
//	}

}