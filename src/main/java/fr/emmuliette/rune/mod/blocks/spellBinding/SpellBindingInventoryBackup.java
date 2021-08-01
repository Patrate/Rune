package fr.emmuliette.rune.mod.blocks.spellBinding;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.NonNullList;

public class SpellBindingInventoryBackup implements IInventory, IRecipeHelperPopulator {
	private final NonNullList<ItemStack> items;
	private final int width;
	private final int height;
	private final Container menu;

	public SpellBindingInventoryBackup(Container container, int width, int height) {
		this.items = NonNullList.withSize(width * height, ItemStack.EMPTY);
		this.menu = container;
		this.width = width;
		this.height = height;
	}

	public int getContainerSize() {
		return this.items.size();
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public ItemStack getItem(int p_70301_1_) {
		return p_70301_1_ >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(p_70301_1_);
	}

	public ItemStack removeItemNoUpdate(int p_70304_1_) {
		return ItemStackHelper.takeItem(this.items, p_70304_1_);
	}

	public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
		ItemStack itemstack = ItemStackHelper.removeItem(this.items, p_70298_1_, p_70298_2_);
		if (!itemstack.isEmpty()) {
			this.menu.slotsChanged(this);
		}

		return itemstack;
	}

	public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
		this.items.set(p_70299_1_, p_70299_2_);
		this.menu.slotsChanged(this);
	}

	public void setChanged() {
	}

	public boolean stillValid(PlayerEntity p_70300_1_) {
		return true;
	}

	public void clearContent() {
		this.items.clear();
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public void fillStackedContents(RecipeItemHelper p_194018_1_) {
		for (ItemStack itemstack : this.items) {
			p_194018_1_.accountSimpleStack(itemstack);
		}

	}
}