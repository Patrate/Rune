package fr.emmuliette.rune.data.client;

import java.util.function.Consumer;

import fr.emmuliette.rune.setup.ModBlocks;
import fr.emmuliette.rune.setup.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(DataGenerator p_i48262_1_) {
		super(p_i48262_1_);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModBlocks.CASTER_BLOCK.get()).define('A', ModItems.BLANK_RUNE.get())
				.define('B', ItemTags.LOGS).pattern("BBB").pattern("BAB")
				.pattern("BBB").unlockedBy("has_item", has(ModItems.BLANK_RUNE.get()))
				.unlockedBy("has_log", has(ItemTags.LOGS)).save(consumer);
		;

		ShapelessRecipeBuilder.shapeless(ModItems.BLANK_RUNE.get()).requires(Items.FLINT).requires(Items.STONE)
				.unlockedBy("has_flint", has(Items.FLINT)).unlockedBy("has_stone", has(Items.STONE)).save(consumer);
	}
}