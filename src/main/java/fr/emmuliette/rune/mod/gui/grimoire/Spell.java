package fr.emmuliette.rune.mod.gui.grimoire;

import net.minecraft.item.ItemStack;

public class Spell {
	private ItemStack resultItem;
	
	public Spell(ItemStack resultItem) {
		this.resultItem = resultItem;
	}
	
	public ItemStack getResultItem() {
		return resultItem;
	}
}
