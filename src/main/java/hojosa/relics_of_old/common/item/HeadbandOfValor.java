package hojosa.relics_of_old.common.item;

import org.jetbrains.annotations.Nullable;

import hojosa.relics_of_old.lib.RelicsUtil;
import hojosa.relics_of_old.lib.item.RelicsHeadband;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class HeadbandOfValor extends RelicsHeadband {

	public HeadbandOfValor(ArmorMaterial material) {
		super(material);
	}

	@Override
	public int getBonus(int amount) {
		return amount + 3 + amount / 4;
	}
	
    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
    	ResourceLocation key = ForgeRegistries.ITEMS.getKey(this);
    	return RelicsUtil.modLocArmor(key.getPath());
    }
}