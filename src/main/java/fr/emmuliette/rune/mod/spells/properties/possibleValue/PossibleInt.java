package fr.emmuliette.rune.mod.spells.properties.possibleValue;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class PossibleInt extends PossibleValues<Integer>{
	private int min, max, step;
	
	public PossibleInt() {
		this(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
	}
	
	public PossibleInt(Integer defaultValue, int min, int max, int step) {
		super(defaultValue);
		this.min = min;
		this.max = max - ((max - min)%step);
		this.step = step;
	}

	@Override
	public void getRange() {
		// TODO: Renvoyer un tableau d'entier allant de min à max avec un step de step ?
	}

	@Override
	public boolean _isValid(Integer val) {
		if(val < min || val > max) {
			return false;
		}
		if((val - min)%step != 0) {
			return false;
		}
		return true;
	}

	@Override
	public INBT asINBT(Integer val) {
		return IntNBT.valueOf(val);
	}

	@Override
	public Integer copyValue(Integer value) {
		return new Integer(value);
	}

}
