package fr.emmuliette.rune.mod.spells.properties;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public abstract class Property<T> {
	public static final String NAME = "name";
	public static final String VALUE = "value";
	private String name;
	private String description;
	private T currentValue;

	public Property(String name, T def) {
		this.name = name;
		this.currentValue = def;
		this.description = "";
	}

	public String getDescription() {
		return description;
	}

	public Property<T> setDescription(String s) {
		this.description = s;
		return this;
	}

	public String getName() {
		return name;
	}

	public T getValue() {
		return currentValue;
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object val) {
		setValueInternal((T) val);
	}

	protected void setValueInternal(T val) {
		currentValue = val;
	}

	public void setValue(INBT inbt) {
		currentValue = nBTtoValue(inbt);
	}

	public abstract T nBTtoValue(INBT inbt);

	public abstract INBT valueToNBT();

	public INBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.putString(NAME, this.getName());
		retour.put(VALUE, valueToNBT());
		return retour;
	}

	public abstract Cost<?> getCost();
}