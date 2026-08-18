package hojosa.relics_of_old.common.item;

import org.jetbrains.annotations.Nullable;

import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.item.entity.EmeraldShardItemEntity;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmeraldShard extends RelicsItem {
	
	private static int exchangeRate = 8;

	public EmeraldShard() {
		super(64);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public @Nullable Entity createEntity(Level level, Entity location, ItemStack stack) {
		EmeraldShardItemEntity entity = new EmeraldShardItemEntity(level, location.getX(), location.getY(), location.getZ(), stack);
		entity.setDeltaMovement(location.getDeltaMovement());

		if (location instanceof ItemEntity original && original.getOwner() instanceof Player) {
			entity.setPickUpDelay(40);
		}
		return entity;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		if (pPlayer.getItemInHand(pUsedHand).getCount() < exchangeRate)
			return InteractionResultHolder.fail(stack);

		var mergedStack = new ItemStack(RelicsItems.EMERALD_PIECE.get());
		stack.setCount(stack.getCount() - exchangeRate);
		if (pLevel.isClientSide) {
			pLevel.playSound(pPlayer, pPlayer.blockPosition(), RelicsSounds.EMERALD_PIECE_PICKUP.get(), SoundSource.PLAYERS);
		}
		if (!pPlayer.getInventory().add(mergedStack))
			pPlayer.drop(mergedStack, false);
		return InteractionResultHolder.success(stack);
	}
}