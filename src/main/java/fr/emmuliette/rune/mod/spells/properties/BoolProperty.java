package fr.emmuliette.rune.mod.spells.properties;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public final class BoolProperty extends Property<Boolean> {
	private Supplier<? extends Cost<?>> cost;

	public BoolProperty(String name, Supplier<? extends Cost<?>> cost) {
		this(name, Grade.UNKNOWN, cost);
	}
	
	public BoolProperty(String name, Grade gradeVisible, Supplier<? extends Cost<?>> cost) {
		super(name, gradeVisible, false);
		this.cost = cost;
	}

	@Override
	public Boolean getValue() {
		return super.getValue();
	}

	@Override
	public void setValueInternal(Boolean val) throws PropertyException {
		if (val != null)
			super.setValueInternal(val);
	}

	@Override
	public INBT valueToNBT() {
		return StringNBT.valueOf(this.getValue().toString());
	}

	@Override
	public Cost<?> getCost() {
		if (!this.getValue())
			return Cost.ZERO_COST.get();
		return cost.get();
	}

	@Override
	public Boolean nBTtoValue(INBT inbt) {
		return Boolean.valueOf(((StringNBT) inbt).getAsString());
	}
	
	
}