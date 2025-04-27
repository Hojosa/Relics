package hojosa.relics.common.item;

import java.util.Random;

import hojosa.relics.common.init.RelicsSounds;
import hojosa.relics.lib.RelicsUtil;
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
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MagicPowder extends RelicsItem {
	Random random = new Random();
	public MagicPowder() {
		super(64);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
		if(!pPlayer.level().isClientSide) {
			//any animal with a variant is also an ageableMob, so we dont need to check them here
			if (pInteractionTarget instanceof AgeableMob ageable) {
				// Toggle age state
				ageable.setBaby(!ageable.isBaby());
				if(pInteractionTarget instanceof Animal animal) {
					RelicsUtil.cycleMobVariant(animal);
					return finishInteractionEntity(pPlayer, pUsedHand);
				}
				return finishInteractionEntity(pPlayer, pUsedHand);
			}//todo, doesn work yet
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
		if(pInteractionTarget instanceof AgeableMob || pInteractionTarget instanceof Zombie || pInteractionTarget instanceof Creeper) {
			pPlayer.level().playSound(pPlayer, pInteractionTarget.blockPosition(), RelicsSounds.INFUSE_SUCCESS.get(), SoundSource.BLOCKS);
			pPlayer.level().addParticle(ParticleTypes.EXPLOSION, pInteractionTarget.getX(), pInteractionTarget.getY() + pInteractionTarget.getBbHeight() / 2, pInteractionTarget.getZ(), 0.0, 0.0, 0.0);
			return InteractionResult.SUCCESS;
		}
		return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
	}
	
	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        // Get the clicked block and world
        BlockState clickedBlock = context.getLevel().getBlockState(context.getClickedPos());
        Level level = context.getLevel();
        if(RelicsUtil.hasBlockToCycle(clickedBlock.getBlock())) {
	        if(!level.isClientSide) {
	        	Block newBlock = RelicsUtil.getNextBlock(clickedBlock.getBlock());
	        	if(!(newBlock == Blocks.AIR)) {
	        		level.setBlockAndUpdate(context.getClickedPos(), newBlock.defaultBlockState());
	        		if(!context.getPlayer().isCreative())
	        			stack.shrink(1);
	        		return InteractionResult.SUCCESS;
	        	}
	    		return super.onItemUseFirst(stack, context);
	        }
	        else {
	        	level.playSound(context.getPlayer(), context.getClickedPos(), RelicsSounds.MAGIC_POWDER.get(), SoundSource.PLAYERS);
	        	level.addParticle(ParticleTypes.EXPLOSION, context.getPlayer().getX(), context.getPlayer().getY() + context.getPlayer().getBbHeight() / 2, context.getPlayer().getZ(), 0.0, 0.0, 0.0);
	        	return InteractionResult.SUCCESS;
	        }
        }
		return super.onItemUseFirst(stack, context);
	}

	private static InteractionResult finishInteractionEntity(Player player, InteractionHand usedHand) {
		if(!player.isCreative())
			player.getItemInHand(usedHand).shrink(1);
		player.level().playSound(player, player.blockPosition(), RelicsSounds.INFUSE_SUCCESS.get(), SoundSource.BLOCKS);
		return InteractionResult.SUCCESS;
	}
}
