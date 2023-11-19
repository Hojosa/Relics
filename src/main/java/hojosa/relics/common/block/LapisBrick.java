package hojosa.relics.common.block;

import mysticwater.lib.Strings;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Deprecated
public class LapisBrick extends RelicsBlock
{

	public LapisBrick(Material material, String name)
	{
		super(material, name);
		this.setHardness(1.5f);
		this.setResistance(10.0f);
		this.setSoundType(SoundType.STONE);
	
		
	}
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(!worldIn.isRemote) 
		{
			System.out.println("Testing");
		//System.out.println(worldIn + " + " + pos + " + " + state + " + " + playerIn + " + " + hand + " + " + facing + hitX + hitY + hitZ);
		if(hand == EnumHand.MAIN_HAND)
		{
			//System.out.println("main hand");
		}
		System.out.println("------------------");
		System.out.println(player.getHeldItemMainhand());
		System.out.println(player.replaceItemInInventory(player.inventory.getFirstEmptyStack(), player.getHeldItemMainhand()));
		
		}
        return true;
    
    }
}
