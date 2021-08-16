package fr.emmuliette.rune.mod.spells.properties;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public final class BoolProperty extends Property<Boolean> {
	private Supplier<? extends Cost<?>> cost;

	public BoolProperty(String name, Supplier<? extends Cost<?>> cost) {
		super(name, false);
		this.cost = cost;
	}

	@Override
	public Boolean getValue() {
		return super.getValue();
	}

	@Override
	public void setValueInternal(Boolean val) {
		if (val != null)
			super.setValueInternal(val);
		// TODO throw exception
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