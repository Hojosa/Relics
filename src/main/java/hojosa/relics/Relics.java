package hojosa.relics;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hojosa.relics.client.particle.RelicsParticles;
import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.common.init.RelicsCreativeModeTabs;
import hojosa.relics.common.init.RelicsEntities;
import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.init.RelicsSounds;
import hojosa.relics.common.loot.RelicsGlobalLootModifier;
import hojosa.relics.common.recipes.RelicsRecipes;
import hojosa.relics.integration.RelicsIntegration;
import hojosa.relics.lib.References;
import hojosa.relics.lib.RelicsUtil;
import hojosa.relics.lib.recipe.StonecutterRetexturedRecipe;
import hojosa.relics.network.RelicsNetwork;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import slimeknights.mantle.registration.adapter.RegistryAdapter;

@Mod(References.MOD_ID)
@Mod.EventBusSubscriber(modid = References.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Relics {
	public static final Logger LOGGER = LogManager.getLogger(References.MOD_ID);

	public Relics() {
		@SuppressWarnings("removal")
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientSetupEvent);
		modEventBus.addListener(this::enqueueIMC);
		modEventBus.addListener(this::register);

		RelicsBlocks.BLOCKS.register(modEventBus);
		RelicsItems.ITEMS.register(modEventBus);
		RelicsBlockEntities.BLOCK_ENTITIES.register(modEventBus);
		RelicsCreativeModeTabs.CREATIVE_TABS.register(modEventBus);
		RelicsEntities.ENTITY_TYPES.register(modEventBus);
		RelicsSounds.SOUNDS.register(modEventBus);
		RelicsGlobalLootModifier.register(modEventBus);
		RelicsParticles.PARTICLE_TYPES.register(modEventBus);
		RelicsRecipes.SERIALIZERS.register(modEventBus);

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void clientSetupEvent(final FMLClientSetupEvent event) {
		// clent setup event
	}

	private void setup(final FMLCommonSetupEvent event) {
		RelicsNetwork.register();
		event.enqueueWork(RelicsUtil::setupBlockCycleMap);
	}

	private void register(RegisterEvent event) {
		ResourceKey<?> key = event.getRegistryKey();
		if (key == Registries.RECIPE_SERIALIZER) {
			RegistryAdapter<RecipeSerializer<?>> adapter = new RegistryAdapter<>(Objects.requireNonNull(event.getForgeRegistry()));
			adapter.register(new StonecutterRetexturedRecipe.Serializer(), "crafting_stonecutter_retextured");
		}
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// Do nothing
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		RelicsIntegration.load();
	}
}
