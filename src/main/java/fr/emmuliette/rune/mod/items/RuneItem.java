package fr.emmuliette.rune.mod.items;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.mod.spells.RuneProperties;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import net.minecraft.item.Item;

public class RuneItem extends Item {
	private final Class<? extends AbstractSpellComponent> component;
	private final RuneProperties properties;
	
	public RuneItem(Class<? extends AbstractSpellComponent> component, Item.Properties properties) {
		super(properties);
		this.component = component;
		this.properties = new RuneProperties();
	}

	public Class<? extends AbstractSpellComponent> getComponentClass() {
		return component;
	}
	
	public AbstractSpellComponent getSpellComponent(RuneProperties properties) {
		try {
			return component.getConstructor(RuneProperties.class).newInstance(properties);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public RuneProperties getProperties() {
		return properties;
	}
}
