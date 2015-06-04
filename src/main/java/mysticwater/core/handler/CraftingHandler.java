package mysticwater.core.handler;

import mysticwater.init.ModBlocks;
import mysticwater.init.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class CraftingHandler
{
	public static void init()
	{
		registerRecipes();
	}
	
	public static void registerRecipes()
	{
		//ShapedRecipes
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.lapisBrick, 4),"## ","## ", '#', Blocks.lapis_block);
		CraftingManager.getInstance().addRecipe(new ItemStack(ModItems.iceCrystal, 2), " # ","###"," # ", '#', Blocks.ice);
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.swordPedestal, 1), "###", '#', Blocks.stone_slab);
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.glassSlab, 6), "###", '#', Blocks.glass);
		
		for(int i = 0; i < 16; i++)
		{
			CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.stainedGlassSlab, 6, i), "###", '#', new ItemStack(Blocks.stained_glass, 1, i));
		}
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
