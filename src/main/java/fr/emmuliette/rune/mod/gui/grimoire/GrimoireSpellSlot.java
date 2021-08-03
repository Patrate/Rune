package fr.emmuliette.rune.mod.gui.grimoire;

import fr.emmuliette.rune.mod.items.SpellItem;
import fr.emmuliette.rune.mod.items.SpellItem.ItemType;
import fr.emmuliette.rune.mod.spells.capability.ISpell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class GrimoireSpellSlot extends Slot {
	//private ISpell spell;

	public GrimoireSpellSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ISpell spell) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		//this.spell = spell;
		this.set(SpellItem.buildSpellItem(spell.getSpell(), ItemType.SPELL));
	}

	@Override
	public boolean mayPlace(ItemStack iStack) {
		return false;
	}

	@Override
	public ItemStack onTake(PlayerEntity player, ItemStack iStack) {
		//this.setChanged();
		return iStack.copy();
	}

}
