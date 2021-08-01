package fr.emmuliette.rune.mod.blocks.spellBinding;

import fr.emmuliette.rune.setup.Registration;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public abstract class SpellBindingRecipe implements IRecipe<SpellBindingInventory> {	
	private final ResourceLocation id;
	
	public SpellBindingRecipe(ResourceLocation id) {
		this.id = id;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeType<?> getType() {
		return Registration.SPELLBINDING_RECIPE;
	}
	
	/*
	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<SpellBindingRecipe> {
		private static final ResourceLocation SPELLBINDING_RECIPE = new ResourceLocation(RuneMain.MOD_ID, "spellbinding_recipe");

		public SpellBindingRecipe fromJson(ResourceLocation p_199425_1_, JsonObject p_199425_2_) {
			String s = JSONUtils.getAsString(p_199425_2_, "group", "");
			Map<String, Ingredient> map = SpellBindingRecipe.keyFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "key"));
			String[] astring = SpellBindingRecipe
					.shrink(SpellBindingRecipe.patternFromJson(JSONUtils.getAsJsonArray(p_199425_2_, "pattern")));
			int i = astring[0].length();
			int j = astring.length;
			NonNullList<Ingredient> nonnulllist = SpellBindingRecipe.dissolvePattern(astring, map, i, j);
			ItemStack itemstack = SpellBindingRecipe.itemFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "result"));
			return new SpellBindingRecipe(p_199425_1_, s, i, j, nonnulllist, itemstack);
		}

		public SpellBindingRecipe fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {
			int i = p_199426_2_.readVarInt();
			int j = p_199426_2_.readVarInt();
			String s = p_199426_2_.readUtf(32767);
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

			for (int k = 0; k < nonnulllist.size(); ++k) {
				nonnulllist.set(k, Ingredient.fromNetwork(p_199426_2_));
			}

			ItemStack itemstack = p_199426_2_.readItem();
			return new SpellBindingRecipe(p_199426_1_, s, i, j, nonnulllist, itemstack);
		}

		public void toNetwork(PacketBuffer p_199427_1_, SpellBindingRecipe p_199427_2_) {
			p_199427_1_.writeVarInt(p_199427_2_.width);
			p_199427_1_.writeVarInt(p_199427_2_.height);
			p_199427_1_.writeUtf(p_199427_2_.group);

			for (Ingredient ingredient : p_199427_2_.recipeItems) {
				ingredient.toNetwork(p_199427_1_);
			}

			p_199427_1_.writeItem(p_199427_2_.result);
		}
	}*/
}
