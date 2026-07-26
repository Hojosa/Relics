package hojosa.relics_of_old.common.item;

import org.jetbrains.annotations.Nullable;

import hojosa.relics_of_old.common.item.entity.EmeraldPieceItemEntity;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmeraldPiece extends RelicsItem {

	public EmeraldPiece() {
		super(64);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public @Nullable Entity createEntity(Level level, Entity location, ItemStack stack) {
		EmeraldPieceItemEntity entity = new EmeraldPieceItemEntity(level, location.getX(), location.getY(), location.getZ(), stack);
		entity.setDeltaMovement(location.getDeltaMovement());

		if (location instanceof ItemEntity original && original.getOwner() instanceof Player) {
			entity.setPickUpDelay(40);
		}
		return entity;
	}
}