package hojosa.relics_of_old.common.item;

import java.util.Random;

import hojosa.relics_of_old.common.block.MysticShrub;
import hojosa.relics_of_old.common.block.MysticShrub.ShrubState;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import hojosa.relics_of_old.lib.RelicsUtil;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MagicPowder extends RelicsItem {
	Random random = new Random();

	public MagicPowder() {
		super(64, Rarity.UNCOMMON);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
		if (!pPlayer.level().isClientSide) {
			// any animal with a variant is also an ageableMob, so we dont need to check
			// them here
			if (pInteractionTarget instanceof AgeableMob ageable) {
				// Toggle age state
				ageable.setBaby(!ageable.isBaby());
				if (pInteractionTarget instanceof Animal animal) {
					RelicsUtil.cycleMobVariant(animal);
					return finishInteractionEntity(pPlayer, pUsedHand);
				}
				return finishInteractionEntity(pPlayer, pUsedHand);
			} // todo, doesn work yet
			if (pInteractionTarget instanceof Creeper crepper) {
				crepper.getEntityData().set(Creeper.DATA_IS_POWERED, crepper.isPowered());
				pPlayer.getItemInHand(pUsedHand).shrink(1);
				return InteractionResult.SUCCESS;
			}
			if (pInteractionTarget instanceof Zombie zombie) {
				if (zombie.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
					if (random.nextInt(2) == 0)
						zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.PUMPKIN));
					else
						zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.MELON));
				} else
					zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.AIR));
				pPlayer.getItemInHand(pUsedHand).shrink(1);
				return finishInteractionEntity(pPlayer, pUsedHand);
			}
		}
		if (pInteractionTarget instanceof AgeableMob || pInteractionTarget instanceof Zombie || pInteractionTarget instanceof Creeper) {
			clientEffects(pPlayer, pInteractionTarget.getX(), pInteractionTarget.getY() + pInteractionTarget.getBbHeight() / 2, pInteractionTarget.getZ());
			return InteractionResult.SUCCESS;
		}
		return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		// Get the clicked block and world
		BlockState clickedBlock = context.getLevel().getBlockState(context.getClickedPos());
		Level level = context.getLevel();
		if (RelicsUtil.hasBlockToCycle(clickedBlock.getBlock())) {
			if (!level.isClientSide) {
				Block newBlock = RelicsUtil.getNextBlock(clickedBlock.getBlock());
				if (newBlock != Blocks.AIR) {
					level.setBlockAndUpdate(context.getClickedPos(), newBlock.defaultBlockState());
					if (!context.getPlayer().isCreative())
						stack.shrink(1);
					return InteractionResult.SUCCESS;
				}
				return super.onItemUseFirst(stack, context);
			} else {
				clientEffects(context.getPlayer(), context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 0.5, context.getClickedPos().getZ() + 0.5);
				return InteractionResult.SUCCESS;
			}
		}
		// custom block change to a specific state, no cycle
		if (clickedBlock.is(RelicsBlocks.MYSTIC_SHRUB.get()) && clickedBlock.getValue(MysticShrub.STATE) == ShrubState.STUMP) {
			if (!level.isClientSide) {
				var targetBlock = clickedBlock.setValue(MysticShrub.STATE, ShrubState.NORMAL);

				if (targetBlock != null) {
					level.setBlockAndUpdate(context.getClickedPos(), targetBlock);
					if (!context.getPlayer().isCreative())
						stack.shrink(1);
					return InteractionResult.SUCCESS;
				}
			}
		} else if (RelicsUtil.hasStateToCycle(clickedBlock.getBlock())) {
			if (!level.isClientSide) {
				var targetState = RelicsUtil.cycleBlockState(clickedBlock);

				if (targetState != null) {
					level.setBlockAndUpdate(context.getClickedPos(), targetState);
					if (!context.getPlayer().isCreative())
						stack.shrink(1);
					return InteractionResult.SUCCESS;
				}
			} else {
				clientEffects(context.getPlayer(), context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 0.5, context.getClickedPos().getZ() + 0.5);
				return InteractionResult.SUCCESS;
			}
		}
		return super.onItemUseFirst(stack, context);
	}

	private void clientEffects(Player pPlayer, double x, double y, double z) {
		pPlayer.level().playSound(pPlayer, BlockPos.containing(x, y, z), RelicsSounds.SPRINKLE.get(), SoundSource.PLAYERS, 0.2f, 1.0f);
		pPlayer.level().playSound(pPlayer, BlockPos.containing(x, y, z), RelicsSounds.TRANSFORM.get(), SoundSource.PLAYERS, 1.0f, 0.7f + random.nextFloat() * 0.5f);
		pPlayer.level().addParticle(ParticleTypes.EXPLOSION, x, y, z, 0.0, 0.0, 0.0);
		for (int i = 0; i < 20; i++)
			pPlayer.level().addParticle(new RelicsParticleOptions(RelicsParticles.SPARKLE_PARTICLES, 6, 0.1f), x + random.nextGaussian() * 0.5f, y + random.nextGaussian() * 0.5, z + random.nextGaussian() * 0.5f, 0.0,
					-0.02f, 0.0);
	}

	private static InteractionResult finishInteractionEntity(Player player, InteractionHand usedHand) {
		if (!player.isCreative())
			player.getItemInHand(usedHand).shrink(1);
		player.level().playSound(player, player.blockPosition(), RelicsSounds.INFUSE_SUCCESS.get(), SoundSource.BLOCKS);
		return InteractionResult.SUCCESS;
	}
}