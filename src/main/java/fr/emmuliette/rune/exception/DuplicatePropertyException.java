package fr.emmuliette.rune.exception;

import fr.emmuliette.rune.mod.spells.properties.Property;

public class DuplicatePropertyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7869281933228801046L;

	public DuplicatePropertyException(Property<?> property) {
		super("Duplicate property with key " + property.getName());
	}
}
