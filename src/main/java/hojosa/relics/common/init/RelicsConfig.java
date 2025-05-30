package hojosa.relics.common.init;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsConfig {

    public static class Common {
        public final IntValue emeraldChance;
        public final BooleanValue doEmeraldShardsDropFromMobs;
        public final BooleanValue doHeartsDropFromMobs;
        public final IntValue heartChance;
        public final BooleanValue test;
        
        private static final String CATEGORY_ITEM_DROPS = "loot";
        private static final String CATEGORY_ITEM_DROPS_MOBS = "from_mobs";
        private static final String CATEGORY_ITEM_DROPS_BLOCKS = "from_blocks";
        
        Common(ForgeConfigSpec.Builder  builder) {
        	builder.comment("loot configuration").push(CATEGORY_ITEM_DROPS);
        	
        	
        	builder.comment("loot drops from hostile mob kills").push(CATEGORY_ITEM_DROPS_MOBS);
        	{
	        	doEmeraldShardsDropFromMobs = builder
	        			.comment("can emerald shards drop. Default true")
	        			.define("doEmeraldShardsDropFromMobs", true);
	        	
	        	emeraldChance = builder
	        			.comment("chance that a emerald shard is dropped. Default: 1 in 5 (20%)")
	        			.defineInRange("emeraldShardChance", 5, 1, 100);
	        	
	        	doHeartsDropFromMobs = builder
	        			.comment("can hearts drop. Default true")
	        			.define("doHeartsDropFromMobs", true);
	        	
	        	heartChance = builder
	        			.comment("chance that a heart is dropped. Default: 1 in 10 (10%)")
	        			.defineInRange("heartChance", 10, 1, 100);
        	}
        	builder.pop();
        	
        	builder.comment("loot drops from block can be customized via datapack. Config settings for this may come at a later point").push(CATEGORY_ITEM_DROPS_BLOCKS);
        	test= builder.comment("test").define("test", false);
        	builder.pop(2);
        }
        
        
        
        
    }
    
    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
      final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
      commonSpec = specPair.getRight();
      COMMON = specPair.getLeft();
    }
    
    /** Registers any relevant listeners for config */
    public static void init() {
      ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RelicsConfig.commonSpec);
    }
}