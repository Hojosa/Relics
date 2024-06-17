package hojosa.relics.client.render;

import hojosa.relics.common.init.RelicsBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class RelicsBlockRenders {
    public static void setRenderLayers() {
        setBlockRenderLayers();
    }

    private static void setBlockRenderLayers() {
    	ItemBlockRenderTypes.setRenderLayer(RelicsBlocks.SWORD_PEDESTAL.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(RelicsBlocks.SWORD_PEDESTAL_OOT.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get(), RenderType.cutout());
    }
}
