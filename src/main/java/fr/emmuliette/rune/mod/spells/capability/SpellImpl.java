package fr.emmuliette.rune.mod.spells.capability;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class SpellImpl implements ISpell {
	private Spell spell;
	private int cooldown;

	@Override
	public Spell getSpell() {
		return this.spell;
	}

	@Override
	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	@Override
	public boolean isCooldown() {
		return cooldown > 0;
	}

	@Override
	public void setCooldown(int cd) {
		int previous = this.cooldown;
		this.cooldown = cd;
		if ((previous == 0 && cd != 0) || previous > 0 && cd == 0) {
			sync();
		}
	}

	@Override
	public int getCooldown() {
		return cooldown;
	}

	private static final String SPELL_KEY = "spell", COOLDOWN_KEY = "cooldown";

	@Override
	public CompoundNBT toNBT() {
		if(spell == null) {
			return null;
		}
		CompoundNBT retour = new CompoundNBT();
		retour.put(SPELL_KEY, spell.toNBT());
		retour.put(COOLDOWN_KEY, IntNBT.valueOf(cooldown));
		return retour;
	}

	@Override
	public void fromNBT(INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT cnbt = (CompoundNBT) nbt;

			// SPELL
			if (cnbt.contains(SPELL_KEY)) {
				try {
					if (this.spell == null) {
						this.spell = Spell.fromNBT(cnbt.getCompound(SPELL_KEY));
					} else {
						updateFromNBT(cnbt);
					}
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
						| SecurityException | RunePropertiesException e) {
					e.printStackTrace();
				}
			}

			// COOLDOWN
			if (cnbt.contains(COOLDOWN_KEY)) {
				this.cooldown = cnbt.getInt(COOLDOWN_KEY);
			}
		}
	}

	private void updateFromNBT(CompoundNBT nbt)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, RunePropertiesException {
		Spell spell = this.spell;
		Spell other = Spell.fromNBT(nbt.getCompound(SPELL_KEY));
		spell.sync(other);
	}

	@Override
	public void sync(ItemStack container) {
		container.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(c -> this.sync(c));
	}

	@Override
	public void sync(ISpell ispell) {
		this.spell = ispell.getSpell();
		this.cooldown = ispell.getCooldown();
	}

	@Override
	public void sync() {
		// TODO
		/*
		 * if (owner instanceof ServerPlayerEntity) { PlayerHandler.sendTo(new
		 * PlayerPacket(this.toNBT()), (ServerPlayerEntity) owner); }
		 */
	}
}