package hojosa.relics.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.dedicated.Settings;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;

public class PaintbrushItem extends Item {
    public PaintbrushItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        // Open GUI to select texture from inventory
        // ...

        // Apply selected texture to block
        BlockState newState = state.with(Properties.TEXTURE, selectedTexture);
        world.setBlockState(pos, newState);

        return ActionResult.SUCCESS;
    }
}
