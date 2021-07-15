package fr.emmuliette.rune.mod.items;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.SpellProperties;
import net.minecraft.item.Item;

public class RuneItem extends Item {
	private final Class<? extends AbstractSpellComponent> component;
	private final SpellProperties properties;
	
	public RuneItem(Class<? extends AbstractSpellComponent> component, Item.Properties properties) {
		super(properties);
		this.component = component;
		this.properties = new SpellProperties();
	}

	public Class<? extends AbstractSpellComponent> getComponentClass() {
		return component;
	}
	
	public AbstractSpellComponent getSpellComponent(SpellProperties properties) {
		try {
			AbstractSpellComponent comp = component.getConstructor().newInstance();
			comp.setProperties(properties);
			return comp;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public SpellProperties getProperties() {
		return properties;
	}
}
