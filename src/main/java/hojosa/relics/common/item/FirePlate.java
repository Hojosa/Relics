package hojosa.relics.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;


public class FirePlate extends RelicsItem
{
	public FirePlate(int stackSize, Rarity raity)
	{
		super(stackSize, raity);
	}
	
	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected)
	{
		Player player = (Player) pEntity;
		player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1));
	}
}
