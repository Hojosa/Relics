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
    public static float r(float phase) {
        phase = (float) (phase * (Math.PI * 2));
        double r = (Math.sin((phase + 0.0d)) + 1.0d) * 0.5d;
        double g = (Math.sin((phase + 2.0943952f)) + 1.0f) * 0.5f;
        double b = (Math.sin((phase + 4.1887903f)) + 1.0f) * 0.5f;
        double resat = Math.min(r, Math.min(g, b));
        double scaler = 1.0f / Math.max(r -= resat, Math.max(g -= resat, b -= resat));
        r = Math.min(scaler * r, 1.0f);
        return (float) r;
    }

    public static float g(float phase) {
        phase = (float) (phase * (Math.PI * 2));
        double r = (Math.sin((float)(phase + 0.0f)) + 1.0f) * 0.5f;
        double g = (Math.sin((float)(phase + 2.0943952f)) + 1.0f) * 0.5f;
        double b = (Math.sin((float)(phase + 4.1887903f)) + 1.0f) * 0.5f;
        double resat = Math.min(r, Math.min(g, b));
        double scaler = 1.0f / Math.max(r -= resat, Math.max(g -= resat, b -= resat));
        g = Math.min(scaler * g * 0.5f + 0.5f, 1.0f);
        return (float) g;
    }

    public static float b(float phase) {
        phase = (float) (phase * (Math.PI * 2));
        double r = (Math.sin((float)(phase + 0.0f)) + 1.0f) * 0.5f;
        double g = (Math.sin((float)(phase + 2.0943952f)) + 1.0f) * 0.5f;
        double b = (Math.sin((float)(phase + 4.1887903f)) + 1.0f) * 0.5f;
        double resat = Math.min(r, Math.min(g, b));
        double scaler = 1.0f / Math.max(r -= resat, Math.max(g -= resat, b -= resat));
        b = Math.min(scaler * b * 0.5f + 0.5f, 1.0f);
        return (float) b;
    }
}
