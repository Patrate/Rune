package fr.emmuliette.rune.mod.spells.capability.sync;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import fr.emmuliette.rune.mod.spells.capability.ISpell;
import fr.emmuliette.rune.mod.spells.capability.SpellCapability;
import fr.emmuliette.rune.mod.spells.capability.sync.global.UpdateContainerCapabilityMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdateSpellMessage extends UpdateContainerCapabilityMessage<ISpell, CompoundNBT> {
	public UpdateSpellMessage(@Nullable final Direction facing, final int windowID, final int slotNumber,
			final ISpell iSpell) {
		super(SpellCapability.SPELL_CAPABILITY, facing, windowID, slotNumber, iSpell,
				ISpellSyncFunction::getISpellNbt);
	}

	private UpdateSpellMessage(@Nullable final Direction facing, final int windowID, final int slotNumber,
			final CompoundNBT nbt) {
		super(SpellCapability.SPELL_CAPABILITY, facing, windowID, slotNumber, nbt);
	}

	public static UpdateSpellMessage decode(final PacketBuffer buffer) {
		return UpdateSpellMessage.<ISpell, CompoundNBT, UpdateSpellMessage>decode(buffer, ISpellSyncFunction::decodeISpellNbt,
				UpdateSpellMessage::new);
	}

	public static void encode(final UpdateSpellMessage message, final PacketBuffer buffer) {
		UpdateSpellMessage.encode(message, buffer, ISpellSyncFunction::encodeISpellNbt);
	}

	public static void handle(final UpdateSpellMessage message, final Supplier<NetworkEvent.Context> ctx) {
		UpdateSpellMessage.handle(message, ctx, ISpellSyncFunction::applyNbtToSpell);
	}
}