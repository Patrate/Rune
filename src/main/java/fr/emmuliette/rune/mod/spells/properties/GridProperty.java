package fr.emmuliette.rune.mod.spells.properties;

import java.util.Map;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import net.minecraft.nbt.INBT;

public final class GridProperty extends Property<BlockGrid> {
	private Supplier<? extends Cost<?>> costPerLevel;
	private Map<Grade, Integer> levels;

	public GridProperty(String name, Map<Grade, Integer> levels, Supplier<? extends Cost<?>> manaPerLevel) {
		this(name, levels, manaPerLevel, false);
	}

	public GridProperty(String name, Map<Grade, Integer> levels, Supplier<? extends Cost<?>> manaPerLevel,
			boolean boostable) {
		super(name, Grade.getMin(levels.keySet()), new BlockGrid());
		this.setLevels(levels);
		this.costPerLevel = manaPerLevel;
	}

	@Override
	public BlockGrid getValue() {
		return super.getValue();
	}

	@Override
	public void setValueInternal(BlockGrid val) {
		if (val.getSize() <= getMaxLevel())
			super.setValueInternal(val);
		// TODO throw exception
		System.out.println("Size too big ! ");
		super.setValueInternal(val);
	}

	public int getMaxLevel() {
		return levels.get(Grade.getMax(levels.keySet(), this.getCurrentGrade()));
	}

	public Cost<?> getCostPerLevel() {
		return costPerLevel.get();
	}

	@Override
	public INBT valueToNBT() {
		return this.getValue().toNBT();
	}

	@Override
	public Cost<?> getCost() {
		if (this.getValue().getSize() < 1)
			return Cost.ZERO_COST.get();
		Cost<?> total = costPerLevel.get();
		for (int i = 0; i < this.getValue().getSize(); i++)
			total.add(total);
		return total;
	}

	@Override
	public BlockGrid nBTtoValue(INBT inbt) {
		return BlockGrid.fromNBT(inbt);
	}

	public Map<Grade, Integer> getLevels() {
		return levels;
	}

	public void setLevels(Map<Grade, Integer> levels) {
		this.levels = levels;
	}
}