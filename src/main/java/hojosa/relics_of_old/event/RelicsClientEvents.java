package hojosa.relics_of_old.event;

import hojosa.relics_of_old.client.particle.FlameParticle;
import hojosa.relics_of_old.client.particle.FlareParticle;
import hojosa.relics_of_old.client.particle.MagicScrambleParticle;
import hojosa.relics_of_old.client.particle.RuneParticle;
import hojosa.relics_of_old.client.particle.SparkleParticle;
import hojosa.relics_of_old.client.particle.SugarParticle;
import hojosa.relics_of_old.client.render.BombEntityRenderer;
import hojosa.relics_of_old.client.render.EmptyEntityRenderer;
import hojosa.relics_of_old.client.render.EnderBombEntityRenderer;
import hojosa.relics_of_old.client.render.FallingStarRenderer;
import hojosa.relics_of_old.client.render.GlintBlockRenderer;
import hojosa.relics_of_old.client.render.InfusedStarstoneBlockRenderer;
import hojosa.relics_of_old.client.render.MagicBoomerangRenderer;
import hojosa.relics_of_old.client.render.MedallionEntityRenderer;
import hojosa.relics_of_old.client.render.SkybeamBlockRenderer;
import hojosa.relics_of_old.client.render.StarBeamRenderer;
import hojosa.relics_of_old.client.render.SwordPedestalBlockRenderer;
import hojosa.relics_of_old.client.render.SwordPedestalStoneBlockRenderer;
import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsBlockColor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(modid = References.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RelicsClientEvents {

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
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), context -> new SwordPedestalBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY.get(), context -> new SwordPedestalBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.SWORD_PEDESTAL_STONE_BLOCK_ENTITY.get(), context -> new SwordPedestalStoneBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.GLINT_BLOCK_ENTITY.get(), GlintBlockRenderer::new);
		event.registerBlockEntityRenderer(RelicsBlockEntities.INFUSED_STARSTONE_BLOCK_ENTITY.get(), InfusedStarstoneBlockRenderer::new);
		event.registerBlockEntityRenderer(RelicsBlockEntities.SKYBEAM_BLOCK_ENTITY.get(), context -> new SkybeamBlockRenderer());
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
	}

	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(RelicsParticles.FLAME_PATTICLES.get(), FlameParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.SPARKLE_PARTICLES.get(), SparkleParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.RUNE_PARTICLE.get(), RuneParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.FLARE_PARTICLE.get(), FlareParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.MAGIC_SCRAMBLE_PARTICLE.get(), MagicScrambleParticle.Provider::new);
		event.registerSpriteSet(RelicsParticles.SUGAR_PARTICLE.get(), SugarParticle.Factory::new);	
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

	// hide hans while a mob gets carried
	public static void onRenderHand(RenderHandEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player != null && player.getFirstPassenger() != null) {
			ItemStack bandStack = RelicsItems.TITAN_BAND.get().getEquippedStack(player);
			if (bandStack != null && bandStack.getOrCreateTag().getBoolean("TitanLift")) {
				event.setCanceled(true);
			}
		}
	}
}