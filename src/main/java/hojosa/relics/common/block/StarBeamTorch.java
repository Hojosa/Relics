package hojosa.relics.common.block;

import java.util.Optional;

import hojosa.relics.client.particle.RelicsParticles;
import hojosa.relics.common.entity.StarBeamEntity;
import hojosa.relics.lib.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StarBeamTorch extends TorchBlock {
	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
	protected static final VoxelShape AABB = Block.box(0.4, 0.6, 0.4, 0.6, 1.0, 0.6);

	public StarBeamTorch(Properties pProperties) {
		super(pProperties, null);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		if (!pLevel.isClientSide && pEntity instanceof Player player  && !player.isPassenger() && player.isShiftKeyDown() && isOnTopOfBlock(player, pPos)) {
			// Find target torch and initiate riding
			BlockPos target = findNextTorch(pLevel, pPos);
			if (target != null) {
				StarBeamEntity rider = new StarBeamEntity(pLevel, pPos, target, player);
				pLevel.addFreshEntity(rider);
				player.startRiding(rider, true);
			}
		}
	}
	
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockPos placedOnPos = context.getClickedPos().relative(clickedFace.getOpposite());
        BlockState placedOnState = context.getLevel().getBlockState(placedOnPos);
        // Check if surface can support the torch
        if (!placedOnState.isFaceSturdy(context.getLevel(), placedOnPos, clickedFace)) {
            return this.defaultBlockState(); // Fallback to floor placement
        }
        return this.defaultBlockState().setValue(FACING, clickedFace);
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos attachedPos = pos.relative(facing.getOpposite());
        BlockState attachedState = level.getBlockState(attachedPos);
        
        return attachedState.isFaceSturdy(level, attachedPos, facing);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
    	return ShapeUtil.rotate(Block.box(6.0D, 5.5D, 6.0D, 10.0D, 16D, 10.0D), state.getValue(FACING));
    }

	
    private boolean isOnTopOfBlock(Entity entity, BlockPos pos) {
        return entity.getY() > pos.getY() && 
               entity.getBoundingBox().intersects(new AABB(pos));
    }
    
    //used to check initially if any torch is within 6 block radius, if not we dont even spawn the entity for travel. subsequent checks are done via the entity
    private BlockPos findNextTorch(Level level, BlockPos origin) {
    	int radius = 6;

    	Optional<BlockPos> matchingPos = BlockPos.withinManhattanStream(origin, radius, radius, radius).filter(pos -> level.getBlockState(pos).getBlock() instanceof StarBeamTorch && !pos.equals(origin)).findFirst();
    	if(matchingPos.isEmpty())
    		return null;

    	if(hasClearPath(level, origin.getCenter(), matchingPos.get().getCenter()))
    		return matchingPos.get();
    	else return null;
    }
    
    private boolean hasClearPath(Level level, Vec3 start, Vec3 end) {
        ClipContext context = new ClipContext(start, end, 
            ClipContext.Block.COLLIDER, // Check collision boxes
            ClipContext.Fluid.NONE,     // Ignore fluids
            null
        );
        BlockHitResult result = level.clip(context);
        return result.getType() == HitResult.Type.MISS; // True if no obstacles
    }
    
    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        double x = pPos.getX() + 0.5D;
        double y = pPos.getY() + 0.7D;
        double z = pPos.getZ() + 0.5D;
        
        double offsetX = pRandom.nextGaussian() * 0.08;
        double offsetY = pRandom.nextGaussian() * 0.08 - 0.04;
        double offsetZ = pRandom.nextGaussian() * 0.08;

        pLevel.addParticle(RelicsParticles.STAR_BEAM_TORCH_PATTICLES.get(), x + offsetX, y + offsetY, z + offsetZ, 0.0D, 0.0D, 0.0D);
    }
}