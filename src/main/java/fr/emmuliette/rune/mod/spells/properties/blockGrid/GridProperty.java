package fr.emmuliette.rune.mod.spells.properties.blockGrid;

import java.util.Map;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;
import net.minecraft.nbt.INBT;

public final class GridProperty extends Property<BlockGrid> {
	private Supplier<? extends Cost<?>> costPerBlock;
	private Map<Grade, Integer> levels;

//	public GridProperty(String name, Map<Grade, Integer> levels, Supplier<? extends Cost<?>> manaPerLevel) {
//		this(name, levels, manaPerLevel, false);
//	}

	public GridProperty(String name, int zero, Map<Grade, Integer> levels, Supplier<? extends Cost<?>> manaPerBlock,
			boolean boostable) {
		super(name, Grade.getMin(levels.keySet()), new BlockGrid());
		levels.put(Grade.UNKNOWN, zero);
		this.setLevels(levels);
		this.costPerBlock = manaPerBlock;
	}

	@Override
	public BlockGrid getValue() {
		return super.getValue();
	}

	@Override
	public void setValueInternal(BlockGrid val) throws PropertyException {
		if (val.getSize() <= getMaxBlocks())
			super.setValueInternal(val);
		throw new PropertyException("Size too big for property " + this.getName());
	}

	public int getMaxBlocks() {
		return levels.get(Grade.getMax(levels.keySet(), this.getCurrentGrade()));
	}

	@Override
	public INBT valueToNBT() {
		return this.getValue().toNBT();
	}

	@Override
	public Cost<?> getCost() {
		if (this.getValue().getSize() < 1)
			return Cost.ZERO_COST.get();
		Cost<?> cost = costPerBlock.get();
		Cost<?> total = Cost.ZERO_COST.get();
		for (int i = 0; i < this.getValue().getSize(); i++)
			total.add(cost);
		return total;
	}

	@Override
	public BlockGrid nBTtoValue(INBT inbt) {
		try {
			return BlockGrid.fromNBT(inbt);
		} catch (PropertyException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<Grade, Integer> getLevels() {
		return levels;
	}

	public void setLevels(Map<Grade, Integer> levels) {
		this.levels = levels;
	}
}