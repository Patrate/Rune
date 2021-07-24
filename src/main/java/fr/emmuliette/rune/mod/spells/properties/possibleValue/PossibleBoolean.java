package fr.emmuliette.rune.mod.spells.properties.possibleValue;

import com.google.common.base.Function;

import net.minecraft.nbt.StringNBT;

public class PossibleBoolean extends PossibleValues<Boolean>{
	public static Function<Boolean, Float> ZERO = new Function<Boolean, Float>(){
		@Override
		public Float apply(Boolean input) {
			return 0f;
		}
	};
	public static Function<Boolean, Float> PLUS_ONE_IF_TRUE = new Function<Boolean, Float>(){
		@Override
		public Float apply(Boolean input) {
			return (input)?1f:0f;
		}
	};
	
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
		return value;
	}

}
