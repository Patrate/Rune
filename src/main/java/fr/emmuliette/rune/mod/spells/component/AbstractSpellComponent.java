package fr.emmuliette.rune.mod.spells.component;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.UnknownPropertyException;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.EnumProperty;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.tags.MainTag;
import fr.emmuliette.rune.mod.spells.tags.RestrictionTag;
import fr.emmuliette.rune.mod.spells.tags.Tag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractSpellComponent {
	private ComponentProperties properties;
	private AbstractSpellComponent parent;
	private PropertyFactory propFactory;
	private int spellInternalId;

	public AbstractSpellComponent(PropertyFactory propFact, AbstractSpellComponent parent) {
		this.propFactory = propFact;
		this.properties = getDefaultProperties();
		this.parent = parent;
	}

	protected final int getSpellInternalId() {
		return spellInternalId;
	}

	public final void setSpellInternalId(int id) {
		this.spellInternalId = id;
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

	public int getIntProperty(String key) {
		if (properties.getProperty(key) != null) {
			return ((LevelProperty) properties.getProperty(key)).getValue();
		} else {
			RuneMain.LOGGER.error("unknown level property " + key + " in component " + this.getClass().getSimpleName());
			return 1;
		}
	}

	public int getIntProperty(String key, float boostVal) {
		if (properties.getProperty(key) != null) {
			if (boostVal >= 1f)
				return ((LevelProperty) properties.getProperty(key)).getValue(boostVal);
			else
				return ((LevelProperty) properties.getProperty(key)).getValue();
		} else {
			RuneMain.LOGGER.error("unknown level property " + key + " in component " + this.getClass().getSimpleName());
			return 1;
		}
	}

	public boolean getBoolProperty(String key) {
		if (properties.getProperty(key) != null) {
			return ((BoolProperty) properties.getProperty(key)).getValue();
		} else {
			RuneMain.LOGGER.error("unknown bool property " + key + " in component " + this.getClass().getSimpleName());
			return false;
		}
	}

	public String getEnumProperty(String key) {
		if (properties.getProperty(key) != null) {
			return ((EnumProperty) properties.getProperty(key)).getValue();
		} else {
			RuneMain.LOGGER.error("unknown enum property " + key + " in component " + this.getClass().getSimpleName());
			return null;
		}
	}

	public <T> void setPropertyValue(String key, T newVal) {
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

	public int getCooldown() {
		return 0;
	}

	public final ComponentProperties getDefaultProperties() {
		return propFactory.build();
	}

	public boolean validate(AbstractSpellComponent other) {
		return getTags().getBuildTag().isAllowedAsNext(other.getTags().getBuildTag());
	}

	public boolean validate() {
		MainTag tags = getTags();
		for (Tag t : tags.getTagSet()) {
			if (t instanceof RestrictionTag) {
				if (!((RestrictionTag) t).isValid())
					return false;
			}
		}
		return true;
	}

	public abstract boolean addNextPart(AbstractSpellComponent other);

	public AbstractSpellComponent getParent() {
		return parent;
	}

	public void setParent(AbstractSpellComponent parent) {
		this.parent = parent;
	}

	public MainTag getTags() {
		return MainTag.getTags(this);
	}

	public float getMaxPower() {
		float maxPower = 0f;
		Collection<Property<?>> propList = this.properties.getProperties(Grade.DIAMOND);// this.getGrade())
		for (Property<?> prop : propList) {
			if (prop instanceof LevelProperty && ((LevelProperty) prop).isBoostable()) {
				float tmpMax = ((LevelProperty) prop).getMaxLevel() - ((LevelProperty) prop).getValue();
				if (maxPower < tmpMax)
					maxPower = tmpMax;
			}
		}
		return maxPower;
	}

	public Cost<?> getCost() {
		Cost<?> retour = Cost.ZERO_COST.get();
		Collection<Property<?>> propList = this.properties.getProperties(Grade.DIAMOND);// this.getGrade())
		for (Property<?> prop : propList) {
			retour.add(prop.getCost());
		}
		return retour;
	}

	public Cost<?> getBoostCost() {
		Cost<?> boostCost = null;
		Collection<Property<?>> propList = this.properties.getProperties(Grade.DIAMOND);// this.getGrade())
		for (Property<?> prop : propList) {
			if (prop instanceof LevelProperty && ((LevelProperty) prop).isBoostable()) {
				if (boostCost == null)
					boostCost = ((LevelProperty) prop).getCostPerLevel();
				else
					boostCost.add(((LevelProperty) prop).getCostPerLevel());
			}
		}
		return boostCost;
	}
}
