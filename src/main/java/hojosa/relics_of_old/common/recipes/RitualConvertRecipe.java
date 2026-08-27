package hojosa.relics_of_old.common.recipes;

import java.util.List;

import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualConvertRecipe extends RitualRecipeBase {

	private final EntityType<?> sourceEntity; // entity that must be above the locus
	private final EntityType<?> resultEntity; // entity to convert into

	public RitualConvertRecipe(ResourceLocation id, List<RitualRecipeComponent> components, EntityType<?> sourceEntity, EntityType<?> resultEntity) {
		super(id, components, null); // no item/block focus
		this.sourceEntity = sourceEntity;
		this.resultEntity = resultEntity;
	}

	@Override
	public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
		if (caster == null || caster.level().isClientSide)
			return false;
		if (!matchesRitual(ingredients))
			return false;

		Level level = location.getLevel();
		// Find matching source entity above the locus
		List<Entity> entities = location.targetsInRitual(Entity.class);
		Entity source = null;
		for (Entity e : entities) {
			if (e.getType() == sourceEntity) {
				source = e;
				break;
			}
		}
		if (source == null)
			return false;

		// Spawn result entity at source's position
		Entity result = resultEntity.create(level);
		if (result == null)
			return false;
		result.moveTo(source.getX(), source.getY(), source.getZ(), source.getYRot(), source.getXRot());

		// Copy health if both are Mob
		if (source instanceof Mob sourceMob && result instanceof Mob resultMob) {
			resultMob.setHealth(sourceMob.getHealth());
		}

		source.discard();
		level.addFreshEntity(result);
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	public static class Serializer implements RecipeSerializer<RitualConvertRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("ritual_convert");

		private Serializer() {
		}

		@Override
		public RitualConvertRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			List<RitualRecipeComponent> components = parseComponents(GsonHelper.getAsJsonArray(json, "components"));
			// Focus object has "entity" for source type
			JsonObject focusObj = GsonHelper.getAsJsonObject(json, "focus");
			EntityType<?> source = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.parse(focusObj.get("entity").getAsString()));
			EntityType<?> result = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.parse(GsonHelper.getAsString(json, "result")));
			return new RitualConvertRecipe(recipeId, components, source, result);
		}

		@Override
		public RitualConvertRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
			List<RitualRecipeComponent> components = readComponents(buf);
			EntityType<?> source = ForgeRegistries.ENTITY_TYPES.getValue(buf.readResourceLocation());
			EntityType<?> result = ForgeRegistries.ENTITY_TYPES.getValue(buf.readResourceLocation());
			return new RitualConvertRecipe(recipeId, components, source, result);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, RitualConvertRecipe recipe) {
			writeComponents(buf, recipe.components);
			buf.writeResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(recipe.sourceEntity));
			buf.writeResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(recipe.resultEntity));
		}
	}
}