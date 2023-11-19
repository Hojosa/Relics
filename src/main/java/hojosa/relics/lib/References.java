package hojosa.relics.lib;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class References 

{
	public static final String MODID = "relics";
//	public static final String MODNAME = "Mystic Water";
	public static final String VERSION = "0.3";
	public static String CREATIVE_TAB = "Relics";
//	public static final String CLIENTPROXYLOCATION = MODID + ".core.proxy.ClientProxy";
//	public static final String COMMONPROXYLOCATION = MODID + ".core.proxy.CommonProxy";
//	public static final String RESOURCESPREFIX = MODID.toLowerCase() + ":";
	
    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(CREATIVE_TAB) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.ACACIA_DOOR);
        }
    };

    public static class UnlocalizedName {
    	public static final String LAPIS_BRICK = "lapis_brick";
		public static final String SWORD_PEDESTAL = "sword_pedestal";
		public static final String SWORD_PEDESTAL_OOT = "sword_pedestal_oot";
    	
    }
}
