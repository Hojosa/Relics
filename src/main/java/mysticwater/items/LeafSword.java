package mysticwater.items;

import mysticwater.lib.Strings;
import relics.Relics;
import relics.common.item.SwordRelic;

public class LeafSword extends SwordRelic
{
	public LeafSword(ToolMaterial material, String type)
	{
		super(material, type);
		this.setCreativeTab(Relics.getCreativTab());
		this.setUnlocalizedName(Strings.LeafSwordName);
	}

}
