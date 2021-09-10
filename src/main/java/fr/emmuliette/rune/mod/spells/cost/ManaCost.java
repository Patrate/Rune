package fr.emmuliette.rune.mod.spells.cost;

import java.util.Map;

import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;

public class ManaCost extends Cost<Float> {
	private float value;

	public ManaCost(float value) {
		this(null, value);
	}

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
		if (context.getSocketItem() != null) {
			int damage = context.getSocketItem().getDamageValue();
			damage = (int) (damage + value);
			if (damage >= 0) {
				context.getSocketItem().setDamageValue(damage);
				return true;
			} else
				return false;
		} else {
			try {
				cap.delMana(value);
				return true;
			} catch (NotEnoughManaException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	@Override
	protected boolean internalCanPay(ICaster cap, SpellContext context) {
		if (context.getSocketItem() != null) {
			System.out.println("MAX DAMAGE: " + context.getSocketItem().getMaxDamage() + ", damage value: "
					+ context.getSocketItem().getDamageValue() + "  ("
					+ (context.getSocketItem().getMaxDamage() - context.getSocketItem().getDamageValue()) + " >= "
					+ value + ") ?");
			return (context.getSocketItem().getMaxDamage() - context.getSocketItem().getDamageValue()) >= value;
		}
		return cap.getMana() >= value;
	}
}
