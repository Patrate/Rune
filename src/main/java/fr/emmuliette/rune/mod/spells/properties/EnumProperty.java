package fr.emmuliette.rune.mod.spells.properties;

import java.util.Map;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public final class EnumProperty extends Property<String> {

	private Map<String, Cost<?>> tagNCost;

	public EnumProperty(String name, String def, Map<String, Cost<?>> tagNCost) {
		super(name, def);
		this.tagNCost = tagNCost;
	}

	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public void setValueInternal(String s) {
		if (tagNCost.containsKey(s))
			super.setValue(s);
		// TODO throw error here
	}

	@Override
	public INBT valueToNBT() {
		return StringNBT.valueOf(this.getValue());
	}

	@Override
	public Cost<?> getCost() {
		return tagNCost.get(this.getValue());
	}

	@Override
	public String nBTtoValue(INBT inbt) {
		return ((StringNBT) inbt).getAsString();
	}
}