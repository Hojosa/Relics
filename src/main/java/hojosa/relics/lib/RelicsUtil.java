package hojosa.relics.lib;

import net.minecraft.resources.ResourceLocation;

public class RelicsUtil {
	
    public static ResourceLocation mcLoc(String path) {
        return new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, path);
    }
    
    public static ResourceLocation forgeLoc(String path) {
        return new ResourceLocation("forge", path);
    }

    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(References.MOD_ID, path);
    }
}
