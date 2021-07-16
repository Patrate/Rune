package fr.emmuliette.rune.mod.spells;

import java.util.List;

import com.google.common.collect.Lists;

import fr.emmuliette.rune.RandomNameUtils;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.items.SpellItem;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
		boolean hasPaper = false, hasBook = false, hasSocket = false;
		
		
		for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
			ItemStack itemstack = craftingInventory.getItem(i);
			if (!itemstack.isEmpty()) {
				if(itemstack.getItem() instanceof BookItem) {
					if(hasPaper || hasBook || hasSocket) {
						return false;
					}
					hasBook = true;
				
				 //Comparer à un papier et un socket
				 } else if(itemstack.getItem().getItem() == Items.PAPER) {
					if(hasPaper || hasBook || hasSocket) {
						return false;
					}
					hasPaper= true;
				/*} else if(itemstack.getItem() instanceof SocketItem) {
					if(hasPaper || hasBook || hasSocket) {
						return false;
					}
					hasSocket = true;*/
				} else if (!(itemstack.getItem() instanceof RuneItem)) {
					return false;
				}

				list.add(itemstack);
			}
		}

		return list.size() >= 2 && (hasPaper ^ hasBook ^ hasSocket);
	}

	public ItemStack assemble(CraftingInventory craftingInventory) {
		List<RuneItem> list = Lists.newArrayList();
		boolean hasPaper = false, hasBook = false, hasSocket = false;

		for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
			ItemStack itemstack = craftingInventory.getItem(i);
			if (!itemstack.isEmpty()) {
				Item item = itemstack.getItem();
				if(itemstack.getItem() instanceof BookItem) {
					if(hasPaper || hasBook || hasSocket) {
						return ItemStack.EMPTY;
					}
					hasBook = true;
					continue;
				 //Comparer à un papier et un socket
				 } else if(itemstack.getItem().getItem() == Items.PAPER) {
					if(hasPaper || hasBook || hasSocket) {
						return ItemStack.EMPTY;
					}
					hasPaper= true;
				/*} else if(itemstack.getItem() instanceof SocketItem) {
					if(hasPaper || hasBook || hasSocket) {
						return ItemStack.EMPTY;
					}
					hasSocket = true;*/
				} else if (!(item instanceof RuneItem)) {
					return ItemStack.EMPTY;
				} else {
					list.add((RuneItem) item);
				}
			}
		}

		try {
			if((hasPaper ^ hasBook ^ hasSocket) && Spell.parseSpell(list)) {
				Spell spell = Spell.buildSpell(RandomNameUtils.getName(), list);
				if(hasPaper)
					return SpellItem.buildSpellItem(spell, SpellItem.ItemType.PARCHMENT);
				if(hasBook)
					return SpellItem.buildSpellItem(spell, SpellItem.ItemType.GRIMOIRE);
				if(hasSocket)
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
