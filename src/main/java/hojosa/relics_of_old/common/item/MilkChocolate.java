package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MilkChocolate extends RelicsItem{

	public MilkChocolate() {
		super(new Item.Properties().food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).alwaysEat().build()));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		pLivingEntity.removeAllEffects();
		return super.finishUsingItem(pStack, pLevel, pLivingEntity);
	}
}