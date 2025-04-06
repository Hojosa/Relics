package hojosa.relics.common.block.entity;

import javax.annotation.Nullable;

import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.init.RelicsSounds;
import hojosa.relics.common.recipes.MagicInfusionRecipce;
import hojosa.relics.lib.block.entity.RelicsBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class InfusedStarstoneBlockEntity extends RelicsBlockEntity {
	private int progress;
	private boolean active;

	@Nullable
	private MagicInfusionRecipce lastRecipe;

	public InfusedStarstoneBlockEntity(BlockPos pPos, BlockState pState) {
		super(RelicsBlockEntities.INFUSED_STARSTONE_BLOCK_ENTITY.get(), 4, pPos, pState);
	}

	public void tick() {
		if (this.emptySlots() != 0) {
			this.reset();
			this.setChangedFast();
			return;
		}
		if (this.isActive()) {
			var recipe = this.getActiveRecipe();

			if (recipe != null) {
				this.progress++;

				if (this.progress >= 100) {
					for (int i = 0; i < this.getContainerSize(); i++) {
						this.removeItem(i, 1);
					}
					ItemEntity item = new ItemEntity(this.level, this.getBlockPos().getX() + 0.5d, this.getBlockPos().getY() + 1.2d, this.getBlockPos().getZ() + 0.5d, recipe.getResultItem(null));
					item.setPickUpDelay(15);
					this.level.addFreshEntity(item);
					level.markAndNotifyBlock(this.getBlockPos(), level.getChunkAt(getBlockPos()), getBlockState(), getBlockState(), 2, 1);
					level.playSound(null, getBlockPos(), RelicsSounds.ITEM_GET.get(), SoundSource.BLOCKS);
					this.lastRecipe = recipe;
				} else {
					for (int i = 0; i < 4; i++) {
						ItemStack item = getItem(i);
						spawnItemParticles(getBlockPos(), item, i);
					}
					if(progress == 1 || progress % 25 == 0 && progress != 100)
						level.playSound(null, getBlockPos(), RelicsSounds.MAGIC_CRAFTING.get(), SoundSource.BLOCKS);
				}
			} else {
				this.reset();
			}
		}
		this.setChangedFast();
	}

	private void spawnItemParticles(BlockPos blockPos, ItemStack stack, int i) {
		if (this.level == null || this.level.isClientSide() || stack.isEmpty())
			return;

		ServerLevel level = (ServerLevel) this.level;

		double angle = Math.toRadians(270 + i * 90);
		double xOffset = 0.3 * Math.cos(angle);
		double zOffset = 0.3 * Math.sin(angle);

		double x = blockPos.getX() + 0.5 + xOffset;
		double y = blockPos.getY() + 1.1;
		double z = blockPos.getZ() + 0.5 + zOffset;
		level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), x, y, z, 0, blockPos.getX() + 0.5 - x, 1, blockPos.getZ() + 0.5 - z, 0.18D);

	}

	public boolean isActive() {
		if (!this.active) {
			this.active = this.level != null && this.level.hasNeighborSignal(this.getBlockPos());
		}
		return this.active;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.progress = tag.getInt("Progress");
		this.active = tag.getBoolean("Active");
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("Progress", this.progress);
		tag.putBoolean("Active", this.active);
	}

	private void reset() {
		this.progress = 0;
		this.active = false;
	}

	public MagicInfusionRecipce getActiveRecipe() {
		if (this.level == null)
			return null;

		if (lastRecipe != null && lastRecipe.matches(this, level))
			return lastRecipe;

		return this.level.getRecipeManager().getRecipeFor(MagicInfusionRecipce.Type.INSTANCE, this, level).orElse(null);
	}
}
