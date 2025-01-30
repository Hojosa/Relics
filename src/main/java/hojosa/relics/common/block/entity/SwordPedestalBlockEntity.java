package hojosa.relics.common.block.entity;

import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.init.RelicsTags;
import hojosa.relics.lib.block.entity.RelicsBlockEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SwordPedestalBlockEntity extends RelicsBlockEntity {
	public static final String INFUSED_TAG = "Infused";
	public static final String GLOW_TAG = "Glow";
	public static final String COLOR_TAG = "Color";
	@Getter
	private int glowColor = 0x6699cc;
	@Getter
	private boolean isInfused = false;
	@Getter
	private boolean isGlowing = false;

	public SwordPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), 1, blockPos, blockState);
	}

	public SwordPedestalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, 1, pos, state);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return stack.is(ItemTags.SWORDS) || stack.is(RelicsTags.Items.SWORD_PEDESTAL_SWORDS);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains(INFUSED_TAG)) {
			this.isInfused = tag.getBoolean(INFUSED_TAG);
		}
		if (tag.contains(GLOW_TAG)) {
			this.isGlowing = tag.getBoolean(GLOW_TAG);
		}
		if (tag.contains(COLOR_TAG)) {
			this.glowColor = tag.getInt(COLOR_TAG);
		}
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.putBoolean(INFUSED_TAG, this.isInfused);
		tag.putBoolean(GLOW_TAG, this.isGlowing);
		tag.putInt(COLOR_TAG, this.glowColor);
		super.saveAdditional(tag);
	}

	public void repairSword() {
		if (!this.isEmpty()) {
			ItemStack sword = this.itemHandler.extractItem(0, 1, false);
			sword.setDamageValue(sword.getDamageValue() - 1);
			this.itemHandler.insertItem(0, sword, false);
		}
	}

	public boolean isSwordDamaged() {
		return this.itemHandler.getStackInSlot(0).getDamageValue() > 0;
	}

	public void infusePedestal() {
		this.isInfused = true;
		setChanged();

	}

	public void setGlowColor(int color) {
		this.glowColor = color;
		this.requestModelDataUpdate();
		setChanged();
	}

	public void glowPedestal() {
		this.isGlowing = true;
		setChanged();
	}
}
