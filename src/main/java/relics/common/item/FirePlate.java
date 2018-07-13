package relics.common.item;

import mysticwater.lib.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import relics.Relics;

public class FirePlate extends ItemRelics
{
	public FirePlate(String name)
	{
		super(name);
	}
	
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int i, boolean b)
	{
		EntityPlayer player = (EntityPlayer) entity;
		
		if(player.inventory.hasItemStack(new ItemStack(this)));
		{
			player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation(PotionTypes.FIRE_RESISTANCE.getRegistryName().toString()), 10, 1));
			
		}
	}
}
