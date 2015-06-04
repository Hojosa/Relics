package mysticwater.items;

import mysticwater.MysticWater;
import mysticwater.lib.Strings;

public class FlameSword extends SwordsMcGoodyBox
{
	public FlameSword(ToolMaterial material, String type)
	{
		super(material, type);
		this.setCreativeTab(MysticWater.getCreativTab());
		this.setUnlocalizedName(Strings.FlameSwordName);
		
	}
	
}
