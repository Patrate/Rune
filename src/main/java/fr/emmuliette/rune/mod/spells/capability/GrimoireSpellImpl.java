package fr.emmuliette.rune.mod.spells.capability;

import fr.emmuliette.rune.mod.caster.grimoire.Grimoire;
import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class GrimoireSpellImpl extends SpellImpl {
	private Grimoire grimoire;
	private int id;

	public void setGrimoire(Grimoire grimoire) {
		this.grimoire = grimoire;
	}

	private ISpell getGrimoireSpell() {
		return grimoire.getSpell(id);
	}

	@Override
	public Spell getSpell() {
		return getGrimoireSpell().getSpell();
	}

	@Override
	public void setSpell(Spell spell) {
		getGrimoireSpell().setSpell(spell);
	}

	@Override
	public boolean isCooldown() {
		return getGrimoireSpell().isCooldown();
	}

	@Override
	public void setCooldown(int cd) {
		getGrimoireSpell().setCooldown(cd);
	}

	@Override
	public int getCooldown() {
		return getGrimoireSpell().getCooldown();
	}

	private static final String SPELL_ID_KEY = "spell_id";

	@Override
	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.put(SPELL_ID_KEY, IntNBT.valueOf(id));
		return retour;
	}

	@Override
	public void fromNBT(INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT cnbt = (CompoundNBT) nbt;
			// COOLDOWN
			if (cnbt.contains(SPELL_ID_KEY)) {
				this.id = cnbt.getInt(SPELL_ID_KEY);
			}
		}
	}

	@Override
	public void sync(ItemStack itemstack) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sync(ISpell ispell) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sync() {
		// TODO Auto-generated method stub
	}
}