package fr.emmuliette.rune.mod.blocks.spellBinding;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SpellBindingRuneSlot extends Slot {

	public SpellBindingRuneSlot(IInventory inventaire, int p_i45790_4_, int p_i45790_5_, int p_i45790_6_) {
		super(inventaire, p_i45790_4_, p_i45790_5_, p_i45790_6_);
	}

	public boolean mayPlace(ItemStack iStack) {
		// TODO renvoyer true seulement si c'est une rune et qu'elle est du bon type
		return super.mayPlace(iStack);
	}
}