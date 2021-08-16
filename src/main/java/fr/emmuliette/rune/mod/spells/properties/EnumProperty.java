package fr.emmuliette.rune.mod.spells.properties;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public final class EnumProperty extends Property<String> {

	private Map<String, Supplier<? extends Cost<?>>> tagNCost;

	public EnumProperty(String name, String def, Map<String, Supplier<? extends Cost<?>>> tagNCost) {
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
			super.setValueInternal(s);
		// TODO throw error here
	}

	@Override
	public INBT valueToNBT() {
		return StringNBT.valueOf(this.getValue());
	}

	@Override
	public Cost<?> getCost() {
		return tagNCost.get(this.getValue()).get();
	}

	@Override
	public String nBTtoValue(INBT inbt) {
		return ((StringNBT) inbt).getAsString();
	}
	
	public Set<String> getValues() {
		return tagNCost.keySet();
	}
}