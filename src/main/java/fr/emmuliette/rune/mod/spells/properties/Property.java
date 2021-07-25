package fr.emmuliette.rune.mod.spells.properties;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public abstract class Property<T> {
	public static final String NAME = "name";
	public static final String VALUE = "value";
	private String name;
	private T currentValue;

	public Property(String name, T def) {
		this.name = name;
		this.currentValue = def;
	}

	public String getName() {
		return name;
	}

	public T getValue() {
		return currentValue;
	}

	public void setValue(T val) {
		currentValue = val;
	}
	
	public abstract INBT getValueAsNBT();
	
	public INBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.putString(NAME, this.getName());
		retour.put(VALUE, getValueAsNBT());
		return retour;
	}
	
	public abstract float getManaCost();
}