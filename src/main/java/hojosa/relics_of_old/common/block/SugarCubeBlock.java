package hojosa.relics_of_old.common.block;

import java.util.List;

import hojosa.relics_of_old.common.init.RelicsParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;

public class SugarCubeBlock extends FallingBlock {

	public SugarCubeBlock() {
		super(Properties.of().mapColor(MapColor.SNOW).strength(0.25f).sound(SoundType.SAND));
	}

	// drops 9 sugar
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		return List.of(new ItemStack(Items.SUGAR, 9));
	}

	// dissolve when water neighbor changes
	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		level.scheduleTick(pos, this, this.getDelayAfterPlace());
		return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
	}

	// check for water contact on scheduled tick
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (touchesWater(level, pos)) {
			level.blockEvent(pos, this, 1, 0);
		} else {
			super.tick(state, level, pos, random); // falling block logic
		}
	}

	// dissolve in rain
	@Override
	public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
		if (precipitation == Biome.Precipitation.RAIN) {
			level.blockEvent(pos, this, 1, 0);
		}
	}

	// blockEvent id=1: dissolve with particles
	@Override
	public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
		if (id == 1) {
			if (level.isClientSide) {
				spawnDissolveParticles(level, pos);
			}
			SoundType sound = state.getSoundType();
			level.playSound(null, pos, sound.getBreakSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
			level.removeBlock(pos, false);
			if (!level.isClientSide) {
				popResource(level, pos, new ItemStack(Items.SUGAR, 9));
			}
			return true;
		}
		return super.triggerEvent(state, level, pos, id, param);
	}

	// also spawn dissolve particles on player break
	@Override
	protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
		if (level.isClientSide) {
			spawnDissolveParticles(level, pos);
		}
		SoundType sound = state.getSoundType();
		level.playSound(null, pos, sound.getBreakSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
	}

	// 4x4x4 grid of white block-texture particles flying outward
	private static void spawnDissolveParticles(Level level, BlockPos pos) {
		RandomSource random = level.random;
		for (int i = 0; i < 60; i++) {
			double x = pos.getX() + random.nextDouble();
			double y = pos.getY() + random.nextDouble();
			double z = pos.getZ() + random.nextDouble();
			level.addParticle(RelicsParticles.SUGAR_PARTICLE.get(), x, y, z, (random.nextDouble() - 0.5) * 0.3, 0, (random.nextDouble() - 0.5) * 0.3);
		}
	}

	private boolean touchesWater(BlockGetter level, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		for (Direction dir : Direction.values()) {
			if (dir == Direction.DOWN)
				continue;
			mutable.setWithOffset(pos, dir);
			if (level.getBlockState(mutable).getFluidState().is(FluidTags.WATER)) {
				return true;
			}
		}
		return false;
	}

	// falling block dust color
	@Override
	public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
		return 0xFFFFFF; // white
	}
}