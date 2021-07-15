package fr.emmuliette.rune.mod.spells.component;

public interface ComponentContainer<C extends AbstractSpellComponent> {
	public abstract int getMaxSize();
	public abstract int getSize();
	public abstract boolean canAddChildren(AbstractSpellComponent children);
	@SuppressWarnings("unchecked")
	public default void addChildren(AbstractSpellComponent children) {
		if(canAddChildren(children)) {
			_addChildren((C) children);
		}
	}
	public abstract void _addChildren(C children);
}
