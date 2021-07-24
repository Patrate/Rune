package fr.emmuliette.rune.mod.spells.capability;

import fr.emmuliette.rune.exception.SpellCapabilityException;
import net.minecraft.item.ItemStack;

public class GrimoireSpellException extends SpellCapabilityException {


	public GrimoireSpellException() {
		super(null);
	}
	
	public GrimoireSpellException(ItemStack e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1851627957191623380L;

}
