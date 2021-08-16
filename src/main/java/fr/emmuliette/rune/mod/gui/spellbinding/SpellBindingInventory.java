package fr.emmuliette.rune.mod.gui.spellbinding;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public class SpellBindingInventory implements IInventory {
	private final List<ItemStack> items;
	private final Map<Integer, AbstractSpellComponent> components;
	private final SpellBindingContainer menu;
	private List<IInventoryChangedListener> listeners;
	private String spellName;

	public SpellBindingInventory(SpellBindingContainer container, int width, int height) {
		this.items = new ArrayList<ItemStack>(width * height);
		this.components = new HashMap<Integer, AbstractSpellComponent>();
		for (int i = 0; i < width * height; ++i) {
			this.items.add(ItemStack.EMPTY);
		}
		this.menu = container;
		this.spellName = "";
	}

	public void addListener(IInventoryChangedListener p_110134_1_) {
		if (this.listeners == null) {
			this.listeners = Lists.newArrayList();
		}

		this.listeners.add(p_110134_1_);
	}

	public void removeListener(IInventoryChangedListener p_110132_1_) {
		this.listeners.remove(p_110132_1_);
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
		if (itemStack.getItem() instanceof RuneItem)
			this.components.put(index, ((RuneItem) itemStack.getItem()).getSpellComponent());
		else
			this.components.remove(index);
		this.menu.slotsChanged(this);
	}

	public AbstractSpellComponent getComponent(int index) {
		if (this.getItem(index) == ItemStack.EMPTY) {
			return null;
		}
		return this.components.get(index);
	}

	@Override
	public void setChanged() {
		if (this.listeners != null) {
			for (IInventoryChangedListener iinventorychangedlistener : this.listeners) {
				iinventorychangedlistener.containerChanged(this);
			}
		}
		this.menu.slotsChanged(this);
	}

	public boolean stillValid(PlayerEntity p_70300_1_) {
		return true;
	}

	public void clearContent() {
		this.items.clear();
	}

	public boolean setSpellName(String name) {
		if (name.equals(this.spellName))
			return false;
		this.spellName = name;
		return true;
	}

	public String getSpellName() {
		return this.spellName;
	}

	public void setProperties(INBT nbt) {
		if (!(nbt instanceof ListNBT)) {
			RuneMain.LOGGER.error("Should be instance of ListNBT");
			return;
		}
		ListNBT listNbt = (ListNBT) nbt;
		System.out.println("Setting properties " + nbt.toString());
		for (INBT data : listNbt) {
			CompoundNBT cnbt = (CompoundNBT) data;
			int slotId = cnbt.getInt("id");
			try {
				this.components.put(slotId, AbstractSpellComponent.fromNBT(cnbt.getCompound("data")));
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	public INBT getProperties() {
		ListNBT retour = new ListNBT();
		for (int i = 0; i < this.getContainerSize(); i++) {
			ItemStack item = this.getItem(i);
			if (item == ItemStack.EMPTY || !this.components.containsKey(i))
				continue;
			CompoundNBT data = new CompoundNBT();
			data.putInt("id", i);
			data.put("data", this.components.get(i).toNBT());
			retour.add(data);
		}
		return retour;
	}
}