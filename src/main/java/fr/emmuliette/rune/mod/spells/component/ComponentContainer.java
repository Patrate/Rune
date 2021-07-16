package fr.emmuliette.rune.mod.spells.component;

import java.util.List;

public interface ComponentContainer<C extends AbstractSpellComponent> {
	public abstract int getMaxSize();
	public abstract int getSize();
	public abstract boolean canAddChildren(AbstractSpellComponent children);
	public abstract void addChildren(C children);
	public abstract List<C> getChildrens();
}
