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
	public int beamStrenght;
	public static final int MAX_BEAM_STRENGHT = 10;
	private boolean signal;

	public SkybeamBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SkybeamBlockEntity(BlockPos pos, BlockState state) {
		super(RelicsBlockEntities.SKYBEAM_BLOCK_ENTITY.get(), pos, state);
	}

	public void tick() {
		if (this.getBlockState().getValue(BlockStateProperties.LIT) && this.beamStrenght < 10) {
			this.beamStrenght = Math.min(this.beamStrenght + 1, MAX_BEAM_STRENGHT);
			this.signal = true;
			this.level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), getBlockState(), getBlockState(), 2, 1);
		} else if(!this.getBlockState().getValue(BlockStateProperties.LIT) && this.beamStrenght > 0){
			this.beamStrenght = Math.max(this.beamStrenght - 1, 0);
			this.signal = false;
			this.level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), getBlockState(), getBlockState(), 2, 1);
		}
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		if (pTag.contains(BEAM_STRENGHT_TAG)) {
			this.beamStrenght = pTag.getInt(BEAM_STRENGHT_TAG);
		}
		if (pTag.contains(SIGNAL_TAG)) {
			this.signal = pTag.getBoolean(SIGNAL_TAG);
		}
	}

	@Override
	public void saveAdditional(CompoundTag pTag) {
		pTag.putInt(BEAM_STRENGHT_TAG, this.beamStrenght);
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
