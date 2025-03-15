package hojosa.relics.common.block;

import java.util.List;

import hojosa.relics.common.block.entity.InfusedStarstoneBlockEntity;
import hojosa.relics.lib.block.GlintBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InfusedStarstoneBlock extends GlintBlock {
	
    private static final double[][] SLOTS = {
            {0.37, 0.62, 0.00, 0.37}, // Slot 1
            {0.62, 0.99, 0.37, 0.61}, // Slot 2
            {0.38, 0.62, 0.62, 0.99}, // Slot 3
            {0.00, 0.37, 0.38, 0.62}  // Slot 4
        };

	public InfusedStarstoneBlock(Properties properties, boolean raindbow) {
		super(properties, raindbow);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new InfusedStarstoneBlockEntity(pPos, pState);
	}
	
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("Activate with Redstone"));
    }
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

		if(!pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND && pHit.getDirection() == Direction.UP){
			InfusedStarstoneBlockEntity blockEntity = (InfusedStarstoneBlockEntity) pLevel.getBlockEntity(pPos);
			
				int slot = lookingAtSlot(pHit);
				if (slot != -1) {
					ItemStack slotStack = blockEntity.getItem(slot);
					ItemStack itemInHand = pPlayer.getItemInHand(pHand);

					if(!itemInHand.isEmpty() || !slotStack.isEmpty()) {
						blockEntity.setItem(slot, itemInHand.split(1));
						pPlayer.addItem(slotStack);
						pLevel.playSound(null, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
						return InteractionResult.SUCCESS;
					}
					return InteractionResult.PASS;
				}
				return InteractionResult.PASS;
			}
		return InteractionResult.FAIL;
	}
	
	private int lookingAtSlot(BlockHitResult pHit) {
        double hitX = pHit.getLocation().x - pHit.getBlockPos().getX();
        double hitZ = pHit.getLocation().z - pHit.getBlockPos().getZ();
        
	    for (int i = 0; i < SLOTS.length; i++) {
	    	if(hitX >= SLOTS[i][0] && hitX <= SLOTS[i][1] && hitZ >= SLOTS[i][2] && hitZ <= SLOTS[i][3]) {
	            return i;
	        }
	    }
	    return -1;
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            // We don't have anything to do on the client side
            return null;
        } else {
            // Server side we delegate ticking to our block entity
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof InfusedStarstoneBlockEntity be) {
                    be.tick();
                }
            };
        }
	}
}
