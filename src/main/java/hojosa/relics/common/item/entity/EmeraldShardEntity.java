package hojosa.relics.common.item.entity;

import hojosa.relics.common.init.RelicsSounds;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmeraldShardEntity extends ItemEntity{

	public EmeraldShardEntity(Level pLevel, double pPosX, double pPosY, double pPosZ, ItemStack pItemStack) {
		super(pLevel, pPosX, pPosY, pPosZ, pItemStack);
	}

	@Override
	public void playerTouch(Player player) {
		 if (!this.level().isClientSide) {
			 super.playerTouch(player);
			 if (player instanceof ServerPlayer serverPlayer) {
				 serverPlayer.connection.send(new ClientboundSoundEntityPacket(RelicsSounds.EMERALD_PICKUP.getHolder().get(), getSoundSource(), player, 1.0f, 1.0f, 1l));
			 }
		 }
		 else {
			 this.level().playSound(player, this.blockPosition(), RelicsSounds.EMERALD_PICKUP.get(), SoundSource.BLOCKS);
		 }
	}
}
