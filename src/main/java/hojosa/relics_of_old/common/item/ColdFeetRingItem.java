package hojosa.relics_of_old.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

public class ColdFeetRingItem extends MagicRingItem {
	
	public ColdFeetRingItem() {
		super("Freeze water underfoot");
	}

	@Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        AABB box = player.getBoundingBox().inflate(0.25);
        int y = (int) Math.floor(box.minY);

        for (int x = (int) Math.floor(box.minX); x <= Math.floor(box.maxX); x++) {
            for (int z = (int) Math.floor(box.minZ); z <= Math.floor(box.maxZ); z++) {
                BlockPos pos = new BlockPos(x, y, z);
                BlockState state = player.level().getBlockState(pos);
                // only freeze source water blocks (level 0)
                if ((state.is(Blocks.WATER) || state.is(Blocks.WATER)) && state.getValue(LiquidBlock.LEVEL) == 0) {
                    // LG2 uses ThawingIceBlock — using frosted ice as 1.20.1 equivalent
                    player.level().setBlockAndUpdate(pos, Blocks.FROSTED_ICE.defaultBlockState());
                }
            }
        }
    }
}