package fr.emmuliette.rune.mod.spells.cost;

import java.util.Map;

import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;

public class LifeCost extends Cost<Float> {
	private float value;

	public LifeCost(Map<Class<? extends Cost<?>>, Cost<?>> internalCost, float value) {
		super(internalCost);
		this.value = value;
	}

	@Override
	protected void internalAdd(Cost<?> other) {
		if (!(other instanceof LifeCost))
			return;
		LifeCost mOther = (LifeCost) other;
		this.value += mOther.value;
	}

	@Override
	protected void internalRemove(Cost<?> other) {
		if (!(other instanceof LifeCost))
			return;
		LifeCost mOther = (LifeCost) other;
		this.value -= mOther.value;
	}

	@Override
	public Float getCost() {
		return value;
	}

	@Override
	protected boolean internalPayCost(ICaster cap, SpellContext context) {
		context.getCaster().setHealth(context.getCaster().getHealth() - value);
		return true;
	}

	@Override
	protected boolean internalCanPay(ICaster cap, SpellContext context) {
		return context.getCaster().getHealth() > value;
	}
}
