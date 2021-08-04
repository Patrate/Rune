package fr.emmuliette.rune.mod.gui.spellbinding;

import fr.emmuliette.rune.mod.items.RuneItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SpellBindingRuneSlot extends Slot {

	public SpellBindingRuneSlot(IInventory inventaire, int p_i45790_4_, int p_i45790_5_, int p_i45790_6_) {
		super(inventaire, p_i45790_4_, p_i45790_5_, p_i45790_6_);
	}

	public boolean mayPlace(ItemStack iStack) {
		// TODO faire un mayPlacec en fonction de si le component peut être placé
		return iStack.getItem() instanceof RuneItem;
	}
}
