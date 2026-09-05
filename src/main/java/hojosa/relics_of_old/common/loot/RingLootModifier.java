package hojosa.relics_of_old.common.loot;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.item.MagicRingItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class RingLootModifier extends LootModifier {
	public static final Supplier<Codec<RingLootModifier>> CODEC = Suppliers
			.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec().fieldOf("source_item").forGetter(m -> m.sourceItem))
					.and(ForgeRegistries.ITEMS.getCodec().fieldOf("result_item").forGetter(m -> m.resultItem)).and(ForgeRegistries.ITEMS.getCodec().fieldOf("ring").forGetter(m -> m.ring))
					.and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)).and(Codec.FLOAT.fieldOf("resonance_chance").forGetter(m -> m.resonanceChance)).apply(inst, RingLootModifier::new)));

	private final Item sourceItem;
	private final Item resultItem;
	private final Item ring;
	private final float chance;
	private final float resonanceChance;

	public RingLootModifier(LootItemCondition[] conditions, Item sourceItem, Item resultItem, Item ring, float chance, float resonanceChance) {
		super(conditions);
		this.sourceItem = sourceItem;
		this.resultItem = resultItem;
		this.ring = ring;
		this.chance = chance;
		this.resonanceChance = resonanceChance;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		if (!(entity instanceof Player player))
			return generatedLoot;
		if (!(ring instanceof MagicRingItem ringItem) || !ringItem.isEquipped(player))
			return generatedLoot;

		float activeChance = RelicsItems.RESONANCE_RING.get().isEquipped(player) ? resonanceChance : chance;
		if (player.getRandom().nextFloat() >= activeChance)
			return generatedLoot;

		// replace one source item with result item
		for (int i = 0; i < generatedLoot.size(); i++) {
			if (generatedLoot.get(i).is(sourceItem)) {
				generatedLoot.set(i, new ItemStack(resultItem, generatedLoot.get(i).getCount()));
				break;
			}
		}
		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}