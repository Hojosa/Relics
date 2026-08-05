package hojosa.relics_of_old.lib.item;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public abstract class RelicsHeadband extends ArmorItem {
	
	public RelicsHeadband(ArmorMaterial material) {
        super(material, Type.HELMET, new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public static boolean isNaked(net.minecraft.world.entity.player.Player player) {
        return player.getArmorValue() == 0;
    }

    public abstract int getBonus(int amount);
}