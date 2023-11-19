package mysticwater.client.render;

import hojosa.relics.lib.References;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ModelPedestal extends ModelBase
{
	private static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/pedestal/pedestaltex.png");
	public ModelRenderer box1 = new ModelRenderer(this, 0, 0).setTextureSize(48, 24);
	public ModelRenderer box2 = new ModelRenderer(this, 0, 10).setTextureSize(48, 24);
   
	public ModelPedestal()
	{
		this.box1.addBox(2.0F, -10.0F, -11.0F, 12, 4, 6, 0.0F);
		this.box2.addBox(2.5F, -12.0F, -10.5F, 11, 2, 5, 0.0F);
	}
   
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);
		this.box1.render(0.0625F);
		this.box2.render(0.0625F);
	}
}
