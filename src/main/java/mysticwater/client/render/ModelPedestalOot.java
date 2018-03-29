package mysticwater.client.render;

import org.lwjgl.opengl.GL11;

import mysticwater.lib.References;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ModelPedestalOot extends ModelBase
{
	private static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/pedestal/ootpedestal.png");

	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);
		VertexBuffer vertexBuffer = Tessellator.getInstance().getBuffer();
     
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	     
		double y = 0.25D;
	    
	    GL11.glTranslated(0.0D, -0.75D, -0.375D);
	    
	    vertexBuffer.begin(GL11.GL_QUADS,DefaultVertexFormats.POSITION_TEX);
	    GL11.glDisable(GL11.GL_LIGHTING);

//	 
//		Backface (x, y, z) y = höhe unten, z schräge, x position
	    vertexBuffer.pos(0.0625D, 0.0D, 0.09375D).tex(0.0D, 0.5D).endVertex();;//l
	    vertexBuffer.pos(0.9375D, 0.0D, 0.09375D).tex(1.0D, 0.5D).endVertex();; //r
	    //Backface, y höhe oben, z schräge, y x  vertex position
	    vertexBuffer.pos(0.8125D, 0.25D, 0.0D).tex(0.859375D, 0.25D).endVertex();;//r
	    vertexBuffer.pos(0.1875D, 0.25D, 0.0D).tex(0.140625D, 0.25D).endVertex();;//l
	     
	    //front
	    vertexBuffer.pos(0.1875D, 0.25D, -0.25D).tex(0.140625D, 0.25D).endVertex();; //r
	    vertexBuffer.pos(0.8125D, 0.25D, -0.25D).tex(0.859375D, 0.25D).endVertex();; //l
	    vertexBuffer.pos(0.9375D, 0.0D, -0.34375D).tex(1.0D, 0.5D).endVertex();; //l
	    vertexBuffer.pos(0.0625D, 0.0D, -0.34375D).tex(0.0D, 0.5D).endVertex();;//r

	    //top
	    vertexBuffer.pos(0.1875D, 0.25D, -0.25D).tex(0.0D, 0.25D).endVertex();;
	    vertexBuffer.pos(0.1875D, 0.25D, 0.0D).tex(0.0D, 0.0D).endVertex();;
	    vertexBuffer.pos(0.8125D, 0.25D, 0.0D).tex(0.625D, 0.0D).endVertex();;
	    vertexBuffer.pos(0.8125D, 0.25D, -0.25D).tex(0.625D, 0.25D).endVertex();;
	    
	    //right
	    vertexBuffer.pos(0.1875D, 0.25D, 0.0D).tex(0.6875D, 0.0D).endVertex();;//r
	    vertexBuffer.pos(0.1875D, 0.25D, -0.25D).tex(0.9375D, 0.0D).endVertex();;//l
	    vertexBuffer.pos(0.0625D, 0.0D, -0.34375D).tex(1.0D, 0.25D).endVertex();;//l
	    vertexBuffer.pos(0.0625D, 0.0D, 0.09375D).tex(0.625D, 0.25D).endVertex();;//r
	   
	    //left
	    vertexBuffer.pos(0.8125D, 0.25D, -0.25D).tex(0.703125D, 0.0D).endVertex();;
	    vertexBuffer.pos(0.8125D, 0.25D, 0.0D).tex(0.921875D, 0.0D).endVertex();;
	    vertexBuffer.pos(0.9375D, 0.0D, 0.09375D).tex(1.0D, 0.25D).endVertex();;
	    vertexBuffer.pos(0.9375D, 0.0D, -0.34375D).tex(0.625D, 0.25D).endVertex();;

	    //down
	    vertexBuffer.pos(0.0625D, 0.0D, 0.09375D).tex(0.0D, 0.5D).endVertex();;
	    vertexBuffer.pos(0.0625D, 0.0D, -0.34375D).tex(0.0D, 0.75D).endVertex();;
	    vertexBuffer.pos(0.9375D, 0.0D, -0.34375D).tex(1.0D, 0.75D).endVertex();;
	    vertexBuffer.pos(0.9375D, 0.0D, 0.09375D).tex(1.0D, 0.5D).endVertex();;

	    Tessellator.getInstance().draw();
	    
	   GL11.glPopMatrix();
	  }

}
