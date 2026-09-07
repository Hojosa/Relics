package hojosa.relics_of_old.client.render;

import java.util.OptionalDouble;
import java.util.function.BiFunction;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class RelicsRenderTypes extends RenderType {

	public RelicsRenderTypes(String pName, VertexFormat pFormat, Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
		super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
	}
	
	private static final ResourceLocation GLINT_TEX = RelicsUtil.modLoc("textures/glint/glint_rainbow.png");
	// spell reticle line types — uses RENDERTYPE_LINES_SHADER for GPU quad expansion (no glLineWidth)
	public static final RenderType RETICLE_LINE_THIN = createReticleLineStrip("reticle_thin", 1.0);
	public static final RenderType RETICLE_LINE_MEDIUM = createReticleLineStrip("reticle_medium", 2.0);
	public static final RenderType RETICLE_LINE_THICK = createReticleLineStrip("reticle_thick", 3.0);
	public static final RenderType RETICLE_SEGMENT_THIN = createReticleLines("reticle_seg_thin", 1.0);
		
	public static final RenderType ENTITY_GLINT_RAINBOW = RenderType.create(References.MOD_ID + ":entity_glint_rainbow", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
		.setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_SHADER)
		.setTextureState(new TextureStateShard(GLINT_TEX, true, false))
		.setWriteMaskState(RenderStateShard.COLOR_WRITE)
		.setCullState(RenderStateShard.NO_CULL)
		.setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
		.setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
		.setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
		.setTexturingState(RenderStateShard.ENTITY_GLINT_TEXTURING)
		.createCompositeState(false));

	
	public static final RenderType ENTITY_GLINT_DIRECT_RAINBOW = RenderType.create(References.MOD_ID + ":entity_glint_direct_rainbow", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
		.setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER)
		.setTextureState(new TextureStateShard(GLINT_TEX, true, false))
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
		.setTextureState(new TextureStateShard(GLINT_TEX, true, false))
		.setWriteMaskState(RenderStateShard.COLOR_WRITE)
		.setCullState(RenderStateShard.NO_CULL)
		.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
		.setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
		.setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
		.setTexturingState(RenderStateShard.GLINT_TEXTURING)
		.setLayeringState(CUSTOM_POLYGON_OFFSET_LAYERING)
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
    
	// -- Custom RenderType for solid triangles with color, no texture --
	public static final RenderType SPELL_TRIANGLES = RenderType.create("relics_spell_triangles", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
			.setShaderState(RenderType.POSITION_COLOR_SHADER)
			.setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
			.setCullState(RenderType.NO_CULL)
			.setWriteMaskState(RenderType.COLOR_DEPTH_WRITE)
			.createCompositeState(false));
    
	private static final BiFunction<ResourceLocation, Boolean, RenderType> ADDITIVE_BEAM = Util.memoize((texture, translucent) -> {
	      CompositeState state = CompositeState.builder()
	              .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
	              .setTextureState(new TextureStateShard(texture, false, false))
	              .setTransparencyState(LIGHTNING_TRANSPARENCY)
	              .setWriteMaskState(translucent ? COLOR_WRITE : COLOR_DEPTH_WRITE)
	              .setCullState(NO_CULL)
	              .createCompositeState(false);
	      return create(References.MOD_ID + ":additive_beam", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, true, state);
	  });
	
	private static final BiFunction<ResourceLocation, Boolean, RenderType> ADDITIVE_BEAM_NO_DEPTH = Util.memoize((texture, unused) -> {
        CompositeState state = CompositeState.builder()
                        .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(LIGHTNING_TRANSPARENCY)
                        .setWriteMaskState(COLOR_WRITE)
                        .setCullState(NO_CULL)
                        .setDepthTestState(NO_DEPTH_TEST)
                        .createCompositeState(false);
        return create(References.MOD_ID + ":additive_beam_no_depth", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, true, state);
	});
	
	private static RenderType createReticleLineStrip(String name, double width) {
	      return create(References.MOD_ID + ":" + name,
	              DefaultVertexFormat.POSITION_COLOR_NORMAL,
	              VertexFormat.Mode.LINE_STRIP, 256, false, false,
	              CompositeState.builder()
	                      .setShaderState(RENDERTYPE_LINES_SHADER)
	                      .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(width)))
	                      .setLayeringState(VIEW_OFFSET_Z_LAYERING)
	                      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
	                      .setWriteMaskState(COLOR_DEPTH_WRITE)
	                      .setCullState(NO_CULL)
	                      .createCompositeState(false));
	  }

	  private static RenderType createReticleLines(String name, double width) {
	      return create(References.MOD_ID + ":" + name,
	              DefaultVertexFormat.POSITION_COLOR_NORMAL,
	              VertexFormat.Mode.LINES, 256, false, false,
	              CompositeState.builder()
	                      .setShaderState(RENDERTYPE_LINES_SHADER)
	                      .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(width)))
	                      .setLayeringState(VIEW_OFFSET_Z_LAYERING)
	                      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
	                      .setWriteMaskState(COLOR_DEPTH_WRITE)
	                      .setCullState(NO_CULL)
	                      .createCompositeState(false));
	  }

	public static RenderType additiveBeam(ResourceLocation texture, boolean translucent) {
	      return ADDITIVE_BEAM.apply(texture, translucent);
	}
	
	public static RenderType additiveBeamNoDepth(ResourceLocation texture) {
        return ADDITIVE_BEAM_NO_DEPTH.apply(texture, false);
	}
	
    public static RenderType getTextureRenderColored(ResourceLocation texture) {
        return getTextureRenderColored(texture, false);
    }
    public static RenderType getTextureRenderColored(ResourceLocation texture, boolean disableDepthTest) {
        return TEXTURE_RENDER_COLORED.apply(texture, disableDepthTest);
    }
}