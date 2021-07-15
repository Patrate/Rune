package fr.emmuliette.rune.mod.spells.component;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.SpellProperties;
import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractSpellComponent {
	private SpellProperties properties;
	
	public AbstractSpellComponent() {
		this.properties = new SpellProperties(getDefaultProperties());
	}
	
	public void initProperties(SpellProperties prop) {
		this.properties = new SpellProperties(prop);
	}
	
	protected SpellProperties getProperties() {
		return properties;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Property<T> getProperty(String key, T object) {
		if(properties.getProperty(key) != null) {
			return (Property<T>) properties.getProperty(key);
		} else {
			return (Property<T>) this.getDefaultProperties().getProperty(key);
		}
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.putString(Spell.NBT_CLASS, this.getClass().getName());//ComponentRegistry.getComponentName(this));
		retour.put(Spell.NBT_PROPERTIES, properties.toNBT());
		return retour;
	}

	public static AbstractSpellComponent fromNBT(CompoundNBT data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> clazz = Class.forName(data.getString(Spell.NBT_CLASS));
		AbstractSpellComponent retour = (AbstractSpellComponent) clazz.getConstructor().newInstance();
		retour.properties.fromNBT((CompoundNBT) data.get(Spell.NBT_PROPERTIES));
		
		if(AbstractCastComponent.class.isAssignableFrom(clazz)) {
			retour = AbstractCastComponent.fromNBT(retour, data);
		}
		return retour;
	}
	
	public abstract float getManaCost();
	public int getCooldown() {
		return 0;
	}
	
	public abstract SpellProperties getDefaultProperties();
}
