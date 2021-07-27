package fr.emmuliette.rune.mod.spells.properties;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public final class LevelProperty extends Property<Integer> {
	private int maxLevel;
	private Supplier<? extends Cost<?>> costPerLevel;
	private boolean boostable;

	public LevelProperty(String name, int maxLevel, Supplier<? extends Cost<?>> manaPerLevel) {
		this(name, maxLevel, manaPerLevel, false);
	}
	
	public LevelProperty(String name, int maxLevel,  Supplier<? extends Cost<?>> manaPerLevel, boolean boostable) {
		super(name, 1);
		this.maxLevel = maxLevel;
		this.costPerLevel = manaPerLevel;
		this.boostable = boostable;
	}

	@Override
	public Integer getValue() {
		return super.getValue();
	}

	public Integer getValue(float boostValue) {
		return (int) Math.min(maxLevel, this.getValue() + boostValue - 1);
	}
	
	@Override
	public void setValueInternal(Integer val) {
		if(val <= maxLevel && val >= 1)
			super.setValue(val);
		// TODO throw exception
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public Cost<?> getCostPerLevel() {
		return costPerLevel.get();
	}

	@Override
	public INBT valueToNBT() {
		return IntNBT.valueOf(this.getValue());
	}

	@Override
	public Cost<?> getCost() {
		if(this.getValue() < 1)
			return Cost.ZERO_COST.get();
		Cost<?> total = costPerLevel.get();
		for(int i = 0; i < this.getValue(); i++)
			total.add(total);
		return total;
	}

	@Override
	public Integer nBTtoValue(INBT inbt) {
		return ((IntNBT)inbt).getAsInt();
	}

	public boolean isBoostable() {
		return boostable;
	}
}