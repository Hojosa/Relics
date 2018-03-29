package mysticwater.core.proxy;

import mysticwater.tileentity.TileEntitySwordPedestal;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntitySwordPedestal.class, "SwordPedestalTE");
	}
	public void registerRendering(){}

}
