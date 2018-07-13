package mysticwater.core.handler;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import relics.common.init.ModBlocks;
import relics.common.init.ModItems;

public class CraftingHandler
{
	public static void init()
	{
		registerRecipes();
	}
	
	public static void registerRecipes()
	{
//		//ShapedRecipes
//		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.lapisBrick, 4),"## ","## ", '#', Blocks.LAPIS_BLOCK);
//		CraftingManager.getInstance().addRecipe(new ItemStack(ModItems.iceCrystal, 2), " # ","###"," # ", '#', Blocks.ICE);
//		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.swordPedestal, 1), "###", '#', Blocks.STONE_SLAB);
//		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.glassSlab, 6), "###", '#', Blocks.GLASS);
//		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.otherSlabs, 6), "###", '#', ModBlocks.lapisBrick);
//		
//		for(int i = 0; i < 8; i++)
//		{
//			CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.stainedGlassSlab, 6, i), "###", '#', new ItemStack(Blocks.STAINED_GLASS, 1, i));
//			CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.stainedGlassSlab2, 6, i), "###", '#', new ItemStack(Blocks.STAINED_GLASS, 1, 8 + i));
//		}
		/*Shapedless Recipe*/
		
		/*CraftingManger.getInstance().addShapelessRecipe(new ItemStack(ModItems.testBlcok, 1), Blocks.dirt, Blocks.stone,);
		Blöcke die zum Craften verwendet werden sollen, hinten einfach anreihen*/
		
		/*FurnaceRecipes.smelting().func_151396_a(ModItems.testItem, new ItemStack(ModBlocks.testBlock), 1f);*/
		
		/*   ItemStack manipulation = new ItemStack(Items.diamond_sword);
        manipulation.addEnchantment(Enchantment.efficiency, 2);
        CraftingManager.getInstance().addShapelessRecipe(manipulation, Blocks.dirt);
        
        *manipulation die enchantment hinzufügen kann*/
	}

}
