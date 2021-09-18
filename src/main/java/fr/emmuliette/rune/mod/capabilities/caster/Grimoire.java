package fr.emmuliette.rune.mod.capabilities.caster;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellImpl;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public class Grimoire {
//	private Map<Integer, ISpell> spellList;
	List<ISpell> spellList;

	public Grimoire() {
		spellList = new ArrayList<ISpell>();
	}

//	private void expeditiveSpellRemoval(int spellId) {
//	}

	public boolean addSpell(ISpell spell) {
		addSpellInternal(spell);
		return true;
	}

	public void moveSpell(Integer a, Integer b) {
		if (a < 0 || b < 0 || a >= spellList.size() || b >= spellList.size())
			return;
		ISpell tmpA = this.spellList.get(b);
		this.spellList.set(b, this.spellList.get(a));
		this.spellList.set(a, tmpA);
	}

	public boolean removeSpell(Integer spellId) {
		removeSpellInternal(spellId);
		return true;
	}

	public ISpell getSpell(Integer spellId) {
		if (spellId < 0 || spellId >= spellList.size())
			return null;
		return spellList.get(spellId);
	}

	public List<ISpell> getSpells() {
		return spellList;
	}

	public int grimoireSize() {
		return spellList.size();
	}

//	public ItemStack getItem(Integer spellId) {
//		ISpell spell = this.getSpell(spellId);
//		if (spell == null) {
//			System.out.println("Spell is null for id " + spellId);
//			return ItemStack.EMPTY;
//		}
//		if (spell.getSpell() == null) {
//			expeditiveSpellRemoval(spellId);
//			System.out.println("Spell.getSpell() is null for id " + spellId);
//			return ItemStack.EMPTY;
//		}
//		return SpellItem.buildSpellItem(spell.getSpell(), ItemType.SPELL);
//	}

	private void removeSpellInternal(int spellId) {
		spellList.remove(spellId);
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
						ISpell spell = new SpellImpl();
						spell.fromNBT((CompoundNBT) spellNBT);
						retour.addSpellInternal(spell);
					}
				}
			}
		}
		return retour;
	}

	public void sync(Grimoire other) {
		spellList.clear();
		spellList.addAll(other.spellList);
	}
}
