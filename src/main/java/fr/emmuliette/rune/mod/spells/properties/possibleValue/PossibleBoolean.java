package fr.emmuliette.rune.mod.spells.properties.possibleValue;

import net.minecraft.nbt.StringNBT;

public class PossibleBoolean extends PossibleValues<Boolean>{
	public PossibleBoolean() {
		this(true);
	}
	
	public PossibleBoolean(boolean defaultValue) {
		super(defaultValue);
	}

	@Override
	public void getRange() {
	}

	@Override
	public boolean _isValid(Boolean val) {
		return (val != null);
	}

	@Override
	public StringNBT asINBT(Boolean val) {
		return StringNBT.valueOf(val.toString());
	}

	@Override
	public Boolean copyValue(Boolean value) {
		return new Boolean(value);
	}

}
