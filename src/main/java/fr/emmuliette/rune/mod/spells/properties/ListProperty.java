package fr.emmuliette.rune.mod.spells.properties;

import java.util.Map;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public final class ListProperty extends Property<String> {

	private Map<String, Integer> tagNCost;

	public ListProperty(String name, String def, Map<String, Integer> tagNCost) {
		super(name, def);
		this.tagNCost = tagNCost;
	}

	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public void setValue(String s) {
		if (tagNCost.containsKey(s))
			super.setValue(s);
		// TODO throw error here
	}

	@Override
	public INBT getValueAsNBT() {
		return StringNBT.valueOf(this.getValue());
	}

	@Override
	public float getManaCost() {
		return tagNCost.get(this.getValue());
	}
}