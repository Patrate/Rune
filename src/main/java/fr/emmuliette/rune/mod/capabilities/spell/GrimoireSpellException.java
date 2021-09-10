package fr.emmuliette.rune.mod.capabilities.spell;

import fr.emmuliette.rune.exception.SpellCapabilityException;
import net.minecraft.item.ItemStack;

public class GrimoireSpellException extends SpellCapabilityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1851627957191623380L;

	public GrimoireSpellException() {
		super(null);
	}

	public GrimoireSpellException(ItemStack e) {
		super(e);
	}

}
