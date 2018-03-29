package mysticwater.client.render;

import org.lwjgl.opengl.GL11;

import mysticwater.tileentity.TileEntitySwordPedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TESwordPedestalSpecialRenderer extends TileEntitySpecialRenderer<TileEntitySwordPedestal>
{
	private ModelBase pedestal;
	private RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	

	
//	private void doRender(TileEntitySwordPedestal te, double x, double y, double z, float f)
//	{
//	
//		this.pedestal = new ModelPedestalOot();
//		
//		
//		//TileEntitySwordPedestal te = (TileEntitySwordPedestal) tileEntity;
//		   
//		GlStateManager.pushMatrix();
//		GlStateManager.translate(x + 0.5D, y + 0.75D, z + 0.5D);
//		
//		GlStateManager.rotate((float)(-te.baseRotation), 0.0F, 1.0F, 0.0F);
////		GlStateManager.pushMatrix();
////		GlStateManager.translate(-0.5D, 0.5D, 0.5D);
////		
////		//this.pedestal.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
////		GlStateManager.popMatrix();
//	     
//		   
//		   //Tessellator.getInstance().draw();
//		   
//		   if (te. != null)
//		   {
//			  // Object s = null;
//			   if (te.isFloating)
//			   {
//				   GlStateManager.translate(0.0D, 0.45D, 0.0D);
//				   GlStateManager.translate(0.0D, Math.sin(Math.toRadians((double)(te.sinShift * 2))) / 32.0D, 0.0D);
//				  
//				   if (te.clockwiseRotation) 
//				   {
//					   GlStateManager.rotate((float)(-te.rotation), 0.0F, 1.0F, 0.0F);
//				   }
//				   else 
//				   {
//					   GlStateManager.rotate((float)(te.rotation), 0.0F, 1.0F, 0.0F);
//				   }
//			   }
//			   GlStateManager.translate(0.6875D, -0.25D, 0.0D);
//			   GlStateManager.rotate(135.0F, 0.0F, 0.0F, 1.0F);
//	       
//			   byte var = 1;
//			   ItemStack tmp = te.sword.copy();
//			 
//			   if (te.enchantmentGlimmer)
//			   {
//				   tmp.addEnchantment(Enchantments.SHARPNESS, 1);
//				   var = 0;
//			   }
//			
//			  renderSwordOld(tmp);
//			   
//			   //renderItem.shouldRenderItemIn3D(tmp);
//	       
//			   char j = 61680;
//			   int k = j % 256;
//			   int l = j / 256;
//			   OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
//		   }
//		   if ((te.lightBeamEnabled) && (te.sword != null))
//		   {
//			   //System.out.println("YES");
//			   GL11.glPopMatrix();
//			 
//			   renderLightBeam(te, x, y, z, te.colorRGBA);
//			   
//	       
//			   GL11.glPushMatrix();
//			   
//		   }
//		  //Tessellator.getInstance().draw();
//		   GL11.glPopMatrix();
//	}
	@Override
	public void renderTileEntityAt(TileEntitySwordPedestal tileEntity, double x, double y, double z, float partialTick, int destryStage)
	{	
		//if(tileEntity.getStackInSlot(0) != null)
		if(tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) ? tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0) != null? true: false : false)
		{
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(x + 0.5D, y + 0.75D, z + 0.5D);
		GlStateManager.rotate((float)(-tileEntity.baseRotation), 0.0F, 1.0F, 0.0F);

		if(tileEntity.isFloating)
			swordAnimation(tileEntity);
			
		
		GlStateManager.translate(0.6875D, -0.25D, 0.0D);
		GlStateManager.rotate(135.0F, 0.0F, 0.0F, 1.0F);
		
		renderSword(tileEntity);
		
		if(tileEntity.lightBeamEnabled) 
			renderLightBeam(tileEntity, x, y, z, tileEntity.colorRGBA);

			GlStateManager.popMatrix();
		}
	}
	
	public void renderSword(TileEntitySwordPedestal tileEntity)
	{
		ItemStack tmp = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);
		if(tileEntity.enchantmentGlimmer)
		{
			tmp.addEnchantment(Enchantments.SHARPNESS, 1);
		}
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(0.65F, 0.326F, 0.00125F);
		GlStateManager.rotate(-270.0F, 0.0F, 0.0F, 8.0F);
		GL11.glRotatef(0.0F, 0.0F, 20.0F, 0.0F);
		GlStateManager.scale(1.0D, 1.0D, 1.0D);
		Minecraft.getMinecraft().getRenderItem().renderItem(tmp, ItemCameraTransforms.TransformType.NONE);
		
		GlStateManager.popMatrix();
		
	}
	
	public void renderSwordOld(ItemStack itemStack)
	{
		//System.out.println("HELLO");
		
			//System.out.println("HELLO2");
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.65F, 0.326F, 0.00125F);
		GlStateManager.rotate(-270.0F, 0.0F, 0.0F, 8.0F);
		GL11.glRotatef(0.0F, 0.0F, 20.0F, 0.0F);
		GlStateManager.scale(1.0D, 1.0D, 1.0D);
		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.NONE);
		//renderItem.shouldRenderItemIn3D(itemStack);
		GlStateManager.popMatrix();
		//GlStateManager.popMatrix();
		
	}
	
	private void swordAnimation(TileEntitySwordPedestal tileEntity)
	{
		GlStateManager.translate(0.0D, 0.45D, -1.0D);
		GlStateManager.translate(0.0D, Math.sin(Math.toRadians((double)(tileEntity.sinShift * 2))) / 32.0D, 1.0D);
		  
		if (tileEntity.clockwiseRotation) 
		   {
				GlStateManager.rotate((float)(-tileEntity.rotation), 0.0F, 1.0F, 0.0F);
		   }
		   else 
		   {
			   	GlStateManager.rotate((float)(tileEntity.rotation), 0.0F, 1.0F, 0.0F);
		   }
		
		if(tileEntity.isFloating )
		{
			if (tileEntity.isRotating)
			{
				tileEntity.rotation += tileEntity.rotationSpeed;
				if ((tileEntity.rotation >= 359) || (tileEntity.rotation < 0)) 
				{
					tileEntity.rotation = 0;
				}
			}
			tileEntity.sinShift += 2;
			if (tileEntity.sinShift > 359) 
			{
				tileEntity.sinShift = 0;
			}
		}
	}
	
	public static void renderLightBeam(TileEntity te, double x, double y, double z, int[] colors)
	{
		GL11.glPopMatrix();
		
		double factor = 0.35D;
		VertexBuffer vertexBuffer = Tessellator.getInstance().getBuffer();
		int height = 20;
		int depth = 0;
		
		GL11.glPushMatrix();
		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GlStateManager.translate(x, y, z);
		GL11.glDepthMask(false);

		for (int j = 2; j < 5; ++j) 
		{
			for (int i = 0; i < 4; ++i)
			{
				GL11.glPushMatrix();
				vertexBuffer.begin(GL11.GL_QUADS,DefaultVertexFormats.POSITION_COLOR);
				GlStateManager.translate(0.5D, 0.0D, 0.5D);
				GlStateManager.scale(0.55D, 1.0D, 0.55D);
				GlStateManager.rotate((float)90 * i, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(-0.5D, 0.0D, -0.5D);
    	  
				vertexBuffer.pos(factor * (double)j, (double)height, factor * (double)j).color(colors[0], colors[1], colors[2], colors[3] + 25).endVertex();;
				vertexBuffer.pos(1.0D - factor * (double)j, (double)height, factor * (double)j).color(colors[0], colors[1], colors[2], colors[3] + 25).endVertex();;
				vertexBuffer.pos(1.0D - factor * (double)j, (double)depth, factor * (double)j).color(colors[0], colors[1], colors[2], colors[3] + 25).endVertex();;
				vertexBuffer.pos(factor * (double)j, (double)depth, factor * (double)j).color(colors[0], colors[1], colors[2], colors[3] + 25).endVertex();;

				Tessellator.getInstance().draw();
				GL11.glPopMatrix();
			}
		}
		
    GL11.glDepthMask(true);
    GL11.glPopMatrix();
    GL11.glDisable(3042);
   	GL11.glEnable(2896);
   	GL11.glEnable(3553);
   	
   	GL11.glPushMatrix();
  }
}
