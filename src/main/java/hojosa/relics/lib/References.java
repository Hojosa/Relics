package hojosa.relics.lib;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class References 

{
	public static final String MODID = "relics";
	public static final String VERSION = "0.3";
	public static String CREATIVE_TAB = "relics";

    public static class UnlocalizedName {
    	public static final String LAPIS_BRICK = "lapis_brick";
		public static final String SWORD_PEDESTAL = "sword_pedestal";
		public static final String SWORD_PEDESTAL_TIME = "sword_pedestal_time";
		public static final String SWORD_PLACE_SOUND = "place_sword";
		public static final String FIRE_PLATE = "fire_tablet";
		public static final String FIRE_SWORD = "flame_sword";
		public static final String WATER_PLATE = "water_tablet";
		public static final String CLAY_BLUE = "clay_blue";
		public static final String BRICK_BLUE = "brick_blue";
		public static final String MASTER_SWORD = "master_sword";
		public static final String SWORD_PEDESTAL_TWILIGHT = "sword_pedestal_twilight";
    }
    
    public static class PedestalShapes {
    	public static final VoxelShape BASIC_SHAPE = Block.box(2.0D, 0.0D, 5.0D, 14.0D, 6.0D, 11.0D);
    	public static final VoxelShape BASIC_SWORD_SHAPE = Shapes.or(Block.box(2,6,7.5,14,25.2,8.5), BASIC_SHAPE);
    	public static final VoxelShape TIME_SHAPE = Block.box(1.0D, 0.0D, 4.5D, 15.0D, 4.1D, 11.5D);
    	public static final VoxelShape TIME_SWORD_SHAPE = Shapes.or(Block.box(2,4,7.5,14,25.2,8.5), TIME_SHAPE);
    	public static final VoxelShape TWILIGHT_SHAPE = Block.box(0.15D, 0.0D, 2.4D, 15.9D, 4D, 13.5D);
    	public static final VoxelShape TWILIGHT_SWORD_SHAPE = Shapes.or(Block.box(2,3.9,7.5,14,25.2,8.5), TWILIGHT_SHAPE);

    }
}