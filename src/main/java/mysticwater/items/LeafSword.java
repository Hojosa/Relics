package mysticwater.items;

import mysticwater.MysticWater;
import mysticwater.lib.Strings;

public class LeafSword extends SwordsMcGoodyBox
{
	public LeafSword(ToolMaterial material, String type)
	{
		super(material, type);
		this.setCreativeTab(MysticWater.getCreativTab());
		this.setUnlocalizedName(Strings.LeafSwordName);
	}

}
