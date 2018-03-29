package mysticwater.core.proxy;

import mysticwater.client.render.TESwordPedestalSpecialRenderer;
import mysticwater.init.ModBlocks;
import mysticwater.tileentity.TileEntitySwordPedestal;
import net.minecraft.item.Item;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRendering()
	{
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySwordPedestal.class, new TESwordPedestalSpecialRenderer());
	
		//RenderingRegistry.registerBlockHandler(SwordPedestalUtil.PEDESTAL_RENDERER_ID, new RenderBlockPedestal());
		//SwordPedestalUtil var10000 = SwordPedestalUtil.instance;
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.swordPedestal), new RenderItemPedestal(new TileEntitySwordPedestalRenderer(), new SwordPedestalTE()));
       // RenderingRegistry.registerBlockHandler(SwordPedestalUtil.PEDESTAL_RENDERER_ID, new RenderBlockPedestal());
		//ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.swordPedestal), 0, TileEntitySwordPedestal.class);
	}

}
