package fr.emmuliette.rune.mod.specialRecipes;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingRecipe;
import fr.emmuliette.rune.mod.gui.spellbinding.ErrorIcon.SpellError;
import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingInventory;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.items.spellItems.SpellItem;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.SpellBuilder;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.item.BookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SpellRecipe extends SpellBindingRecipe {// implements IRecipe<SpellBindingInventory> {

	public SpellRecipe(ResourceLocation id) {
		super(id);
	}

	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public IRecipeType<?> getType() {
		return Registration.SPELLBINDING_RECIPE;
	}

	public static List<SpellError> validate(SpellBindingInventory spellBindingInventory) {
		List<AbstractSpellComponent> list = Lists.newArrayList();
		List<SpellError> retour = new ArrayList<SpellError>();
		boolean hasPaper = false, hasBook = false, hasSocket = false;
		
		boolean hasMultiple = false;

		for (int i = 0; i < spellBindingInventory.getContainerSize(); ++i) {
			ItemStack itemstack = spellBindingInventory.getItem(i);
			if (!itemstack.isEmpty()) {
				Item item = itemstack.getItem();
				if (itemstack.getItem() instanceof BookItem) {
					if (hasPaper || hasBook || hasSocket) {
						hasMultiple = true;
					}
					hasBook = true;
					continue;
					// Comparer � un papier et un socket
				} else if (itemstack.getItem().getItem() == Items.PAPER) {
					if (hasPaper || hasBook || hasSocket) {
						hasMultiple = true;
					}
					hasPaper = true;
				} else if (itemstack.getItem() == ModItems.EMPTY_SOCKET.get()) {
					if (hasPaper || hasBook || hasSocket) {
						hasMultiple = true;
					}
					hasSocket = true;
				} else if (!(item instanceof RuneItem)) {
					retour.add(SpellError.NOT_A_RUNE);
				} else {
					list.add(spellBindingInventory.getComponent(i));
				}
			}
		}
		if (!hasPaper && !hasBook && !hasSocket) {
			retour.add(SpellError.MISSING_PAPER);
		} else if(hasMultiple) {
			retour.add(SpellError.TOO_MUCH_PAPER);
		}
		
		List<SpellError> errorMessage = SpellBuilder.parseSpellComponents(list, hasSocket);
		if (!errorMessage.isEmpty()) {
			retour.addAll(errorMessage);
		} else {
			try {
				Spell spell = SpellBuilder.buildSpell(spellBindingInventory.getSpellName(), list);
				if (spell == null) {
					retour.add(SpellError.GENERIC);
				}
			} catch (RunePropertiesException e) {
				e.printStackTrace();
				retour.add(SpellError.EXCEPTION);
			}
		}
		return retour;
	}

	public boolean matches(SpellBindingInventory spellBindingInventory, World world) {
		List<ItemStack> list = Lists.newArrayList();
		boolean hasPaper = false, hasBook = false, hasSocket = false;

		for (int i = 0; i < spellBindingInventory.getContainerSize(); ++i) {
			ItemStack itemstack = spellBindingInventory.getItem(i);
			if (!itemstack.isEmpty()) {
				if (itemstack.getItem() instanceof BookItem) {
					if (hasPaper || hasBook || hasSocket) {
						return false;
					}
					hasBook = true;

					// Comparer � un papier et un socket
				} else if (itemstack.getItem().getItem() == Items.PAPER) {
					if (hasPaper || hasBook || hasSocket) {
						return false;
					}
					hasPaper = true;

				} else if (itemstack.getItem() == ModItems.EMPTY_SOCKET.get()) {
					if (hasPaper || hasBook || hasSocket) {
						return false;
					}
					hasSocket = true;
				} else if (!(itemstack.getItem() instanceof RuneItem)) {
					return false;
				}

				list.add(itemstack);
			}
		}
		return list.size() >= 2 && (hasPaper ^ hasBook ^ hasSocket);
	}

	@Override
	public ItemStack assemble(SpellBindingInventory spellBindingInventory) {
		List<AbstractSpellComponent> list = Lists.newArrayList();
		boolean hasPaper = false, hasBook = false, hasSocket = false;

		for (int i = 0; i < spellBindingInventory.getContainerSize(); ++i) {
			ItemStack itemstack = spellBindingInventory.getItem(i);
			if (!itemstack.isEmpty()) {
				Item item = itemstack.getItem();
				if (itemstack.getItem() instanceof BookItem) {
					if (hasPaper || hasBook || hasSocket) {
						return ItemStack.EMPTY;
					}
					hasBook = true;
					continue;
					// Comparer � un papier et un socket
				} else if (itemstack.getItem().getItem() == Items.PAPER) {
					if (hasPaper || hasBook || hasSocket) {
						return ItemStack.EMPTY;
					}
					hasPaper = true;
				} else if (itemstack.getItem() == ModItems.EMPTY_SOCKET.get()) {
					if (hasPaper || hasBook || hasSocket) {
						return ItemStack.EMPTY;
					}
					hasSocket = true;
				} else if (!(item instanceof RuneItem)) {
					return ItemStack.EMPTY;
				} else {
					list.add(spellBindingInventory.getComponent(i));
				}
			}
		}

		try {
			if ((hasPaper ^ hasBook ^ hasSocket) && SpellBuilder.parseSpellComponents(list, hasSocket).isEmpty()) {
				Spell spell = SpellBuilder.buildSpell(spellBindingInventory.getSpellName(), list);
				if (spell == null) {
					System.out.println("Error! buildSpell returned null");
					return ItemStack.EMPTY;
				}
				if (hasPaper)
					return SpellItem.buildSpellItem(spell, SpellItem.ItemType.PARCHMENT);
				if (hasBook)
					return SpellItem.buildSpellItem(spell, SpellItem.ItemType.GRIMOIRE);
				if (hasSocket)
					return SpellItem.buildSpellItem(spell, SpellItem.ItemType.SOCKET);
			}
			return ItemStack.EMPTY;
		} catch (RunePropertiesException e) {
			e.printStackTrace();
			return ItemStack.EMPTY;
		}
	}

	public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
		return gridWidth * gridHeight >= 2;
	}

	public IRecipeSerializer<?> getSerializer() {
		return Registration.SPELL_RECIPE.get();
	}
}
