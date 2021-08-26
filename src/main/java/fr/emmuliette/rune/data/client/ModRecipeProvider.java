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
import net.minecraft.data.SmithingRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.registry.Registry;

public class ModRecipeProvider extends RecipeProvider {
	public ModRecipeProvider(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModBlocks.CASTER_BLOCK.get()).define('A', ModItems.BLANK_RUNE.get())
				.define('B', ItemTags.LOGS).pattern("BBB").pattern("BAB").pattern("BBB")
				.unlockedBy("has_item", has(ModItems.BLANK_RUNE.get())).unlockedBy("has_log", has(ItemTags.LOGS))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(ModItems.BLANK_RUNE.get()).requires(Items.FLINT).requires(Items.STONE)
				.unlockedBy("has_flint", has(Items.FLINT)).unlockedBy("has_stone", has(Items.STONE)).save(consumer);

		SpellBindingRecipeBuilder.build(Registration.SPELL_RECIPE.get()).save(consumer, "spellbinding_spell_recipe");

		ShapedRecipeBuilder.shaped(ModItems.WOODEN_WAND.get()).define('#', Items.STICK).define('X', ItemTags.PLANKS)
				.pattern("#").pattern("X").pattern("#").unlockedBy("has_stick", has(Items.STICK)).save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.STONE_WAND.get()).define('#', Items.STICK)
				.define('X', ItemTags.STONE_TOOL_MATERIALS).pattern("#").pattern("X").pattern("#")
				.unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS)).save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.IRON_WAND.get()).define('#', Items.STICK).define('X', Items.IRON_INGOT)
				.pattern("#").pattern("X").pattern("#").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.GOLDEN_WAND.get()).define('#', Items.STICK)
				.define('X', Items.GOLD_INGOT).pattern("#").pattern("X").pattern("#")
				.unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT)).save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.DIAMOND_WAND.get()).define('#', Items.STICK).define('X', Items.DIAMOND)
				.pattern("#").pattern("X").pattern("#").unlockedBy("has_diamond", has(Items.DIAMOND)).save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.NETHERITE_WAND.get()).define('#', Items.STICK)
				.define('X', Items.NETHERITE_INGOT).pattern("#").pattern("X").pattern("#")
				.unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(consumer);

		netheriteSmithing(consumer, ModItems.DIAMOND_WAND.get(), ModItems.NETHERITE_WAND.get());

	}

	@SuppressWarnings("deprecation")
	private static void netheriteSmithing(Consumer<IFinishedRecipe> p_240469_0_, Item p_240469_1_, Item p_240469_2_) {
		SmithingRecipeBuilder.smithing(Ingredient.of(p_240469_1_), Ingredient.of(Items.NETHERITE_INGOT), p_240469_2_)
				.unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
				.save(p_240469_0_, Registry.ITEM.getKey(p_240469_2_.asItem()).getPath() + "_smithing");
	}
}