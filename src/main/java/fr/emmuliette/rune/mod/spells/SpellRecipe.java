package fr.emmuliette.rune.mod.spells;

import java.util.List;

import com.google.common.collect.Lists;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.items.SpellItem;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SpellRecipe extends SpecialRecipe {
	public SpellRecipe(ResourceLocation id) {
		super(id);
	}

	public boolean matches(CraftingInventory craftingInventory, World world) {
		List<ItemStack> list = Lists.newArrayList();

		for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
			ItemStack itemstack = craftingInventory.getItem(i);
			if (!itemstack.isEmpty()) {
				if (!(itemstack.getItem() instanceof RuneItem)) {
					return false;
				}

				list.add(itemstack);
			}
		}

		return list.size() >= 2;
	}

	public ItemStack assemble(CraftingInventory craftingInventory) {
		System.out.println("Assembling a spell");
		List<RuneItem> list = Lists.newArrayList();

		for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
			ItemStack itemstack1 = craftingInventory.getItem(i);
			if (!itemstack1.isEmpty()) {
				Item item = itemstack1.getItem();
				if (!(item instanceof RuneItem)) {
					return ItemStack.EMPTY;
				}

				list.add((RuneItem) item);
			}
		}

		try {
			return validateComponents(list) ? SpellItem.buildSpellItem(Spell.buildSpell("test", list)) : ItemStack.EMPTY;
		} catch (RunePropertiesException e) {
			e.printStackTrace();
			return ItemStack.EMPTY;
		}
	}

	private boolean validateComponents(List<RuneItem> list) {
		System.out.println("\nValidating components");
		if (list.size() < 2) {
			return false;
		}
		System.out.println(list.get(0) + " " + list.get(0).getComponentClass().isAssignableFrom(AbstractCastComponent.class));
		if (!AbstractCastComponent.class.isAssignableFrom(list.get(0).getComponentClass())) {
			System.out.println(list.get(0) + " is not valid for AbstractCastComponent");
			return false;
		}
		for (int i = 1; i < list.size(); i++) { 
			System.out.println(list.get(i));
			if (!AbstractEffectComponent.class.isAssignableFrom(list.get(i).getComponentClass())) {
				System.out.println(list.get(i) + " is not valid for AbstractEffectComponent");
				return false;
			}
		}
		System.out.println("Valid");
		return true;
	}

	public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
		return gridWidth * gridHeight >= 2;
	}

	public IRecipeSerializer<?> getSerializer() {
		return Registration.SPELL_RECIPE.get();
	}
}
