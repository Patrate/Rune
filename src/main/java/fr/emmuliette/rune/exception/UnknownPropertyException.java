package fr.emmuliette.rune.exception;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;

public class UnknownPropertyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7869281933228801046L;

	public UnknownPropertyException(AbstractSpellComponent parent, String key) {
		super("Unknown property " + key + " in component " + parent.getClass().getSimpleName());
	}
}
