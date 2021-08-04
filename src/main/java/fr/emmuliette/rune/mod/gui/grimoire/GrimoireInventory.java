package fr.emmuliette.rune.mod.gui.grimoire;

import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class GrimoireInventory implements IInventory {
	private NonNullList<ItemStack> items;
	private final static int MAX_SIZE = 10;

	public GrimoireInventory() {
		this.items = NonNullList.withSize(MAX_SIZE, ItemStack.EMPTY);
	}

	public void init(Grimoire grimoire) {
		int listSize = grimoire.getSpells().size();
		this.items.clear();
//		this.items = NonNullList.withSize(listSize, ItemStack.EMPTY);
		for (int i = 0; i < listSize; i++) {
//			ItemStack item = (i < listSize) ? grimoire.getItem(i) : ItemStack.EMPTY;
			this.items.set(i, grimoire.getItem(i));
		}
	}

	public int getContainerSize() {
		return this.items.size();
//		return this.menu.getGrimoire().getSpells().size();
	}

	public boolean isEmpty() {
		for (ItemStack item : items)
			if (item != null && item != ItemStack.EMPTY)
				return false;
		return true;
	}

	public ItemStack getItem(int index) {
//		index -= j;
		return index >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(index);
	}

	public ItemStack removeItem(int slotId, int nbr) {
//		slotId -= j;
		return ItemStackHelper.takeItem(this.items, slotId);
	}

	public ItemStack removeItemNoUpdate(int slotId) {
//		slotId -= j;
		return ItemStackHelper.takeItem(this.items, slotId);
	}

	public void setItem(int slotId, ItemStack item) {
//		slotId -= j;
		this.items.set(slotId, item);
	}

	public boolean stillValid(PlayerEntity p_70300_1_) {
		return true;
	}

	public void setChanged() {
	}

	public void clearContent() {
		this.items.clear();
	}
}