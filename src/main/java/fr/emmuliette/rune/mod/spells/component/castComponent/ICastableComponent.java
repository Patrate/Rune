package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.INBTableComponent;
import fr.emmuliette.rune.mod.spells.component.ISpellComponent;

public interface ICastableComponent extends ISpellComponent, INBTableComponent {
	public boolean specialCast(SpellContext context);
	public boolean canCast(SpellContext context);
	public boolean cast(SpellContext context);
}
