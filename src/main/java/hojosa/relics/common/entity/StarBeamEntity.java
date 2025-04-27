package hojosa.relics.common.entity;

import java.util.Optional;

import org.joml.Vector3f;

import hojosa.relics.client.particle.RelicsParticles;
import hojosa.relics.common.block.StarBeamTorch;
import hojosa.relics.common.init.RelicsEntities;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StarBeamEntity extends Entity {
	
	@Getter
	private static final EntityDataAccessor<BlockPos> DATA_ID_TARGET_POS = SynchedEntityData.defineId(FallingStarEntity.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<BlockPos> DATA_ID_START_POS = SynchedEntityData.defineId(FallingStarEntity.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Vector3f> DATA_ID_MOTION = SynchedEntityData.defineId(FallingStarEntity.class, EntityDataSerializers.VECTOR3);

    private Player player;
	    
	    public StarBeamEntity(EntityType<?> pEntityType, Level pLevel) {
			super(pEntityType, pLevel);
	    }

	    public StarBeamEntity(Level level, BlockPos start, BlockPos target, Player player) {
	        super(RelicsEntities.STARBEAM.get(), level);
	        this.setStartPos(start.immutable());
	        this.setTargetPos(target.immutable());

	        this.setPos(start.getX() + 0.5, start.getY()+0.8, start.getZ() + 0.5);
	        this.player = player;
	        this.calcMotionVec();
	        this.player.startRiding(this);
	    }
	    
	    @Override
	    public void load(CompoundTag pCompound) {
	    	super.load(pCompound);
	    	this.calcMotionVec();
	    }
	    
	    @SuppressWarnings("resource")
		@Override
	    public void tick() {
	        if(!this.level().isClientSide && this.getPassengers().isEmpty()) {
	        		if(this.player == null)
	        			this.discard();
	        		else this.player.startRiding(this);
        	}
    		if(!this.getPassengers().isEmpty() && this.getFirstPassenger().isShiftKeyDown()) {
    			this.move(MoverType.SELF, new Vec3(this.getMotion()).scale(0.5));
    		}
    		else this.move(MoverType.SELF, new Vec3(this.getMotion()));
    		
    		if(this.level().isClientSide) {
    	        double offsetX = random.nextGaussian() * 0.08;
    	        double offsetZ = random.nextGaussian() * 0.08;
    		
    			this.level().addParticle(RelicsParticles.STAR_BEAM_GRIND_PATTICLES.get(), this.xo + offsetX, this.yo - 0.3, this.zo + offsetZ, 0.0D, 0.0D, 0.0D);
    		}
	        if(!this.level().isClientSide && this.position().distanceToSqr(this.entityData.get(DATA_ID_TARGET_POS).getCenter()) < 0.15 && !getNextTarget(this.entityData.get(DATA_ID_TARGET_POS))) {
		        	this.discard();
	        }   
	    }
	    
	    @Override
	    public boolean shouldRiderSit() {
	    	return false;
	    }
	    
	    private boolean getNextTarget(BlockPos checkPoint) {
	    	int radius = 6;
	    	Optional<BlockPos> matchingPos = BlockPos.withinManhattanStream(checkPoint, radius, radius, radius).filter(pos -> level().getBlockState(pos).getBlock() instanceof StarBeamTorch && !pos.equals(checkPoint) && !pos.equals(this.getStartPos())).findFirst();
	    	if(matchingPos.isEmpty()) {
	    		return false;
	    	}
	    	
	    	if(hasClearPath(level(), checkPoint.getCenter(), matchingPos.get().getCenter())) {
	    		this.setStartPos(checkPoint.immutable());
	    		this.setTargetPos(matchingPos.get().immutable());
	        	this.calcMotionVec();
	        	return true;
	    	}
	    	else return false;
	    }
	    
	    //calc the motion vector to travel from startPos to targetPos
	    private void calcMotionVec() {
	    	this.setMotion(new Vector3f(
		    	this.getTargetPos().getX() - this.getStartPos().getX(),
		    	this.getTargetPos().getY() - this.getStartPos().getY(),
		    	this.getTargetPos().getZ() - this.getStartPos().getZ()
	    	).normalize().mul(0.25f));
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
	    
	    @Override protected void defineSynchedData() {
	    	this.entityData.define(DATA_ID_TARGET_POS, BlockPos.ZERO);
	    	this.entityData.define(DATA_ID_START_POS, BlockPos.ZERO);
	    	this.entityData.define(DATA_ID_MOTION, new Vector3f(0));
	    }
	    
	    //we have to read/add our pos + motion, so a player is not stuck on rejoin
	    @Override protected void readAdditionalSaveData(CompoundTag tag) {
	    	this.setStartPos(BlockPos.of(tag.getLong("StartPos")));
	        this.setTargetPos(BlockPos.of(tag.getLong("TargetPos")));
	    }
	    @Override protected void addAdditionalSaveData(CompoundTag tag) {
	    	tag.putLong("StartPos", getStartPos().asLong());
	        tag.putLong("TargetPos", getTargetPos().asLong());
	    }
	    
	    public BlockPos getStartPos() {
	    	return this.entityData.get(DATA_ID_START_POS);
	    }
	    
	    public BlockPos getTargetPos() {
	    	return this.entityData.get(DATA_ID_TARGET_POS);
	    }
	    
	    public Vector3f getMotion() {
	    	return this.entityData.get(DATA_ID_MOTION);
	    }

	    private void setStartPos(BlockPos pos) {
	    	this.entityData.set(DATA_ID_START_POS, pos);
	    }
	    
	    private void setTargetPos(BlockPos pos) {
	    	this.entityData.set(DATA_ID_TARGET_POS, pos);
	    }
	    
	    private void setMotion(Vector3f motion) {
	    	this.entityData.set(DATA_ID_MOTION, motion);
	    }
}
