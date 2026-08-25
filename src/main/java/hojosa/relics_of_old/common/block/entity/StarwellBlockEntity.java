package hojosa.relics_of_old.common.block.entity;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import slimeknights.mantle.block.entity.MantleBlockEntity;

public class StarwellBlockEntity extends MantleBlockEntity {

	public static final int FLIGHT_CHARGE_DURATION = 200;

	public float beamHeight;
	public int flightCharge = 0;
	// Which block sits above the core — checked each tick for render mode
	private Block blockAbove = Blocks.AIR;

	public StarwellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public StarwellBlockEntity(BlockPos pos, BlockState state) {
		super(RelicsBlockEntities.STARWELL_BLOCK_ENTITY.get(), pos, state);
	}

	public Block getBlockAbove() {
		return blockAbove;
	}

	public boolean isSkylensMode() {
		return blockAbove == RelicsBlocks.SKY_LENS.get() || (level != null && level.getBlockState(worldPosition.above()).is(RelicsBlocks.SKY_LENS.get()));
	}

	public void tick() {
		if (level == null)
			return;

		blockAbove = level.getBlockState(worldPosition.above()).getBlock();

		if (blockAbove == Blocks.AIR || blockAbove == RelicsBlocks.STARSTONE_BLOCK.get() || blockAbove == RelicsBlocks.INFUSED_STARSTONE_BLOCK.get()) {
			// Beam height scales with moon
			if (level.getGameTime() % 4L == 0L) {
				float moonAngle = (float) (-Math.cos(level.getSunAngle(0.0f)));
				float moonScale = 128.0f;
				if (blockAbove == Blocks.AIR) {
					moonScale = 10.0f;
				}
				// New moon = full power regardless
				if (level.getMoonPhase() == 0) {
					moonScale = 128.0f;
				}
				beamHeight = moonAngle > 0 ? moonAngle * moonScale : 0;
				if (blockAbove == RelicsBlocks.INFUSED_STARSTONE_BLOCK.get()) {
					beamHeight = 128.0f;
				}
			}
		} else if (isSkylensMode()) {
			tickSkylens();
		} else {
			beamHeight = 0;
		}
	}

	// Sky lens flight physics
	private void tickSkylens() {
		float ceiling = 32.0f;
		float zoneHeight = 128.0f;
		float zoneRadius = 1.5f;

		flightCharge--;
		if (flightCharge == 0 && level != null) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
		if (flightCharge < 0)
			flightCharge = 0;

		// Power boost from block above the lens
		Block powerBlock = level.getBlockState(worldPosition.above(2)).getBlock();
		if (powerBlock == RelicsBlocks.STARSTONE_BLOCK.get())
			ceiling = 48.0f;
		if (powerBlock == RelicsBlocks.INFUSED_STARSTONE_BLOCK.get())
			ceiling = 64.0f;

		beamHeight = ceiling * 1.5f;

		if (level.isClientSide)
			return;

		// Affect entities in the zone
		AABB zone = new AABB(worldPosition.getX() + 0.5 - zoneRadius, worldPosition.getY() + 2, worldPosition.getZ() + 0.5 - zoneRadius, worldPosition.getX() + 0.5 + zoneRadius, worldPosition.getY() + 1 + zoneHeight,
				worldPosition.getZ() + 0.5 + zoneRadius);

		List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, zone);
		for (LivingEntity entity : entities) {
			if (!(entity instanceof Player player))
				continue;
			if (player.onGround())
				continue;

			float height = (float) (player.getY() - player.getEyeHeight() - zone.minY);

			// Buoyancy + steering
			float steer = -player.getXRot() / 90.0f + 1.0f;
			float topDeadZone = 0.1f;
			float bottomDeadZone = 0.5f;
			float power = Math.min(steer * (1.0f + topDeadZone + bottomDeadZone) - bottomDeadZone, 1.0f);
			if (power < 0)
				power = 0;

			float dig = 2.0f;
			float targetHeight = (ceiling + dig) * power - dig;
			float tension = (targetHeight - height) / zoneHeight;
			float drag = 0.05f;
			float buoyancy = 0.08f;

			player.setDeltaMovement(player.getDeltaMovement().add(0, tension * 0.5 + buoyancy, 0));
			player.setDeltaMovement(player.getDeltaMovement().subtract(0, drag * player.getDeltaMovement().y, 0));
			player.fallDistance = 0;
			player.hurtMarked = true;
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		flightCharge = tag.getInt("flightCharge");
		beamHeight = tag.getFloat("beamHeight");
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.putInt("flightCharge", flightCharge);
		tag.putFloat("beamHeight", beamHeight);
		super.saveAdditional(tag);
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