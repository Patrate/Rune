package fr.emmuliette.rune.mod.blocks.spellBinding;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;

public class SpellBindingInventory implements IInventory {
	private final List<ItemStack> items;
	private final SpellBindingContainer menu;

	public SpellBindingInventory(SpellBindingContainer container, int width, int height) {
		this.items = new ArrayList<ItemStack>(width * height);
		for (int i = 0; i < width * height; ++i)
			this.items.add(ItemStack.EMPTY);
		this.menu = container;
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

	public ItemStack getItem(int index) {
		return index >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(index);
	}

	public ItemStack removeItemNoUpdate(int index) {
		return ItemStackHelper.takeItem(this.items, index);
	}

	@Override
	public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
		ItemStack itemstack = ItemStackHelper.removeItem(this.items, p_70298_1_, p_70298_2_);
		if (!itemstack.isEmpty()) {
			this.menu.slotsChanged(this);
		}

		return itemstack;
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
		while (index >= this.items.size()) {
			this.items.add(ItemStack.EMPTY);
		}
		this.items.set(index, itemStack);
		this.menu.slotsChanged(this);
	}

	@Override
	public void setChanged() {
	}

	public boolean stillValid(PlayerEntity p_70300_1_) {
		return true;
	}

	public void clearContent() {
		this.items.clear();
	}

	public String getSpellName() {
		return menu.getSpellName();
	}
}