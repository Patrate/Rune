package fr.emmuliette.rune.mod.spells.properties.variable;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public abstract class VariableProperty<T> extends Property<T> {
	private static final String VAR = "var", VALUE = "value";
	protected boolean variablePossible;
	private boolean variable;
	protected int cost;

	public VariableProperty(String name, int cost) {
		super(name, Grade.WOOD, null);
		this.variablePossible = true;
		this.variable = true;
		this.cost = cost;
	}

	public VariableProperty(String name, T def) {
		super(name, Grade.WOOD, def);
		this.variablePossible = false;
		this.variable = false;
		this.cost = 0;
	}
	
	@Override
	public Cost<?> getCost() throws PropertyException {
		return new ManaCost(cost);
	}

	protected abstract INBT variableToNBT(T val);

	protected abstract T NBTToVariable(INBT inbt);

	public boolean isVariable() {
		return variable;
	}
	
	public void setVariable(boolean variable) {
		this.variable = variable;
	}
	
	public boolean variablePossible() {
		return variablePossible;
	}

	@Override
	public final INBT valueToNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean(VAR, isVariable());
		if (this.getValue() != null)
			nbt.put(VALUE, variableToNBT(this.getValue()));
		return nbt;
	}

	@Override
	public final T nBTtoValue(INBT inbt) {
		CompoundNBT nbt = (CompoundNBT) inbt;
		this.variable = nbt.getBoolean(VAR);
		if (nbt.contains(VALUE))
			return NBTToVariable(nbt.get(VALUE));
		return null;
	}
}