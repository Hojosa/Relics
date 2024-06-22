package hojosa.relics.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;



public class RelicsBlock extends Block
{
	public RelicsBlock(Block material, SoundType soundType) {
        this(getInitProperties(material, soundType));
    }

    public RelicsBlock(Properties properties) {
		super(properties);
	}
	private static Properties getInitProperties(Block material, SoundType soundType) {
        Properties properties = Properties.copy(material);
        properties.sound(soundType);
		return properties;
	}
	
}
