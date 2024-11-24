package hojosa.relics.client.render;

import hojosa.relics.common.init.RelicsBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

@Deprecated
public class RelicsBlockRenders {
    public static void setRenderLayers() {
        setBlockRenderLayers();
    }
    
    @Deprecated
    private static void setBlockRenderLayers() {
    	//specify rendertyp in model json
    	ItemBlockRenderTypes.setRenderLayer(RelicsBlocks.SWORD_PEDESTAL.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get(), RenderType.cutout());
    }
}
