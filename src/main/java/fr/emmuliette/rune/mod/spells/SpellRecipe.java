package fr.emmuliette.rune.mod.spells;

import java.util.List;

import com.google.common.collect.Lists;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingInventory;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingRecipe;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.items.SpellItem;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.item.BookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SpellRecipe extends SpellBindingRecipe {//implements IRecipe<SpellBindingInventory> {
	/*public SpellRecipe(ResourceLocation id, String name, int p_i48162_3_, int p_i48162_4_,
			NonNullList<Ingredient> ingredients, ItemStack result) {
		super(id, name, p_i48162_3_, p_i48162_4_, ingredients, result);
		this.id = id;
	}*/

	
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

					// Comparer à un papier et un socket
				} else if (itemstack.getItem().getItem() == Items.PAPER) {
					if (hasPaper || hasBook || hasSocket) {
						return false;
					}
					hasPaper = true;
					/*
					 * } else if(itemstack.getItem() instanceof SocketItem) { if(hasPaper || hasBook
					 * || hasSocket) { return false; } hasSocket = true;
					 */
				} else if (!(itemstack.getItem() instanceof RuneItem)) {
					return false;
				}

				list.add(itemstack);
			}
		}
		return list.size() >= 2 && (hasPaper ^ hasBook ^ hasSocket);
	}

	public ItemStack assemble(SpellBindingInventory spellBindingInventory) {
		List<RuneItem> list = Lists.newArrayList();
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
					// Comparer à un papier et un socket
				} else if (itemstack.getItem().getItem() == Items.PAPER) {
					if (hasPaper || hasBook || hasSocket) {
						return ItemStack.EMPTY;
					}
					hasPaper = true;
					/*
					 * } else if(itemstack.getItem() instanceof SocketItem) { if(hasPaper || hasBook
					 * || hasSocket) { return ItemStack.EMPTY; } hasSocket = true;
					 */
				} else if (!(item instanceof RuneItem)) {
					return ItemStack.EMPTY;
				} else {
					list.add((RuneItem) item);
				}
			}
		}

		try {
			if ((hasPaper ^ hasBook ^ hasSocket) && SpellBuilder.parseSpell(list)) {
				Spell spell = SpellBuilder.runeToSpell(spellBindingInventory.getSpellName(), list);
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
