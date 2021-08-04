package fr.emmuliette.rune.mod.capabilities.spell;

import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public class GrimoireSpellImpl extends SpellImpl {
	private Grimoire grimoire;
	private String name;
//	private Entity owner;

	public void setGrimoire(Grimoire grimoire) {
		this.grimoire = grimoire;
	}

	private ISpell getGrimoireSpell() {
		return grimoire.getSpell(name);
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

	private static final String SPELL_NAME_KEY = "spell_name"; // , OWNER_KEY = "owner";

	@Override
	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.put(SPELL_NAME_KEY, StringNBT.valueOf(name));
		return retour;
	}

	@Override
	public void fromNBT(INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT cnbt = (CompoundNBT) nbt;
			if (cnbt.contains(SPELL_NAME_KEY)) {
				this.name = cnbt.getString(SPELL_NAME_KEY);
			}
		}
	}

	@Override
	public void sync(ItemStack item) {
		System.out.println("186: Syncing !");
		item.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(c -> this.sync(c));
	}

	@Override
	public void sync(ISpell spell) {
		System.out.println("192: Syncing the spells internal !");
		this.name = spell.getSpell().getName();
	}

	@Override
	public void sync() {
//		if (owner instanceof ServerPlayerEntity) {
//			System.out.println("SENDING");
//			CapabilitySyncHandler.sendTo(new CasterPacket(this.toNBT()), (ServerPlayerEntity) owner);
//		}
	}
}