package hojosa.relics.lib;

import java.util.EnumMap;
import java.util.Map;

import hojosa.relics.common.init.RelicsItems;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.VoxelShape;

public class References 

{
	public static final String MODID = "relics";
//	public static final String MODNAME = "Mystic Water";
	public static final String VERSION = "0.3";
	public static String CREATIVE_TAB = "relics";
//	public static final String CLIENTPROXYLOCATION = MODID + ".core.proxy.ClientProxy";
//	public static final String COMMONPROXYLOCATION = MODID + ".core.proxy.CommonProxy";
//	public static final String RESOURCESPREFIX = MODID.toLowerCase() + ":";
	
    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(CREATIVE_TAB) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RelicsItems.FIRE_PLATE.get());
        }
    };

    public static class UnlocalizedName {
    	public static final String LAPIS_BRICK = "lapis_brick";
		public static final String SWORD_PEDESTAL = "sword_pedestal";
		public static final String SWORD_PEDESTAL_OOT = "sword_pedestal_oot";
		
		public static final String SWORD_PLACE_SOUND = "place_sword";
		
		public static final String FIRE_PLATE = "fire_plate";
		public static final String FIRE_SWORD = "flame_sword";
		public static final String WATER_PLATE = "water_plate";
		public static final String CLAY_BLUE = "clay_blue";
		public static final String BRICK_BLUE = "brick_blue";
    	
    }
    
    public static class PedestalShapes {
    	public static final Map<Direction, VoxelShape> BASE_SWORD_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
    		map.put(Direction.NORTH, Block.box(2,6,7.5,14,24.5,8.5));
    		map.put(Direction.EAST, Block.box(7.5,6,2,8.5,24.5,14));
    		map.put(Direction.SOUTH, Block.box(2,6,7.5,14,24.5,8.5));
    		map.put(Direction.WEST, Block.box(7.5,6,2,8.5,24.5,14));
    	});
    	
    	
    	public static final Map<Direction, VoxelShape> BASE_SIDES_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
    		map.put(Direction.NORTH, Block.box(2.0D, 0.0D, 5.0D, 14.0D, 6.0D, 11.0D));
    		map.put(Direction.EAST, Block.box(5.0D, 0.0D, 2.0D, 11.0D, 6.0D, 14.0D));
    		map.put(Direction.SOUTH, Block.box(2.0D, 0.0D, 5.0D, 14.0D, 6.0D, 11.0D));
    		map.put(Direction.WEST, Block.box(5.0D, 0.0D, 2.0D, 11.0D, 6.0D, 14.0D));
    	});
    	
    	public static final Map<Direction, VoxelShape> OOT_SWORD_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
    		map.put(Direction.NORTH, Block.box(2,4,7.5,14,24.5,8.5));
    		map.put(Direction.EAST, Block.box(7.5,4,2,8.5,24.5,14));
    		map.put(Direction.SOUTH, Block.box(2,4,7.5,14,24.5,8.5));
    		map.put(Direction.WEST, Block.box(7.5,4,2,8.5,24.5,14));
    	});
    	
    	
    	public static final Map<Direction, VoxelShape> OOT_SIDES_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
    		map.put(Direction.NORTH, Block.box(1.0D, 0.0D, 4.5D, 15.0D, 4.1D, 11.5D));
    		map.put(Direction.EAST, Block.box(4.5D, 0.0D, 1.0D, 11.5D, 4.1D, 15.0D));
    		map.put(Direction.SOUTH, Block.box(1.0D, 0.0D, 4.5D, 15.0D, 4.1D, 11.5D));
    		map.put(Direction.WEST, Block.box(4.5D, 0.0D, 1.0D, 11.5D, 4.1D, 15.0D));
    	});
    }
}
