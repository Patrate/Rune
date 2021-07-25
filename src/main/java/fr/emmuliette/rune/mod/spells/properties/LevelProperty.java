package fr.emmuliette.rune.mod.spells.properties;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public final class LevelProperty extends Property<Integer> {
	private int maxLevel;
	private int manaPerLevel;

	public LevelProperty(String name, int maxLevel, int manaPerLevel) {
		super(name, 1);
		this.maxLevel = maxLevel;
		this.manaPerLevel = manaPerLevel;
	}

	@Override
	public Integer getValue() {
		return super.getValue();
	}
	
	@Override
	public void setValue(Integer val) {
		if(val <= maxLevel && val >= 1)
			super.setValue(val);
		// TODO throw exception
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}

	@Override
	public INBT getValueAsNBT() {
		return IntNBT.valueOf(this.getValue());
	}

	@Override
	public float getManaCost() {
		return this.getValue() * manaPerLevel;
	}
}