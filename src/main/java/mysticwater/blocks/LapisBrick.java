package mysticwater.blocks;

import mysticwater.base.BaseBlock;
import mysticwater.lib.Strings;
import net.minecraft.block.Block;

public class LapisBrick extends BaseBlock
{

	public LapisBrick()
	{
		this.setUnlocalizedName(Strings.LapisBrickName);
		this.setHardness(1.5f);
		this.setResistance(10.0f);
		this.setStepSound(Block.soundTypeStone);
		
		
	}
}
