package mysticwater.blocks;

import mysticwater.base.BaseSlab;
import mysticwater.core.handler.EnumHandler.Category;
import net.minecraft.block.material.Material;

public class DoubleSlab extends BaseSlab
{

	public DoubleSlab(Material material, Category typ)
	{
		super(material, typ);
	}

	@Override
	public boolean isDouble()
	{
		return true;
	}
	

}
