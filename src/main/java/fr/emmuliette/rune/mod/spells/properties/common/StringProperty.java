package fr.emmuliette.rune.mod.spells.properties.common;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public final class StringProperty extends Property<String> {
	public StringProperty(String name, Grade gradeVisible) {
		super(name, gradeVisible, "");
	}

	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public INBT valueToNBT() {
		return StringNBT.valueOf(this.getValue());
	}

	@Override
	public Cost<?> getCost() throws PropertyException {
		return Cost.ZERO_COST.get();
	}

	@Override
	public String nBTtoValue(INBT inbt) {
		return ((StringNBT) inbt).getAsString();
	}
}