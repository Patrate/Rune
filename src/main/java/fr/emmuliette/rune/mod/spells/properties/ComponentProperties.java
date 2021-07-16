package fr.emmuliette.rune.mod.spells.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public abstract class ComponentProperties {
	private HashMap<Grade, Map<String, Property<?>>> properties;
	
	public ComponentProperties() {
		properties = new HashMap<Grade, Map<String, Property<?>>>();
		for (Grade grade : Grade.values()) {
			properties.put(grade, new HashMap<String, Property<?>>());
		}
		init();
	}

	protected abstract void init();

	public ComponentProperties addNewProperty(Grade key, Property<?> property) {
		Map<String, Property<?>> gradeMap = properties.get(key);
		gradeMap.put(property.getName(), property);
		return this;
	}

	public Collection<Property<?>> getProperties(Grade grade) {
		Collection<Property<?>> retour = new ArrayList<Property<?>>();
		int level = grade.getLevel();
		for (Grade g : Grade.values()) {
			if (g.getLevel() <= level) {
				retour.addAll(properties.get(g).values());
			}
		}
		return retour;
	}

	public Property<?> getProperty(String key) {
		for (Grade g : Grade.values()) {
			if (properties.get(g).containsKey(key)) {
				return properties.get(g).get(key);
			}
		}
		return null;
	}

	public Grade getPropertyGrade(String key) {
		for (Grade g : Grade.values()) {
			if (properties.get(g).containsKey(key)) {
				return g;
			}
		}
		return null;
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		for (Grade grade : Grade.values()) {
			ListNBT prop = new ListNBT();
			for (Property<?> property : properties.get(grade).values()) {
				INBT propNBT = property.toNBT();
				if (propNBT != null) {
					prop.add(propNBT);
				}
			}
			retour.put(grade.getKey(), prop);
		}
		return retour;
	}
}
