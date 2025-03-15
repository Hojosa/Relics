package hojosa.relics.common.block.entity;

import org.jetbrains.annotations.NotNull;

import hojosa.relics.common.init.RelicsBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import slimeknights.mantle.block.entity.MantleBlockEntity;

public class SkybeamBlockEntity extends MantleBlockEntity {
	public static final String BEAM_STRENGHT_TAG = "beam_strenght";
	public static final String SIGNAL_TAG = "signal";
	public int beam_strenght;
	public static int MAX_BEAM_STRENGHT = 10;
	public boolean signal;

	public SkybeamBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SkybeamBlockEntity(BlockPos pos, BlockState state) {
		super(RelicsBlockEntities.SKYBEAM_BLOCK_ENTITY.get(), pos, state);
	}

	public void tick() {
		if (this.getBlockState().getValue(BlockStateProperties.LIT) && this.beam_strenght < 10) {
			this.beam_strenght = Math.min(this.beam_strenght + 1, MAX_BEAM_STRENGHT);
			this.signal = true;
			this.level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), getBlockState(), getBlockState(), 2, 1);
		} else if(!this.getBlockState().getValue(BlockStateProperties.LIT) && this.beam_strenght > 0){
			this.beam_strenght = Math.max(this.beam_strenght - 1, 0);
			this.signal = false;
			this.level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), getBlockState(), getBlockState(), 2, 1);
		}
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		if (pTag.contains(BEAM_STRENGHT_TAG)) {
			this.beam_strenght = pTag.getInt(BEAM_STRENGHT_TAG);
		}
		if (pTag.contains(SIGNAL_TAG)) {
			this.signal = pTag.getBoolean(SIGNAL_TAG);
		}
	}

	@Override
	public void saveAdditional(CompoundTag pTag) {
		pTag.putInt(BEAM_STRENGHT_TAG, this.beam_strenght);
		pTag.putBoolean(SIGNAL_TAG, this.signal);
		super.saveAdditional(pTag);
	}

	@Override
	protected boolean shouldSyncOnUpdate() {
		return true;
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		return this.serializeNBT();
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
	}
	
    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
