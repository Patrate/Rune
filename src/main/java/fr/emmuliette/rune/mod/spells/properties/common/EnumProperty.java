package fr.emmuliette.rune.mod.spells.properties.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public final class EnumProperty extends Property<String> {

	private Map<EnumElement, Supplier<? extends Cost<?>>> tagNCost;

	public EnumProperty(String name, String def, Map<EnumElement, Supplier<? extends Cost<?>>> tagNCost) {
		this(name, Grade.UNKNOWN, def, tagNCost);
	}

	public EnumProperty(String name, Grade gradeVisible, String def,
			Map<EnumElement, Supplier<? extends Cost<?>>> tagNCost) {
		super(name, gradeVisible, def);
		this.tagNCost = tagNCost;
	}

	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public void setValueInternal(String s) throws PropertyException {
		for (EnumElement e : this.tagNCost.keySet()) {
			if (e.getValue().equals(s)) {
				super.setValueInternal(s);
				return;
			}
		}
		throw new PropertyException(s + " is not a possible value for property " + this.getName());
	}

	@Override
	public INBT valueToNBT() {
		return StringNBT.valueOf(this.getValue());
	}

	@Override
	public Cost<?> getCost() throws PropertyException {
		for (EnumElement e : this.tagNCost.keySet()) {
			if (e.getValue().equals(this.getValue()))
				return tagNCost.get(e).get();
		}
		throw new PropertyException(this.getValue() + " is not a correct value for property " + this.getName());
	}

	@Override
	public String nBTtoValue(INBT inbt) {
		return ((StringNBT) inbt).getAsString();
	}

	public Set<String> getValues(Grade grade) {
		Set<String> retour = new HashSet<String>();
		for (EnumElement e : this.tagNCost.keySet()) {
			if (e.getGrade().getLevel() <= grade.getLevel())
				retour.add(e.getValue());
		}
		return retour;
	}
}