package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.block.RelicsFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BoostPlate extends RelicsFacingBlock {

	public static final EnumProperty<BoostType> TYPE = EnumProperty.create("type", BoostType.class);
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 0.5, 16);

	public enum BoostType implements StringRepresentable {
		SPEED("speed"), JUMP("jump"), HEALING("heal");

		private final String name;

		BoostType(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	public BoostPlate(Properties properties) {
		super(properties.lightLevel(value -> 8));
		this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, BoostType.SPEED).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(TYPE));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

//    @Override
//    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
//        return Shapes.empty();
//    }

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canSupportRigidBlock(level, pos.below());
	}

//    @Override
//    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
//        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
//            return Blocks.AIR.defaultBlockState();
//        }
//        return state;
//    }

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.isShiftKeyDown()) {
			if (!level.isClientSide) {
				BoostType[] types = BoostType.values();
				BoostType next = types[(state.getValue(TYPE).ordinal() + 1) % types.length];
				level.setBlock(pos, state.setValue(TYPE, next), UPDATE_ALL);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!(entity instanceof LivingEntity living))
			return;
		BoostType type = state.getValue(TYPE);

		switch (type) {
		case SPEED -> {
			Vec3 motion = living.getDeltaMovement();
			double flatX = motion.x;
			double flatZ = motion.z;
			double speed = Math.sqrt(flatX * flatX + flatZ * flatZ);
			if (speed > 0.01) {
				double boost = 5.0;
				living.setDeltaMovement(flatX * boost / speed, motion.y, flatZ * boost / speed);
			}

			if (living.getEffect(MobEffects.MOVEMENT_SPEED) == null) {
				if (!level.isClientSide) {
					level.playSound(null, pos, RelicsSounds.SPEED_BOOST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
				}
				if (level.isClientSide) {
					Vec3 m = living.getDeltaMovement();
					for (int i = 0; i < 15; i++) {
						double vs = level.random.nextDouble() * 2;
						level.addParticle(ParticleTypes.FLAME, living.getX() + level.random.nextGaussian() * 0.4, living.getY(), living.getZ() + level.random.nextGaussian() * 0.4, m.x * vs, 0, m.z * vs);
					}
				}
			}

			if (!level.isClientSide) {
				living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 2));
			}
		}
		case JUMP -> {
			if (!level.isClientSide) {
				living.addEffect(new MobEffectInstance(MobEffects.JUMP, 20, 4));
			}
		}
		case HEALING -> {
			if (!level.isClientSide) {
				long lastHeal = living.getPersistentData().getLong("BoostPlateHealCooldown");
				if (level.getGameTime() - lastHeal >= 200) {
					living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 1));
					living.getPersistentData().putLong("BoostPlateHealCooldown", level.getGameTime());
				}
			}
		}
		}
	}
}
