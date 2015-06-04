package mysticwater.core.proxy;

import mysticwater.init.ModItems;
import mysticwater.lib.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRendering()
	{
		//ClientRegistry.bindTileEntitySpecialRenderer(SwordPedestalTE.class, new TileEntitySwordPedestalRenderer());
		//RenderingRegistry.registerBlockHandler(SwordPedestalUtil.PEDESTAL_RENDERER_ID, new RenderBlockPedestal());
		//SwordPedestalUtil var10000 = SwordPedestalUtil.instance;
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.swordPedestal), new RenderItemPedestal(new TileEntitySwordPedestalRenderer(), new SwordPedestalTE()));
       // RenderingRegistry.registerBlockHandler(SwordPedestalUtil.PEDESTAL_RENDERER_ID, new RenderBlockPedestal());
	}

}
