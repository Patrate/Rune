package fr.emmuliette.rune.mod.items;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import net.minecraft.item.Item;

public class RuneItem extends Item {
	private final Class<? extends AbstractSpellComponent> component;
	
	public RuneItem(Class<? extends AbstractSpellComponent> component, Item.Properties properties) {
		super(properties);
		this.component = component;
	}

	public Class<? extends AbstractSpellComponent> getComponentClass() {
		return component;
	}
	
	//public AbstractSpellComponent getSpellComponent(AbstractSpellComponent parent) {
	public AbstractSpellComponent getSpellComponent() {
		try {
			AbstractSpellComponent comp = component.getConstructor(AbstractSpellComponent.class).newInstance((AbstractSpellComponent)null);
			comp.initProperties();
			return comp;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			System.out.println("SPELL COMP CLASS IS " + component.getCanonicalName());
			e.printStackTrace();
			return null;
		}
	}
}
