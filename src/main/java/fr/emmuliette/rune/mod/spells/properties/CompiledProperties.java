package fr.emmuliette.rune.mod.spells.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CompiledProperties {
	private Map<String, Property<?>> full;
	private Map<String, Property<?>> visibleProperties;
	private Map<String, Property<?>> hiddenProperties;

	public CompiledProperties(RuneProperties compProp, Grade grade) {
		visibleProperties = new HashMap<String, Property<?>>();
		hiddenProperties = new HashMap<String, Property<?>>();
		Map<String, Property<?>> full = new HashMap<String, Property<?>>();
		for(Grade g:Grade.values()) {
			if(g == grade || g.getLevel() < grade.getLevel()) {
				for(Property<?> prop:compProp.getProperties(g)) {
					visibleProperties.put(prop.getName(), prop);
					full.put(prop.getName(), prop);
				}
			} else {
				for(Property<?> prop:compProp.getProperties(g)) {
					if(visibleProperties.containsKey(prop.getName())) {
						continue;
					}
					hiddenProperties.put(prop.getName(), prop);
					full.put(prop.getName(), prop);
				}
			}
		}
	}

	public Map<String, Property<?>> getAllProperties() {
		return full;
	}

	public Property<?> getProperty(String key) {
		return full.get(key);
	}

	public Map<String, Property<?>> getVisibleProperties() {
		return visibleProperties;
	}
	
	public Map<String, Property<?>> getHiddenProperties() {
		return hiddenProperties;
	}

	public Set<String> getKeys() {
		return full.keySet();
	}
}
