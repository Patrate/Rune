package fr.emmuliette.rune.mod.spells.cost;

import java.util.Map;

import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;

public class ManaCost extends Cost<Float> {
	private float value;

	public ManaCost(Map<Class<? extends Cost<?>>, Cost<?>> internalCost, float value) {
		super(internalCost);
		this.value = value;
	}

	@Override
	protected void internalAdd(Cost<?> other) {
		if (!(other instanceof ManaCost))
			return;
		ManaCost mOther = (ManaCost) other;
		this.value += mOther.value;
	}

	@Override
	protected void internalRemove(Cost<?> other) {
		if (!(other instanceof ManaCost))
			return;
		ManaCost mOther = (ManaCost) other;
		this.value -= mOther.value;
	}

	@Override
	public Float getCost() {
		return value;
	}

	@Override
	protected boolean internalPayCost(ICaster cap, SpellContext context) {
		try {
			cap.delMana(value);
			return true;
		} catch (NotEnoughManaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean internalCanPay(ICaster cap, SpellContext context) {
		return cap.getMana() >= value;
	}
}
