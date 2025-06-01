package hojosa.relics.lib;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonHelper {

    public static List<ICondition> deserializeConditions(JsonObject object, String memberName) {
        JsonArray conditions = GsonHelper.getAsJsonArray(object, memberName);
        List<ICondition> result = new ArrayList<>();
        for (JsonElement condition : conditions) {
            if (!condition.isJsonObject()) {
                throw new JsonSyntaxException("Conditions must be an array of JsonObjects");
            }

            result.add(CraftingHelper.getCondition(condition.getAsJsonObject()));
        }
        return result;
    }

    public static JsonElement serializeConditions(List<ICondition> conditions) {
        JsonArray result = new JsonArray();
        for (ICondition condition : conditions) {
            result.add(CraftingHelper.serialize(condition));
        }
        return result;
    }
}