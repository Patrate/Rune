package fr.emmuliette.rune.mod.specialRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.setup.ModTags;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RuneRecipe extends SpecialRecipe {
	private static final Map<RuneItem, List<Item>> runeIngredients = new HashMap<RuneItem, List<Item>>();
	private static final Comparator<Item> itemComparator = new Comparator<Item>() {
		@Override
		public int compare(Item o1, Item o2) {
			return o1.getRegistryName().compareTo(o2.getRegistryName());
		}
	};

	public RuneRecipe(ResourceLocation rl) {
		super(rl);
	}

	public static void register() {
		RuneRecipe.runeRecipe(ModItems.PROJECTILE_RUNE.get(), Items.ARROW);
		RuneRecipe.runeRecipe(ModItems.TOUCH_RUNE.get(), Items.STICK, Items.IRON_INGOT);
		RuneRecipe.runeRecipe(ModItems.DAMAGE_RUNE.get(), Items.DIAMOND, Items.DIAMOND);
	}

	private static void runeRecipe(Item rune, Item... ingredients) {
		if (!(rune instanceof RuneItem))
			RuneMain.LOGGER.error("Item " + rune.getRegistryName() + " isn't a RuneItem");
		else
			runeRecipe((RuneItem) rune, ingredients);
	}

	private static void runeRecipe(RuneItem rune, Item... ingredients) {
		if (runeIngredients.containsKey(rune)) {
			RuneMain.LOGGER.error("RuneRecipe for " + rune.getRegistryName() + " is already registered once");
		}
		List<Item> ingredientsList = Arrays.asList(ingredients);
		ingredientsList.sort(itemComparator);

		for (RuneItem runeItem : runeIngredients.keySet()) {
			if (runeIngredients.get(runeItem).equals(ingredientsList)) {
				RuneMain.LOGGER
						.error("RuneRecipe with ingredients " + runeIngredients.get(runeItem) + " is registered with "
								+ runeItem.getRegistryName() + ", can't register with " + rune.getRegistryName());
			}
		}
		runeIngredients.put(rune, ingredientsList);
	}

	public boolean matches(CraftingInventory inventory, World world) {
		List<Item> ingredients = new ArrayList<Item>();
		boolean hasRune = false, hasIngredient = false;

		for (int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack itemstack = inventory.getItem(i);
			if (!itemstack.isEmpty()) {
				if (ModTags.Items.BLANK_RUNE.contains(itemstack.getItem())) {
					if (hasRune) { // Rune already present
						return false;
					}
					hasRune = true;
				} else {
					ingredients.add(itemstack.getItem());
				}
			}
		}
		if (!hasRune)
			return false;

		ingredients.sort(itemComparator);

		for (RuneItem rune : runeIngredients.keySet()) {
			if (runeIngredients.get(rune).equals(ingredients)) {
				if (hasIngredient)
					return false;
				hasIngredient = true;
			}
		}

		return hasIngredient && hasRune;
	}

	@Override
	public ItemStack assemble(CraftingInventory inventory) {
		List<Item> ingredients = new ArrayList<Item>();
		boolean hasRune = false, hasIngredient = false;
		Grade grade = Grade.UNKNOWN;
		RuneItem rune = null;

		for (int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack itemstack = inventory.getItem(i);
			if (!itemstack.isEmpty()) {
				if (ModTags.Items.BLANK_RUNE.contains(itemstack.getItem())) {
					if (hasRune) { // Rune already present
						return ItemStack.EMPTY;
					}
					hasRune = true;

					if (itemstack.getItem() == ModItems.BLANK_WOODEN_RUNE.get()) {
						grade = Grade.WOOD;
					} else if (itemstack.getItem() == ModItems.BLANK_STONE_RUNE.get()) {
						grade = Grade.STONE;
					} else if (itemstack.getItem() == ModItems.BLANK_IRON_RUNE.get()) {
						grade = Grade.IRON;
					} else if (itemstack.getItem() == ModItems.BLANK_GOLDEN_RUNE.get()) {
						grade = Grade.GOLD;
					} else if (itemstack.getItem() == ModItems.BLANK_DIAMOND_RUNE.get()) {
						grade = Grade.DIAMOND;
					} else if (itemstack.getItem() == ModItems.BLANK_NETHERITE_RUNE.get()) {
						grade = Grade.NETHERITE;
					}
				} else {
					ingredients.add(itemstack.getItem());
				}
			}
		}
		if (!hasRune)
			return ItemStack.EMPTY;

		ingredients.sort(itemComparator);

		for (RuneItem runeTmp : runeIngredients.keySet()) {
			if (runeIngredients.get(runeTmp).equals(ingredients)) {
				if (hasIngredient)
					return ItemStack.EMPTY;
				hasIngredient = true;
				rune = runeTmp;
			}
		}
		if (!hasIngredient)
			return ItemStack.EMPTY;

		ItemStack retour = rune.getDefaultInstance();
		RuneItem.setGrade(retour, grade);
		return retour;
	}

	public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
		return gridWidth * gridHeight >= 2;
	}

	public IRecipeSerializer<?> getSerializer() {
		return Registration.RUNE_RECIPE.get();
	}
}
