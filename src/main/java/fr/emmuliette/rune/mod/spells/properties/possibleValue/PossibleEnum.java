package fr.emmuliette.rune.mod.spells.properties.possibleValue;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.StringNBT;

public class PossibleEnum extends PossibleValues<String> {
	Set<String> possibleValues;

	public PossibleEnum(String defaultValue, String... possibleValues) {
		super(defaultValue);
		this.possibleValues = new HashSet<String>();
		for (String s : possibleValues) {
			this.possibleValues.add(s);
		}
	}

	@Override
	public void getRange() {
	}

	@Override
	public boolean _isValid(String val) {
		return (val != null && this.possibleValues.contains(val));
	}

	@Override
	public StringNBT asINBT(String val) {
		return StringNBT.valueOf(val);
	}

	@Override
	public String copyValue(String value) {
		return value;
	}

}
