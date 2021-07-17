package fr.emmuliette.rune.mod.spells.capability.sync;

import fr.emmuliette.rune.mod.spells.capability.ISpell;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class ISpellSyncFunction {
	static CompoundNBT getISpellNbt(final ISpell spell) {
		return spell.toNBT();
	}

	static CompoundNBT decodeISpellNbt(final PacketBuffer buffer) {
		return buffer.readNbt();
	}

	static void encodeISpellNbt(final CompoundNBT spellNbt, final PacketBuffer buffer) {
		buffer.writeNbt(spellNbt);
	}

	static void applyNbtToSpell(final ISpell spell, final CompoundNBT spellNbt) {
		spell.fromNBT(spellNbt);
	}
}