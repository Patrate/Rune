package fr.emmuliette.rune.mod.blocks.scripting;

import java.util.function.Function;

import com.google.gson.JsonObject;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class SpellBindingRecipeSerializer<T extends SpellBindingRecipe>
		extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
	private final Function<ResourceLocation, T> constructor;

	public SpellBindingRecipeSerializer(Function<ResourceLocation, T> constructor) {
		this.constructor = constructor;
	}

	public T fromJson(ResourceLocation p_199425_1_, JsonObject p_199425_2_) {
		return this.constructor.apply(p_199425_1_);
	}

	public T fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {
		return this.constructor.apply(p_199426_1_);
	}

	public void toNetwork(PacketBuffer p_199427_1_, T p_199427_2_) {
	}

	public boolean isSpecial() {
		return true;
	}
}

/*
 * 
 * 
 * 
 * 
 * <T extends SpellBindingRecipe> extends
 * net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
 * implements IRecipeSerializer<T> {
 * 
 * private final SpellBindingRecipeSerializer.IFactory<T> factory;
 * 
 * public SpellBindingRecipeSerializer(SpellBindingRecipeSerializer.IFactory<T>
 * factory) { this.factory = factory; }
 * 
 * @Override
 * 
 * @Nonnull public T fromJson(@Nonnull ResourceLocation recipeId, @Nonnull
 * JsonObject json) { // group String groupString = JSONUtils.getAsString(json,
 * "group", "");
 * 
 * // ingredient JsonElement ingredientJSON = JSONUtils.isArrayNode(json,
 * "ingredient") ? JSONUtils.getAsJsonArray(json, "ingredient") :
 * JSONUtils.getAsJsonObject(json, "ingredient"); Ingredient ingredient =
 * Ingredient.fromJson(ingredientJSON);
 * 
 * // result ItemStack resultItemStack; if (!json.has("result")) {
 * resultItemStack = ItemStack.EMPTY; } else if
 * (json.get("result").isJsonObject()) { resultItemStack =
 * ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result")); } else
 * { String resultString = JSONUtils.getAsString(json, "result");
 * ResourceLocation resultRS = new ResourceLocation(resultString);
 * resultItemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(resultRS)); }
 * 
 * // cookTime int cookTime = JSONUtils.getAsInt(json, "furnaceTime", 200);
 * 
 * return this.factory.create(recipeId, groupString, ingredient,
 * resultItemStack, cookTime); }
 * 
 * @Nullable
 * 
 * @Override public T fromNetwork(@Nonnull ResourceLocation recipeId,
 * PacketBuffer buffer) { // group String groupString = buffer.readUtf(32767);
 * 
 * // ingredient Ingredient ingredient = Ingredient.fromNetwork(buffer);
 * 
 * // result ItemStack itemstack = buffer.readItem();
 * 
 * // cookTime int cookTime = buffer.readVarInt();
 * 
 * return this.factory.create(recipeId, groupString, ingredient, itemstack,
 * cookTime); }
 * 
 * @Override public void toNetwork(PacketBuffer buffer, T recipe) { // group
 * buffer.writeUtf(recipe.getGroup());
 * 
 * // ingredient recipe.ingredient.write(buffer);
 * 
 * // result buffer.writeItemStack(recipe.getResult()); }
 * 
 * public interface IFactory<T extends SpellBindingRecipe> { T
 * create(ResourceLocation resourceLocation, String group, Ingredient
 * ingredient, ItemStack result, int cookTime); } }
 */