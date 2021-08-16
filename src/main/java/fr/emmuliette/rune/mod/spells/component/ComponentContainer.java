package fr.emmuliette.rune.mod.spells.component;

import java.util.List;

public interface ComponentContainer<T extends AbstractSpellComponent> {
	public abstract int getMaxSize();
	public abstract int getSize();
	public abstract boolean canAddChildren(AbstractSpellComponent children);
	public abstract boolean addChildren(AbstractSpellComponent children);
	public abstract List<T> getChildrens();
	public abstract void clearChildrens();
}
