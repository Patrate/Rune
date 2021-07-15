package fr.emmuliette.rune.mod.spells.properties.possibleValue;

import java.lang.reflect.ParameterizedType;

import net.minecraft.nbt.INBT;

public abstract class PossibleValues<T> {
	@SuppressWarnings("unchecked")
	Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];
	private T defaultValue;
	//private Collection<T> range;

	public PossibleValues(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	public T getDefault() {
		return defaultValue;
	}

	public abstract void getRange();
	
	@SuppressWarnings("unchecked")
	public final boolean isValid(Object val) {
		if(type.isInstance(val)) {
			return _isValid((T) val);
		}
		return false;
	}
	public abstract boolean _isValid(T val);
	public abstract INBT asINBT(T val);
	public abstract T copyValue(T value);
}