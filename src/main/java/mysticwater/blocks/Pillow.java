package mysticwater.blocks;

import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.lib.Strings;
import net.minecraft.block.material.Material;
import relics.common.block.BlockRelics;

public class Pillow extends BlockRelics
{
	public Pillow(Material material, Category typ)
	{
		super(material);
		this.setHardness(0.8f);
		this.setResistance(4f);
		this.setUnlocalizedName(Strings.PillowName);
	}
	
	

}
