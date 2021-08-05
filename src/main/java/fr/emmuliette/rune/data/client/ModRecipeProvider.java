package fr.emmuliette.rune.data.client;

import java.util.function.Consumer;

import fr.emmuliette.rune.mod.blocks.ModBlocks;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingRecipeBuilder;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;

public class ModRecipeProvider extends RecipeProvider {
	public ModRecipeProvider(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModBlocks.CASTER_BLOCK.getBlock()).define('A', ModItems.BLANK_RUNE.getItem())
				.define('B', ItemTags.LOGS).pattern("BBB").pattern("BAB").pattern("BBB")
				.unlockedBy("has_item", has(ModItems.BLANK_RUNE.getItem())).unlockedBy("has_log", has(ItemTags.LOGS))
				.save(consumer);
		;

		ShapelessRecipeBuilder.shapeless(ModItems.BLANK_RUNE.getItem()).requires(Items.FLINT).requires(Items.STONE)
				.unlockedBy("has_flint", has(Items.FLINT)).unlockedBy("has_stone", has(Items.STONE)).save(consumer);

		SpellBindingRecipeBuilder.build(Registration.SPELL_RECIPE.get()).save(consumer, "spellbinding_spell_recipe");
		// CustomRecipeBuilder.special(Registration.CRAFTING_SPELL_RECIPE.get()).save(consumer,
		// "crafting_spell_recipe");
	}
}