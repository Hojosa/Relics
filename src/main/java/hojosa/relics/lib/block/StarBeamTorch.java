package hojosa.relics.lib.block;

import java.util.Optional;

import hojosa.relics.common.entity.StarBeamEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StarBeamTorch extends TorchBlock {

	public StarBeamTorch(Properties pProperties, ParticleOptions pFlameParticle) {
		super(pProperties, pFlameParticle);
	}

	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {

		if (!pLevel.isClientSide && pEntity instanceof Player player  && !player.isPassenger() && player.isShiftKeyDown() && isOnTopOfBlock(player, pPos)) {
			// Find target torch and initiate riding
			BlockPos target = findNextTorch(pLevel, pPos, player.getDirection());
			if (target != null) {
				StarBeamEntity rider = new StarBeamEntity(pLevel, pPos, target, player);
				pLevel.addFreshEntity(rider);
				player.startRiding(rider, true);
			}
		}
	}
	
	
	
    private boolean isOnTopOfBlock(Entity entity, BlockPos pos) {
        return entity.getY() > pos.getY() && 
               entity.getBoundingBox().intersects(new AABB(pos));
    }
    
    //used to check initially if any torch is within 6 block radius, if not we dont even spawn the entity for travel. subsequent checks are done via the entity
    private BlockPos findNextTorch(Level level, BlockPos origin, Direction facing) {
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
}