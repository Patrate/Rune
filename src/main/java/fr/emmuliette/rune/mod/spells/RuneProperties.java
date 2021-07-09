package fr.emmuliette.rune.mod.spells;

import java.util.HashMap;
import java.util.Map;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;

public class RuneProperties {
	public enum Property {
		PREVIOUS(AbstractSpellComponent.class);

		public final Class<?> clazz;

		private Property(Class<?> clazz) {
			this.clazz = clazz;
		}
	}

	private Map<Property, Object> properties;

	public RuneProperties() {
		properties = new HashMap<Property, Object>();
	}

	public void setProperty(Property property, Object value) throws RunePropertiesException {
		if(property.clazz.isAssignableFrom(value.getClass())) {
			properties.put(property, value);
		} else {
			throw new RunePropertiesException("Can't put " + value + " of class " + value.getClass() + " in property field " + property.name());
		}
	}

	public Object getProperty(Property property) throws RunePropertiesException {
		if(properties.containsKey(property)) {
			return properties.get(property);
		} else {
			throw new RunePropertiesException("Property " + property.name() + " is undefined");
		}
	}
}
