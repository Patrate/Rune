package fr.emmuliette.rune.mod.spells.properties;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public final class EnumProperty extends Property<String> {
	
	private Map<EnumElement, Supplier<? extends Cost<?>>> tagNCost;

	public EnumProperty(String name, Grade gradeVisible, String def, Map<EnumElement, Supplier<? extends Cost<?>>> tagNCost) {
		super(name, gradeVisible, def);
		this.tagNCost = tagNCost;
	}

	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public void setValueInternal(String s) {
		for(EnumElement e:this.tagNCost.keySet()) {
			if(e.getValue().equals(s)) {
				super.setValueInternal(s);
				return;
			}
		}
		// TODO throw error
	}

	@Override
	public INBT valueToNBT() {
		return StringNBT.valueOf(this.getValue());
	}

	@Override
	public Cost<?> getCost() {
		for(EnumElement e:this.tagNCost.keySet()) {
			if(e.getValue().equals(this.getValue()))
				return tagNCost.get(e).get();
		}
		// TODO throw error
		return Cost.ZERO_COST.get();
	}

	@Override
	public String nBTtoValue(INBT inbt) {
		return ((StringNBT) inbt).getAsString();
	}
	
	public Set<String> getValues(Grade grade) {
		Set<String> retour = new HashSet<String>();
		for(EnumElement e:this.tagNCost.keySet()) {
			if(e.getGrade().getLevel() <= grade.getLevel()) 
				retour.add(e.getValue());
		}
		return retour;
	}
}