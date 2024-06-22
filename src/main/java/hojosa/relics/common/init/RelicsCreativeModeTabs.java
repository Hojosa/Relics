package hojosa.relics.common.init;

import hojosa.relics.lib.References;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RelicsCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, References.MODID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register(References.CREATIVE_TAB, () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group." + References.MODID + ".tab"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(RelicsItems.FIRE_TABLET.get()))
            .displayItems((parameters, output) -> {
                //Add blocks
                RelicsBlocks.BLOCKS.getEntries().forEach(
                        blockRegistryObject -> {
                               try { 
                            	   output.accept(new ItemStack(blockRegistryObject.get())); } 
                               catch (Exception e) {}
                        }
                );
                // Add items
                RelicsItems.ITEMS.getEntries().forEach(itemRegistryObject -> {
                        output.accept(new ItemStack(itemRegistryObject.get()));
            });
            }).build());
}
