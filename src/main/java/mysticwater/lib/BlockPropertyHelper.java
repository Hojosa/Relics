package mysticwater.lib;

import java.util.HashMap;
import java.util.Map;

import mysticwater.core.handler.EnumHandler.Category;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;

public class BlockPropertyHelper
{
	static Map<String, Object[]> propertyMap = new HashMap<String, Object[]>();
	
	private static void createBlockPropertys()
	{
		//Values: hardness, resistance, soundTyp
		//Object[] lapisPropertys = new Object[]{Material.rock, 1.5F, 10.0F, Block.soundTypeStone};
		propertyMap.put("glass", new Object[]{0.3F, 1.5F, SoundType.GLASS});
		propertyMap.put("lapis", new Object[]{1.5F, 10.0F, SoundType.STONE});
	}
	
	public static Object[] setBlockPropertyPerState(Block block, String state, Category cat)
	{
		Object[] blockPropertys;
		if(cat == Category.GLASSCOLOR_0 || cat == Category.GLASSCOLOR_1 || state.toString().equals("glass"))
		{
//			System.out.println(propertyMap.get(state.toString()) + " need text");
//			System.out.println(propertyMap.get("lapis") + " test");
//			Object[] toTest = propertyMap.get("lapis");
//			System.out.println((toTest[0] + " testArray 0");
//			System.out.println(toTest[1] + " testArray 1");
			blockPropertys = propertyMap.get("glass");
		}
		else
		{
			blockPropertys = propertyMap.get(state);
		}
		//System.out.
		System.out.println(state + " stateInf");
		System.out.println(blockPropertys[1] + " NULL?");
		block.setHardness((Float) blockPropertys[0]);
		block.setResistance((Float) blockPropertys[1]);
		//block.setSoundType((SoundType) blockPropertys[2]);
		System.out.println((blockPropertys[0] + " testArray 0"));
		System.out.println(blockPropertys[1] + " testArray 1");
		//System.out.println(state + " stateInf");
		System.out.println(cat + "catInf");
		
		return blockPropertys;//ropertyMap.get(state.toString());
	}
	
	
	public static void init()
	{
		
		createBlockPropertys();
	}
}

