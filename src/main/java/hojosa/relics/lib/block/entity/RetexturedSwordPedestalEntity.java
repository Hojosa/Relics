package hojosa.relics.lib.block.entity;

import javax.annotation.Nonnull;

import hojosa.relics.common.block.entity.SwordPedestalBlockEntity;
import hojosa.relics.common.init.RelicsBlockEntities;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import slimeknights.mantle.block.entity.IRetexturedBlockEntity;
import slimeknights.mantle.util.RetexturedHelper;

public class RetexturedSwordPedestalEntity extends SwordPedestalBlockEntity implements IRetexturedBlockEntity {
	private static final String TAG_TEXTURE = "texture";

	@Nonnull
	@Getter
	private Block texture = Blocks.AIR;

	public RetexturedSwordPedestalEntity(BlockPos pos, BlockState state) {
		super(RelicsBlockEntities.REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY.get(), pos, state);
	}

	/* Textures */

	@Nonnull
	@Override
	public ModelData getModelData() {
		return RetexturedHelper.getModelData(texture);
	}

	@Override
	public String getTextureName() {
		return RetexturedHelper.getTextureName(texture);
	}

	private void textureUpdated() {
		// update the texture in BE data
		if (level != null && level.isClientSide) {
			Block normalizedTexture = texture == Blocks.AIR ? null : texture;
			ModelData data = getModelData();
			if (data.get(RetexturedHelper.BLOCK_PROPERTY) != normalizedTexture) {
				requestModelDataUpdate();
				BlockState state = getBlockState();
				level.sendBlockUpdated(worldPosition, state, state, 0);
			}
		}
	}

	@Override
	public void updateTexture(String name) {
		Block oldTexture = texture;
		texture = RetexturedHelper.getBlock(name);
		if (oldTexture != texture) {
			setChangedFast();
			textureUpdated();
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
			textureUpdated();
		}
	}
}
