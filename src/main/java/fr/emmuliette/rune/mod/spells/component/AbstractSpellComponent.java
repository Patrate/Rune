package fr.emmuliette.rune.mod.spells.component;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.mod.spells.RuneProperties;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractSpellComponent {
	private RuneProperties properties;
	
	public AbstractSpellComponent(RuneProperties properties) {
		this.properties = properties;
	}
	
	protected RuneProperties getProperties() {
		return properties;
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.putString(Spell.NBT_CLASS, this.getClass().getName());//ComponentRegistry.getComponentName(this));
		retour.put(Spell.NBT_PROPERTIES, properties.toNBT());
		return retour;
	}

	public static AbstractSpellComponent fromNBT(CompoundNBT data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> clazz = Class.forName(data.getString(Spell.NBT_CLASS));
		RuneProperties properties = RuneProperties.fromNBT((CompoundNBT) data.get(Spell.NBT_PROPERTIES));
		AbstractSpellComponent retour = (AbstractSpellComponent) clazz.getConstructor(RuneProperties.class).newInstance(properties);
		if(AbstractCastComponent.class.isAssignableFrom(clazz)) {
			retour = AbstractCastComponent.fromNBT(retour, data);
		}
		return retour;
	}
	
	public abstract float getManaCost();
}
