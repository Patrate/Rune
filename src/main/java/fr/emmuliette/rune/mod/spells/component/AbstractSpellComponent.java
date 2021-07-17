package fr.emmuliette.rune.mod.spells.component;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.UnknownPropertyException;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.build.IBuildPart;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractSpellComponent implements IBuildPart {
	private ComponentProperties properties;
	private AbstractSpellComponent parent;
	private PropertyFactory propFactory;

	public AbstractSpellComponent(PropertyFactory propFact, AbstractSpellComponent parent) {
		this.propFactory = propFact;
		this.properties = getDefaultProperties();
		this.parent = parent;
	}

	public abstract boolean applyOnTarget(LivingEntity target, SpellContext context);

	public abstract boolean applyOnPosition(World world, BlockPos target, SpellContext context);

	public boolean applyOnSelf(LivingEntity self, SpellContext context) {
		return applyOnTarget(self, context);
	}

	public boolean isStartingComponent() {
		return getParent() == null;
	}

	public void initProperties() {
		this.properties = propFactory.build();
	}

	protected ComponentProperties getProperties() {
		return properties;
	}
	
	public void syncProperties(AbstractSpellComponent other) {
		this.properties.sync(other.properties);
	}

	protected Property<?> getProperty(String key) throws UnknownPropertyException {
		if (properties.getProperty(key) != null) {
			return properties.getProperty(key);
		} else {
			throw new UnknownPropertyException(this, key);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T getPropertyValue(String key, T defVal) {
		if (properties.getProperty(key) != null) {
			return (T) properties.getProperty(key).getValue();
		} else {
			RuneMain.LOGGER.error("unknown property " + key + " in component " + this.getClass().getSimpleName());
			return defVal;
		}
	}

	protected <T> void setPropertyValue(String key, T newVal) {
		if (properties.getProperty(key) != null) {
			properties.getProperty(key).setValue(newVal);
		} else {
			RuneMain.LOGGER.error("unknown property " + key + " in component " + this.getClass().getSimpleName());
		}
	}

	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.putString(Spell.NBT_CLASS, this.getClass().getName());
		retour.put(Spell.NBT_PROPERTIES, properties.toNBT());
		return retour;
	}

	public static AbstractSpellComponent fromNBT(CompoundNBT data)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> clazz = Class.forName(data.getString(Spell.NBT_CLASS));
		AbstractSpellComponent retour = (AbstractSpellComponent) clazz.getConstructor(AbstractSpellComponent.class)
				.newInstance((AbstractSpellComponent) null);
		retour.properties = retour.propFactory.fromNBT((CompoundNBT) data.get(Spell.NBT_PROPERTIES));

		/*
		 * if (AbstractCastComponent.class.isAssignableFrom(clazz)) { retour =
		 * AbstractCastComponent.fromNBT(retour, data); }
		 */
		return retour;
	}

	public abstract float getManaCost();

	public int getCooldown() {
		return 0;
	}

	public final ComponentProperties getDefaultProperties() {
		return propFactory.build();
	}

	public abstract boolean addNextPart(AbstractSpellComponent other);

	public AbstractSpellComponent getParent() {
		return parent;
	}

	public void setParent(AbstractSpellComponent parent) {
		this.parent = parent;
	}
}
