package mysticwater.blocks;

import mysticwater.base.BaseMetaSlab;
import mysticwater.base.BaseSlab;
import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.core.handler.EnumHandler.ColorSet;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;


public class DoubleSlab extends BaseSlab
{

	public DoubleSlab(Material material, Category typ, String name, boolean fullBlock)
	{
		super(material, setEnumTyp(typ), name, fullBlock);
	}

	@Override
	public String getUnlocalizedName(int meta)
	{
		// TODO Auto-generated method stub
		return this.getRegistryName().toString() + "." + getVariantProperty().getAllowedValues().toArray()[meta].toString().toLowerCase();
	}
	
//	@Override
//	public boolean isDouble()
//	{
//		return true;
//	}

//	@Override
//	public Comparable<?> getTypeForItem(ItemStack stack)
//	{
//		return (Comparable<?>) ColorSet.getStateList(blockTyp, false).get(stack.getMetadata() & 7);
//	}
//	
}
