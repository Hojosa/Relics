package mysticwater.world;


	import hojosa.relics.lib.References;
import mysticwater.lib.Strings;
import net.minecraft.block.BlockFire;
	
	
	public class DimBlockFire extends BlockFire
	{
		public DimBlockFire()
		{
			this.setUnlocalizedName(Strings.DimFire);
			
		}
		
		public String getUnwrappedUnlocalizedName(String unlocalizedName)
		{
			return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);

		}

		@Override
		public String getUnlocalizedName()
		{
			return String.format("%s%s", References.RESOURCESPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		}
	}
