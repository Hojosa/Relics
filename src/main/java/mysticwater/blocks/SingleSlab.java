package mysticwater.blocks;

import mysticwater.base.BaseSlab;
import mysticwater.core.handler.EnumHandler.Category;
import net.minecraft.block.material.Material;

public class SingleSlab extends BaseSlab
{
	
	public SingleSlab(Material material, Category typ)
	{
		super(material, typ);
	}

	@Override
	public final boolean isDouble()
	{
		return false;
	}
	

}
