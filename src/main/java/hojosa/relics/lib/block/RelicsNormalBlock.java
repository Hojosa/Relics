package hojosa.relics.lib.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

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
}
