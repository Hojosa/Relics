package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class RockCandyItem extends RelicsItem {

	private final MobEffect bonusEffect;
	private final int amplifier;

	public RockCandyItem(MobEffect bonusEffect, int amplifier) {
		super(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(1).saturationMod(0).alwaysEat().build()));
		this.bonusEffect = bonusEffect;
		this.amplifier = amplifier;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		super.finishUsingItem(stack, level, entity);
		if (!level.isClientSide) {
			// all candies give Speed III for 15s
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 2));
			// unique bonus effect
			entity.addEffect(new MobEffectInstance(bonusEffect, 300, amplifier));
		}
		// return stick
		return new ItemStack(Items.STICK);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}
}