package hojosa.relics.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.Material;


public class RelicsBlock extends Block
{
	public RelicsBlock(Material material, SoundType soundType) {
        this(getInitProperties(material, soundType));
    }

    public RelicsBlock(Properties properties) {
		super(properties);
	}
	private static Properties getInitProperties(Material material, SoundType soundType) {
        Properties properties = Properties.of(material);
        properties.sound(soundType);
		return properties;
	}
	
}
