package hojosa.relics.common.loot;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class RelicsGlobalLootModifier extends LootModifier {

    private final Item item;

    public RelicsGlobalLootModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(item, 1));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<RelicsGlobalLootModifier> {
    	
    	public static final Serializer INSTANCE = new Serializer();

        @Override
        public RelicsGlobalLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] lootConditions) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation((GsonHelper.getAsString(object, "item"))));
            return new RelicsGlobalLootModifier(lootConditions, item);
        }

        @Override
        public JsonObject write(RelicsGlobalLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(instance.item).toString());
            return json;
        }
    }
	
}
