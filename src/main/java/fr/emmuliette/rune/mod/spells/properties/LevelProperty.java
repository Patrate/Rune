package fr.emmuliette.rune.mod.spells.properties;

import java.util.Map;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public final class LevelProperty extends Property<Integer> {
	private Map<Grade, Integer> levels;
	private Supplier<? extends Cost<?>> costPerLevel;
	private boolean boostable;

	public LevelProperty(String name, Map<Grade, Integer> levels, Supplier<? extends Cost<?>> manaPerLevel) {
		this(name, levels, manaPerLevel, false);
	}

	public LevelProperty(String name, Map<Grade, Integer> levels, Supplier<? extends Cost<?>> manaPerLevel,
			boolean boostable) {
		super(name, Grade.getMin(levels.keySet()), 1);
		this.setLevels(levels);
		this.costPerLevel = manaPerLevel;
		this.boostable = boostable;
	}

	@Override
	public Integer getValue() {
		return super.getValue();
	}

	public Integer getValue(float boostValue) {
//		return (int) Math.min(getMaxLevel(), this.getValue() + boostValue - 1);
		if (boostable)
			return (int) (this.getValue() + boostValue - 1);
		else
			return this.getValue();
	}

	@Override
	public void setValueInternal(Integer val) throws PropertyException {
		if (val <= getMaxLevel() && val >= 1)
			super.setValueInternal(val);
		throw new PropertyException("val " + val + " out of bound [1, " + getMaxLevel() + "] for property " + this.getName());
	}

	public int getMaxLevel() {
		return levels.get(Grade.getMax(levels.keySet(), this.getCurrentGrade()));
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
		if (this.getValue() < 1)
			return Cost.ZERO_COST.get();
		Cost<?> cost = costPerLevel.get();
		Cost<?> total = Cost.ZERO_COST.get();
		for (int i = 0; i < this.getValue(); i++)
			total.add(cost);
		return total;
	}

	@Override
	public Integer nBTtoValue(INBT inbt) {
		return ((IntNBT) inbt).getAsInt();
	}

	public boolean isBoostable() {
		return boostable;
	}

	public void setBoostable(boolean boostable) {
		this.boostable = boostable;
	}

	public Map<Grade, Integer> getLevels() {
		return levels;
	}

	public void setLevels(Map<Grade, Integer> levels) {
		this.levels = levels;
	}
}