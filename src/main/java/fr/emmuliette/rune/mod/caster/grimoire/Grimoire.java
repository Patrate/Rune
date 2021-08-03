package fr.emmuliette.rune.mod.caster.grimoire;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.capability.ISpell;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public class Grimoire {
	private List<ISpell> spellList;

	public Grimoire() {
		spellList = new ArrayList<ISpell>();
	}

	public boolean addSpell(ISpell spell) {
		addSpellInternal(spell);
		return true;
	}
	
	public boolean removeSpell(int id) {
		removeSpellInternal(id);
		return true;
	}
	
	public ISpell getSpell(int id) {
		return spellList.get(id);
	}
	
	public List<ISpell> getSpells(){
		return spellList;
	}
	
	private void removeSpellInternal(int id) {
		spellList.remove(id);
	}
	private void addSpellInternal(ISpell spell) {
		spellList.add(spell);
	}

	private static final String SPELL_LIST_KEY = "spell_list";

	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		ListNBT sList = new ListNBT();
		for (ISpell spell : spellList) {
			sList.add(spell.toNBT());
		}
		retour.put(SPELL_LIST_KEY, sList);
		return retour;
	}

	public static Grimoire fromNBT(INBT inbt)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, RunePropertiesException {
		Grimoire retour = new Grimoire();
		if (inbt != null && inbt instanceof CompoundNBT) {
			if (((CompoundNBT) inbt).contains(SPELL_LIST_KEY)) {
				ListNBT sList = (ListNBT) ((CompoundNBT) inbt).get(SPELL_LIST_KEY);
				for (INBT spellNBT : sList) {
					if (spellNBT instanceof CompoundNBT) {
						Spell.fromNBT((CompoundNBT) spellNBT);
					}
				}
			}
		}
		return retour;
	}
}
