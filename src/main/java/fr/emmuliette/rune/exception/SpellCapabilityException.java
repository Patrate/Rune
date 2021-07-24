package fr.emmuliette.rune.exception;

import net.minecraft.item.ItemStack;

public class SpellCapabilityException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpellCapabilityException() {
		super("GRIMOIRE EXCEPTION");
	}
	public SpellCapabilityException(ItemStack e) {
		super(e.getDisplayName().getString() + " doesn't have a SpellCapability capability !");
	}
}
