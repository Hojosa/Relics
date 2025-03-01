package hojosa.relics.client.render;

import java.util.function.BiFunction;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import hojosa.relics.lib.References;
import hojosa.relics.lib.RelicsUtil;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class RelicsRenderTypes extends RenderType {

	public RelicsRenderTypes(String pName, VertexFormat pFormat, Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
		super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
	}
	
	public static final ResourceLocation BEAM_TEX = new ResourceLocation(References.MOD_ID, "textures/entity/beam.png");
		
	public static final RenderType ENTITY_GLINT_RAINBOW = RenderType.create(References.MOD_ID + ":entity_glint_rainbow", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
		.setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_SHADER)
		.setTextureState(new TextureStateShard(RelicsUtil.modLoc("textures/glint/glint_rainbow.png"), true, false))
		.setWriteMaskState(RenderStateShard.COLOR_WRITE)
		.setCullState(RenderStateShard.NO_CULL)
		.setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
		.setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
		.setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
		.setTexturingState(RenderStateShard.ENTITY_GLINT_TEXTURING)
		.createCompositeState(false));

	
	public static final RenderType ENTITY_GLINT_DIRECT_RAINBOW = RenderType.create(References.MOD_ID + ":entity_glint_direct_rainbow", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
		.setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER)
		.setTextureState(new TextureStateShard(RelicsUtil.modLoc("textures/glint/glint_rainbow.png"), true, false))
		.setWriteMaskState(RenderStateShard.COLOR_WRITE)
		.setCullState(RenderStateShard.NO_CULL)
		.setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
		.setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
		.setTexturingState(RenderStateShard.ENTITY_GLINT_TEXTURING)
		.createCompositeState(false));
	
    
	private static final RenderStateShard.LayeringStateShard CUSTOM_POLYGON_OFFSET_LAYERING = new RenderStateShard.LayeringStateShard(
			"polygon_offset_layering", () -> {
		RenderSystem.polygonOffset(-0.25F, -10.0F);
		RenderSystem.enablePolygonOffset();
	}, () -> {
		RenderSystem.polygonOffset(0.0F, 0.0F);
		RenderSystem.disablePolygonOffset();
	}
	);
	
	public static final RenderType BLOCK_GLINT_RAINBOW = RenderType.create(References.MOD_ID + ":entity_glint_rainbow", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 1536, false, false, CompositeState.builder()
		.setShaderState(RenderStateShard.RENDERTYPE_GLINT_SHADER)
		.setTextureState(new TextureStateShard(RelicsUtil.modLoc("textures/glint/glint_rainbow.png"), true, false))
		.setWriteMaskState(RenderStateShard.COLOR_WRITE)
		.setCullState(RenderStateShard.NO_CULL)
		.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
		.setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
		.setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
		.setTexturingState(RenderStateShard.GLINT_TEXTURING)
		.setLayeringState(CUSTOM_POLYGON_OFFSET_LAYERING)
		.createCompositeState(false));
    
    public static final RenderType BEAM = RenderType.create(References.MOD_ID + ":beam", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
        .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
        .setTextureState(new TextureStateShard(BEAM_TEX, false, false))
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        .setCullState(CULL)
        .setLightmapState(LIGHTMAP)
        .setOverlayState(OVERLAY)
        .setWriteMaskState(COLOR_WRITE)
        .setDepthTestState(LEQUAL_DEPTH_TEST)
        .setLayeringState(VIEW_OFFSET_Z_LAYERING)
        .createCompositeState(false));

    private static final BiFunction<ResourceLocation, Boolean, RenderType> TEXTURE_RENDER_COLORED = Util.memoize((rl, disableDepthTest) -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(rl, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setDepthTestState(disableDepthTest ? NO_DEPTH_TEST : LEQUAL_DEPTH_TEST)
                .setWriteMaskState(disableDepthTest ? COLOR_WRITE : COLOR_DEPTH_WRITE)
                .createCompositeState(false);
        return create("texture_color", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS,
                256, false, false, state);
    });
    
    public static RenderType getTextureRenderColored(ResourceLocation texture) {
        return getTextureRenderColored(texture, false);
    }
    public static RenderType getTextureRenderColored(ResourceLocation texture, boolean disableDepthTest) {
        return TEXTURE_RENDER_COLORED.apply(texture, disableDepthTest);
    }
		
}
