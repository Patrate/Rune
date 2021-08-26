package fr.emmuliette.rune.mod.spells.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public abstract class RuneProperties {
	private Map<String, Property<?>> properties;

	public RuneProperties() {
		properties = new HashMap<String, Property<?>>();
		init();
	}

	protected abstract void init();

	public void sync(RuneProperties other) {
		for (String key : getKeys()) {
			this.getProperty(key).setValue(other.getProperty(key).getValue());
		}
	}

	public RuneProperties addNewProperty(Property<?> property) {
		properties.put(property.getName(), property);
		return this;
	}
	
	public void addAll(RuneProperties other) {
		for(Property<?> prop:other.properties.values()) {
			addNewProperty(prop);
		}
	}
	
	public Collection<Property<?>> getProperties() {
		return properties.values();
	}

	public Collection<Property<?>> getProperties(Grade grade) {
		Collection<Property<?>> retour = new ArrayList<Property<?>>();
		for(Property<?> property:properties.values()) {
			if(property.isVisible(grade))
				retour.add(property);
		}
		return retour;
	}

	public Property<?> getProperty(String key) {
		return properties.get(key);
	}

	public Set<String> getKeys() {
		return properties.keySet();
	}

	public INBT toNBT() {
		ListNBT prop = new ListNBT();
		for (Property<?> property : properties.values()) {
			INBT propNBT = property.toNBT();
			if (propNBT != null) {
				prop.add(propNBT);
			} else {
				RuneMain.LOGGER.debug("Property is null and shouldn't be");
			}
		}
		return prop;
	}

	public Collection<? extends ITextComponent> getTooltips(Grade grade) {
		List<ITextComponent> retour = new ArrayList<ITextComponent>();
		for (Property<?> prop : getProperties(grade)) {
			retour.add(new StringTextComponent(prop.getName() + ": " + prop.getValue()));
		}
		return retour;
	}
}
