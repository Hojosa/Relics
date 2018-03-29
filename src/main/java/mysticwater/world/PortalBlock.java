package mysticwater.world;

import mysticwater.MysticWater;
import mysticwater.lib.References;
import mysticwater.lib.Strings;
import net.minecraft.block.BlockPortal;
import net.minecraft.entity.player.EntityPlayerMP;

public class PortalBlock extends BlockPortal
{
	public PortalBlock()
	{
		this.setUnlocalizedName(Strings.PortalBlock);
		this.setCreativeTab(MysticWater.getCreativTab());
	}
	
	public String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);

	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("%s%s", References.RESOURCESPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
}
