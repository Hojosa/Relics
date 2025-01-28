package hojosa.relics.common.init;

import hojosa.relics.lib.References;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;

public class RelicsCreativeModeTabs {
	public static final SynchronizedDeferredRegister<CreativeModeTab> CREATIVE_TABS = SynchronizedDeferredRegister.create(Registries.CREATIVE_MODE_TAB, References.MOD_ID);

	public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register(References.CREATIVE_TAB,
			() -> CreativeModeTab.builder().title(Component.translatable("item_group." + References.MOD_ID + ".tab")).withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
					.icon(() -> new ItemStack(RelicsItems.FIRE_TABLET.get()))
					.displayItems((parameters, output) -> {
						RelicsBlocks.addTabItems(parameters, output);
						RelicsItems.addTabItems(parameters, output);
					}).build());
}
