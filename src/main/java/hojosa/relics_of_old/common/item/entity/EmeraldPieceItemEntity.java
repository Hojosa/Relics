package hojosa.relics_of_old.common.item.entity;

import hojosa.relics_of_old.common.init.RelicsSounds;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmeraldPieceItemEntity extends ItemEntity {

	public EmeraldPieceItemEntity(Level pLevel, double pPosX, double pPosY, double pPosZ, ItemStack pItemStack) {
		super(pLevel, pPosX, pPosY, pPosZ, pItemStack);
		this.setDefaultPickUpDelay();
	}

	@Override
	public void playerTouch(Player player) {
		if (!this.level().isClientSide && !this.hasPickUpDelay()) {
			super.playerTouch(player);
			
			if (this.isRemoved() && player instanceof ServerPlayer serverPlayer) {
				serverPlayer.connection.send(new ClientboundSoundEntityPacket(RelicsSounds.EMERALD_PIECE_PICKUP.getHolder().get(), getSoundSource(), player, 1.0f, 1.0f, 1l));
			}
		}
	}
	
}