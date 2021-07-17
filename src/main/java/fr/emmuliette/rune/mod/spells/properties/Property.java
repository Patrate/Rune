package fr.emmuliette.rune.mod.spells.properties;

import com.google.common.base.Function;

import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleValues;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public final class Property<T> {
	public static final String NAME = "name";
	public static final String VALUE = "value";
	private String name;
	private PossibleValues<T> values;
	private T currentValue;
	private Function<T, Float> manaCost;

	public Property(String name, PossibleValues<T> values, Function<T, Float> manaCost) {
		this.name = name;
		this.values = values;
		this.manaCost = manaCost;
		this.currentValue = values.getDefault();
	}
	
	public Property(Property<T> other) {
		this.name = other.name;
		this.values = other.values;
		this.manaCost = other.manaCost;
		this.currentValue = other.values.copyValue(other.getValue());
	}

	public String getName() {
		return name;
	}

	public T getDefault() {
		return values.getDefault();
	}

	public T getValue() {
		return currentValue;
	}

	@SuppressWarnings("unchecked")
	public boolean setValue(Object val) {
		try {
			if (values.isValid(val)) {
				currentValue = (T) val;
				return true;
			}
			return false;
		} catch(ClassCastException e) {
			e.printStackTrace();
			return false;
		}
	}

	public float getCurrentManaCost() {
		return manaCost.apply(currentValue);
	}

	public float getManaCostFor(T val) {
		return manaCost.apply(val);
	}
	
	public boolean isDefaultValue() {
		return currentValue == getDefault();
	}
	
	public INBT getValueAsNBT() {
		return values.asINBT(this.getValue());
	}
	
	public INBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.putString(NAME, this.getName());
		retour.put(VALUE, getValueAsNBT());
		return retour;
	}
}