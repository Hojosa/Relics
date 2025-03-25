package hojosa.relics.common.item.entity;

import hojosa.relics.common.init.RelicsSounds;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeartItemEntity extends ItemEntity{

	public HeartItemEntity(Level pLevel, double pPosX, double pPosY, double pPosZ, ItemStack pItemStack) {
		super(pLevel, pPosX, pPosY, pPosZ, pItemStack);
	}

	@SuppressWarnings("resource")
	@Override
	public void playerTouch(Player player) {
		 if (!this.level().isClientSide) {
			 this.getItem().shrink(1);
			 player.heal(6);
			 super.playerTouch(player);
			 if (player instanceof ServerPlayer serverPlayer) {
				 serverPlayer.connection.send(new ClientboundSoundEntityPacket(RelicsSounds.HEART.getHolder().get(), getSoundSource(), player, 1.0f, 1.0f, 1l));
			 }
		 }
		 else {
			 this.level().playSound(player, this.blockPosition(), RelicsSounds.HEART.get(), SoundSource.BLOCKS);
		 }
	}
}
