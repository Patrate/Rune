package fr.emmuliette.rune.mod.spells.cost;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import net.minecraft.item.Item;

public abstract class Cost<T> {
	private Map<Class<? extends Cost<?>>, Cost<?>> internalCost;

	public static Supplier<? extends Cost<?>> ZERO_COST = () -> new ManaCost(0f);

	@SuppressWarnings("unchecked")
	protected Cost(Map<Class<? extends Cost<?>>, Cost<?>> internalCost) {
		if (internalCost == null) {
			this.internalCost = new HashMap<Class<? extends Cost<?>>, Cost<?>>();
		} else {
			this.internalCost = internalCost;
		}
		this.internalCost.put((Class<? extends Cost<?>>) this.getClass(), this);
	}

	@SuppressWarnings("unchecked")
	public void add(Cost<?> other) {
		for (Class<? extends Cost<?>> clazz : internalCost.keySet()) {
			if (clazz == other.getClass()) {
				internalCost.get(clazz).internalAdd(other);
				return;
			}
		}
		internalCost.put((Class<? extends Cost<?>>) other.getClass(), other);
	}

	public void remove(Cost<?> other) {
		for (Class<? extends Cost<?>> clazz : internalCost.keySet()) {
			if (clazz == other.getClass()) {
				internalCost.get(clazz).internalRemove(other);
				return;
			}
		}
	}

	public float getManaCost() {
		ManaCost mCost = (ManaCost) internalCost.get(ManaCost.class);
		if (mCost == null)
			return 0f;
		return Math.max(0f, mCost.getCost());
	}

	public float getLifeCost() {
		LifeCost mCost = (LifeCost) internalCost.get(LifeCost.class);
		if (mCost == null)
			return 0f;
		return Math.max(0f, mCost.getCost());
	}

	public Map<Item, Integer> getItemCost() {
		ItemCost mCost = (ItemCost) internalCost.get(ItemCost.class);
		if (mCost == null)
			return new HashMap<Item, Integer>();
		return mCost.getCost();
	}

	public boolean canPay(ICaster cap, SpellContext context) {
		for (Class<? extends Cost<?>> clazz : internalCost.keySet()) {
			if (!(internalCost.get(clazz).internalCanPay(cap, context)))
				return false;
		}
		return true;
	}

	public boolean payCost(ICaster cap, SpellContext context) {
		for (Class<? extends Cost<?>> clazz : internalCost.keySet()) {
			if (!(internalCost.get(clazz).internalPayCost(cap, context)))
				return false;
		}
		return true;
	}

	protected abstract void internalAdd(Cost<?> other);

	protected abstract void internalRemove(Cost<?> other);

	public abstract T getCost();

	protected abstract boolean internalCanPay(ICaster cap, SpellContext context);

	protected abstract boolean internalPayCost(ICaster cap, SpellContext context);

	@Override
	public String toString() {
		String retour = "";
		for (Class<? extends Cost<?>> clazz : internalCost.keySet()) {
			retour += clazz.getSimpleName() + ": " + internalCost.get(clazz).getCost().toString() + "\t\t";
		}
		return retour;
	}
}
