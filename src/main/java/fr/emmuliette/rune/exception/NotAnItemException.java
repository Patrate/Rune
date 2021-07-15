package fr.emmuliette.rune.exception;

import fr.emmuliette.rune.mod.AbstractModObject;

public class NotAnItemException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAnItemException(AbstractModObject e) {
		super(e.getName() + " is not a block !");
	}
}
