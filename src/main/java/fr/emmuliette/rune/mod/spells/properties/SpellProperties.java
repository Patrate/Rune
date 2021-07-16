package fr.emmuliette.rune.mod.spells.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.emmuliette.rune.exception.DuplicatePropertyException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public class SpellProperties {
	private HashMap<Grade, Map<String, Property<?>>> properties;

	public SpellProperties() {
		properties = new HashMap<Grade, Map<String, Property<?>>>();
		for(Grade grade:Grade.values()) {
			properties.put(grade, new HashMap<String, Property<?>>());
		}
	}
	
	public SpellProperties(SpellProperties other) {
		this();
		if(other != null) {
			for(Grade grade:Grade.values()) {
				if(!other.properties.containsKey(grade)) {
					continue;
				}
				Map<String, Property<?>> currentGrade = properties.get(grade);
				Map<String, Property<?>> otherGrade = other.properties.get(grade);
				for(String key:otherGrade.keySet()) {
					currentGrade.put(key, otherGrade.get(key));
				}
			}
		}
	}

	public void addNewProperty(Grade key, Property<?> property) throws DuplicatePropertyException {
		Map<String, Property<?>> gradeMap = properties.get(key);
		if(gradeMap.containsKey(property.getName())) {
			throw new DuplicatePropertyException(property);
		}
		gradeMap.put(property.getName(), property);
	}

	public Collection<Property<?>> getProperties(Grade grade) {
		Collection<Property<?>> retour = new ArrayList<Property<?>>();
		int level = grade.getLevel();
		for(Grade g:Grade.values()) {
			if(g.getLevel() <= level) {
				retour.addAll(properties.get(g).values());
			}
		}
		return retour;
	}
	
	public Property<?> getProperty(String key) {
		for(Grade g:Grade.values()) {
			if(properties.get(g).containsKey(key)) {
				return properties.get(g).get(key);
			}
		}
		return null;
	}
	
	public Grade getPropertyGrade(String key) {
		for(Grade g:Grade.values()) {
			if(properties.get(g).containsKey(key)) {
				return g;
			}
		}
		return null;
	}

	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		for (Grade grade:Grade.values()) {
			ListNBT prop = new ListNBT();
			for(Property<?> property:properties.get(grade).values()) {
				INBT propNBT = property.toNBT();
				if(propNBT != null) {
					prop.add(propNBT);
				}
			}
			retour.put(grade.getKey(), prop);
		}
		return retour;
	}
	
	public void fromNBT(INBT data) {
		for (Grade grade:Grade.values()) {
			Map<String, Property<?>> gradeMap = this.properties.get(grade);
			ListNBT prop = (ListNBT) ((CompoundNBT) data).get(grade.getKey());
			for(INBT propertyINBT:prop) {
				CompoundNBT propertyNBT = (CompoundNBT) propertyINBT;
				gradeMap.get(propertyNBT.getString(Property.NAME)).setValue(propertyNBT.get(Property.VALUE));
			}
		}
	}
}
