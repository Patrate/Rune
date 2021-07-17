package fr.emmuliette.rune.mod.spells.capability.sync;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import fr.emmuliette.rune.mod.spells.capability.ISpell;
import fr.emmuliette.rune.mod.spells.capability.SpellCapability;
import fr.emmuliette.rune.mod.spells.capability.sync.global.BulkUpdateContainerCapabilityMessage;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

public class BulkUpdateSpellMessage extends BulkUpdateContainerCapabilityMessage<ISpell, CompoundNBT> {
	public BulkUpdateSpellMessage(@Nullable final Direction facing, final int windowID,
			final NonNullList<ItemStack> items) {
		super(SpellCapability.SPELL_CAPABILITY, facing, windowID, items,
				ISpellSyncFunction::getISpellNbt);
	}

	private BulkUpdateSpellMessage(@Nullable final Direction facing, final int windowID,
			final Int2ObjectMap<CompoundNBT> capabilityData) {
		super(SpellCapability.SPELL_CAPABILITY, facing, windowID, capabilityData);
	}

	public static BulkUpdateSpellMessage decode(final PacketBuffer buffer) {
		return BulkUpdateContainerCapabilityMessage.<ISpell, CompoundNBT, BulkUpdateSpellMessage>decode(
				buffer,
				ISpellSyncFunction::decodeISpellNbt,
				BulkUpdateSpellMessage::new
		);
	}

	public static void encode(final BulkUpdateSpellMessage message, final PacketBuffer buffer) {
		BulkUpdateContainerCapabilityMessage.encode(message, buffer, ISpellSyncFunction::encodeISpellNbt);
	}

	public static void handle(final BulkUpdateSpellMessage message, final Supplier<NetworkEvent.Context> ctx) {
		BulkUpdateContainerCapabilityMessage.handle(message, ctx, ISpellSyncFunction::applyNbtToSpell);
	}
}