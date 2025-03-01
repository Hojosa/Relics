package hojosa.relics.lib;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.StandaloneGeometryBakingContext;
import net.minecraftforge.client.model.obj.ObjLoader;
import net.minecraftforge.client.model.obj.ObjModel;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

//thanks to gigaherz for this class, otherwise i would have went crazy in self loading .obj models
public class ModelHandle {
	private final CompositeRenderable renderable;

	public static ModelHandle of(String modelLocation) {
		return new ModelHandle(new ResourceLocation(modelLocation));
	}

	public ModelHandle(ResourceLocation modelLocation) {
		ObjModel model = ObjLoader.INSTANCE.loadModel(new ObjModel.ModelSettings(modelLocation, false, true, true, true, null));
		this.renderable = model.bakeRenderable(StandaloneGeometryBakingContext.create(modelLocation));
	}

	public void render(PoseStack poseStack, MultiBufferSource bufferIn, RenderType rt, int packedLightIn, int color) {
		if (color != 0xFFFFFFFF) {
			var r = ((color >> 16) & 0xFF) / 255.0f;
			var g = ((color >> 8) & 0xFF) / 255.0f;
			var b = ((color >> 0) & 0xFF) / 255.0f;
			var a = ((color >> 24) & 0xFF) / 255.0f;
//			System.out.println(r);
			bufferIn = new ColoringBufferSource(r, g, b, a, bufferIn);
		}
		renderable.render(poseStack, bufferIn, t -> rt, packedLightIn, OverlayTexture.NO_OVERLAY, 0, CompositeRenderable.Transforms.EMPTY);
	}
	
	public void renderRainbow(PoseStack poseStack, MultiBufferSource bufferIn, RenderType rt, int packedLightIn) {
        float phase = (float)(System.currentTimeMillis() % 1000L) / 1000.0f;
        float r = RelicsUtil.r(phase);
        float g = RelicsUtil.g(phase);
        float b = RelicsUtil.b(phase);
        bufferIn = new ColoringBufferSource(r, g, b, 255, bufferIn);
        renderable.render(poseStack, bufferIn, t -> rt, packedLightIn, OverlayTexture.NO_OVERLAY, 0, CompositeRenderable.Transforms.EMPTY);
	}
}
