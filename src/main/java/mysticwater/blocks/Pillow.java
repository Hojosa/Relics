package mysticwater.blocks;

import mysticwater.base.BaseBlock;
import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.lib.Strings;
import net.minecraft.block.material.Material;

public class Pillow extends BaseBlock
{
	public Pillow(Material material, Category typ)
	{
		super(material);
		this.setHardness(0.8f);
		this.setResistance(4f);
		this.setUnlocalizedName(Strings.PillowName);
	}
	
	

}
