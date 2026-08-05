package hojosa.relics_of_old.lib.item;

import java.util.EnumMap;
import java.util.function.Supplier;

import hojosa.relics_of_old.lib.References;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public enum RelicsArmorMaterials implements ArmorMaterial {

	WHIRLWIND("whirlwind", 462, Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
		m.put(ArmorItem.Type.BOOTS, 0);
		m.put(ArmorItem.Type.LEGGINGS, 0);
		m.put(ArmorItem.Type.CHESTPLATE, 0);
		m.put(ArmorItem.Type.HELMET, 0);
	}), 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.EMPTY),
	
	HEADBAND("headband", 14, Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
	      m.put(ArmorItem.Type.BOOTS, 0);
	      m.put(ArmorItem.Type.LEGGINGS, 0);
	      m.put(ArmorItem.Type.CHESTPLATE, 0);
	      m.put(ArmorItem.Type.HELMET, 0);
	  }), 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.EMPTY);

	private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
		m.put(ArmorItem.Type.BOOTS, 13);
		m.put(ArmorItem.Type.LEGGINGS, 15);
		m.put(ArmorItem.Type.CHESTPLATE, 16);
		m.put(ArmorItem.Type.HELMET, 11);
	});

	private final String name;
	private final int durabilityMultiplier;
	private final EnumMap<ArmorItem.Type, Integer> protection;
	private final int enchantmentValue;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	RelicsArmorMaterials(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protection, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance,
			Supplier<Ingredient> repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protection = protection;
		this.enchantmentValue = enchantmentValue;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
	}

	@Override
	public int getDurabilityForType(ArmorItem.Type type) {
		return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type type) {
		return this.protection.get(type);
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.equipSound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@Override
	public String getName() {
		return References.MOD_ID + ":" + this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}
