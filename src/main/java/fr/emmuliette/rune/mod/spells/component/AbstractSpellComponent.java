package fr.emmuliette.rune.mod.spells.component;

import fr.emmuliette.rune.mod.spells.RuneProperties;

public abstract class AbstractSpellComponent {
	private RuneProperties properties;
	
	public AbstractSpellComponent(RuneProperties properties) {
		this.properties = properties;
	}
	
	protected RuneProperties getProperties() {
		return properties;
	}
}
