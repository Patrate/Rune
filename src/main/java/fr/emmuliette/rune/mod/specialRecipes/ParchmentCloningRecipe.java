package fr.emmuliette.rune.mod.specialRecipes;

import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParchmentCloningRecipe extends SpecialRecipe {
	public ParchmentCloningRecipe(ResourceLocation rl) {
		super(rl);
	}

	public boolean matches(CraftingInventory p_77569_1_, World p_77569_2_) {
		ItemStack parchment = ItemStack.EMPTY;
		boolean hasPaper = false;
		int manaCount = 0;

		for (int j = 0; j < p_77569_1_.getContainerSize(); ++j) {
			ItemStack itemstack1 = p_77569_1_.getItem(j);
			if (!itemstack1.isEmpty()) {
				if (itemstack1.getItem() == ModItems.PARCHMENT.get()) {
					if (!parchment.isEmpty()) {
						return false;
					}

					parchment = itemstack1;
				} else if (itemstack1.getItem() == Items.PAPER) {
					if (hasPaper)
						return false;
					hasPaper = true;
				} else if (itemstack1.getItem() == ModItems.MANA_ORE.get()) {
					manaCount += itemstack1.getCount();
				}
			}
		}

		return hasPaper && !parchment.isEmpty() && manaCount > 1;
	}

	public ItemStack assemble(CraftingInventory p_77572_1_) {
		ItemStack parchment = ItemStack.EMPTY;
		boolean hasPaper = false;
		int manaCount = 0;

		for (int j = 0; j < p_77572_1_.getContainerSize(); ++j) {
			ItemStack itemstack1 = p_77572_1_.getItem(j);
			if (!itemstack1.isEmpty()) {
				if (itemstack1.getItem() == ModItems.PARCHMENT.get()) {
					if (!parchment.isEmpty()) {
						return ItemStack.EMPTY;
					}

					parchment = itemstack1;
				} else if (itemstack1.getItem() == Items.PAPER) {
					if (hasPaper)
						return ItemStack.EMPTY;
					hasPaper = true;
				} else if (itemstack1.getItem() == ModItems.MANA_ORE.get()) {
					manaCount += itemstack1.getCount();
				}
			}
		}

		if (hasPaper && !parchment.isEmpty() && manaCount > 1) {
			ItemStack itemstack2 = parchment.copy();
			itemstack2.setCount(2);
			return itemstack2;
		} else {
			return ItemStack.EMPTY;
		}
	}

	public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
		return p_194133_1_ >= 3 && p_194133_2_ >= 3;
	}

	public IRecipeSerializer<?> getSerializer() {
		return Registration.PARCHMENT_CLONING_RECIPE.get();
	}
}