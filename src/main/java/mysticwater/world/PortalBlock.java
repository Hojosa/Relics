package mysticwater.world;

import mysticwater.lib.References;
import mysticwater.lib.Strings;
import net.minecraft.block.BlockPortal;
import net.minecraft.entity.player.EntityPlayerMP;
import relics.Relics;

public class PortalBlock extends BlockPortal
{
	public PortalBlock()
	{
		this.setUnlocalizedName(Strings.PortalBlock);
		this.setCreativeTab(Relics.getCreativTab());
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
