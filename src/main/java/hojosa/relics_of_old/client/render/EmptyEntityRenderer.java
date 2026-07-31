package hojosa.relics_of_old.client.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

//an empty renderer for entity, that only spawn particles. mostly used for medallion attack effects
public class EmptyEntityRenderer<T extends Entity> extends EntityRenderer<T> {

	public EmptyEntityRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return null;
	}
}