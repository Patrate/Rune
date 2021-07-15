package fr.emmuliette.rune.mod.spells.capability;

import fr.emmuliette.rune.mod.spells.Spell;

public class SpellImpl implements ISpell {
    private Spell spell;

    @Override
    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public void setSpell(Spell spell) {
        this.spell = spell;
    }
}