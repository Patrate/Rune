package fr.emmuliette.rune.mod.blocks.spellBinding;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.NonNullList;

public class SpellBindingInventory implements IInventory, IRecipeHelperPopulator {
	private final NonNullList<ItemStack> items;
	// Should be tree, not grid
	private final Container menu;

	public SpellBindingInventory(Container container) {
		this.items = NonNullList.withSize(1, ItemStack.EMPTY);
		this.menu = container;
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getItem(int containerId) {
		return containerId >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(containerId);
	}

	@Override
	public ItemStack removeItemNoUpdate(int p_70304_1_) {
		return ItemStackHelper.takeItem(this.items, p_70304_1_);
	}

	@Override
	public ItemStack removeItem(int containerId, int p_70298_2_) {
		ItemStack itemstack = ItemStackHelper.removeItem(this.items, containerId, p_70298_2_);
		if (!itemstack.isEmpty()) {
			this.menu.slotsChanged(this);
		}

		return itemstack;
	}

	@Override
	public void setItem(int index, ItemStack iStack) {
		while(index >= this.items.size()) {
			this.items.add(ItemStack.EMPTY);
		}
		this.items.set(index, iStack);
		this.menu.slotsChanged(this);
	}

	@Override
	public void setChanged() {
	}

	@Override
	public boolean stillValid(PlayerEntity p_70300_1_) {
		return true;
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	public int getHeight() {
		return 1;
	}

	public int getWidth() {
		return this.items.size();
	}

	@Override
	public void fillStackedContents(RecipeItemHelper p_194018_1_) {
		for (ItemStack itemstack : this.items) {
			p_194018_1_.accountSimpleStack(itemstack);
		}

	}
}