package hojosa.relics_of_old.lib.block.entity;

import javax.annotation.Nonnull;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import slimeknights.mantle.block.entity.IRetexturedBlockEntity;
import slimeknights.mantle.util.RetexturedHelper;

public class RelicsRetexturedBlockEntity extends RelicsBlockEntity implements IRetexturedBlockEntity {
	private static final String TAG_TEXTURE = "texture";

	@Nonnull
	@Getter
	private Block texture = Blocks.AIR;

	public RelicsRetexturedBlockEntity(BlockEntityType<?> type, int containerSize, BlockPos blockPos, BlockState blockState) {
		super(type, containerSize, blockPos, blockState);
	}

	@Nonnull
	@Override
	public ModelData getModelData() {
		return RetexturedHelper.getModelData(texture);
	}

	@Override
	public String getTextureName() {
		return RetexturedHelper.getTextureName(texture);
	}

	@Override
	public void updateTexture(String name) {
		Block oldTexture = texture;
		texture = RetexturedHelper.getBlock(name);
		if (oldTexture != texture) {
			setChangedFast();
			if (level != null && level.isClientSide) {
				requestModelDataUpdate();
				BlockState state = getBlockState();
				level.sendBlockUpdated(worldPosition, state, state, 0);
			}
		}
	}

	@Override
	public void saveSynced(CompoundTag tags) {
		super.saveSynced(tags);
		if (texture != Blocks.AIR) {
			tags.putString(TAG_TEXTURE, getTextureName());
		}
	}

	@Override
	public void load(CompoundTag tags) {
		super.load(tags);
		if (tags.contains(TAG_TEXTURE, Tag.TAG_STRING)) {
			texture = RetexturedHelper.getBlock(tags.getString(TAG_TEXTURE));
		}
	}
}