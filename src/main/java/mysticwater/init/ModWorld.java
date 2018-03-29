package mysticwater.init;

import mysticwater.lib.References;
import mysticwater.world.dimension.WorldProviderMysticLands;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class ModWorld
{

	public static void init()
	{
		
	}
	
	public static final int mysticWaterDimensionID = 3;
	public static final DimensionType MYSTIC_LANDS = DimensionType.register("Mystic Lands", "_ "+ References.MODNAME, 3, WorldProviderMysticLands.class, false);

	
	public static void registerDimension()
	{
		DimensionManager.registerDimension(3, MYSTIC_LANDS);
		
	}
}
