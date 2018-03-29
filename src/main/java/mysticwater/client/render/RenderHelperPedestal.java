package mysticwater.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelperPedestal
{
	
	@SideOnly(Side.CLIENT)
	public static void renderItemInPedestal(ItemStack itemStack)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.65F, 0.326F, 0.00125F);
		GlStateManager.rotate(-270.0F, 0.0F, 0.0F, 8.0F);
		//GL11.glRotatef(0.0F, 0.0F, 20.0F, 0.0F);
		GlStateManager.scale(1.0D, 1.0D, 1.0D);
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.renderItem(itemStack, ItemCameraTransforms.TransformType.NONE);
		
		//renderItem.shouldRenderItemIn3D(itemStack);
		
		GlStateManager.popMatrix();
	}
}
