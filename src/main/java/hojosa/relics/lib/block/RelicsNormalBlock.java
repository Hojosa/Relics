package hojosa.relics.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RelicsNormalBlock extends Block
{
	public RelicsNormalBlock(Block material, SoundType soundType) {
        this(getInitProperties(material, soundType));
    }

    public RelicsNormalBlock(Properties properties) {
		super(properties);
	}
	private static Properties getInitProperties(Block material, SoundType soundType) {
        Properties properties = Properties.copy(material);
        properties.sound(soundType);
		return properties;
	}
	
	@Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean flag) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof Container container) {
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, flag);
        }
    }
}
