package fr.emmuliette.rune.mod.spells.capability.sync;

import fr.emmuliette.rune.mod.spells.capability.ISpell;
import fr.emmuliette.rune.mod.spells.capability.SpellCapability;
import fr.emmuliette.rune.mod.spells.capability.sync.global.CapabilityContainerListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SpellContainerListener extends CapabilityContainerListener<ISpell> {

	public SpellContainerListener(final ServerPlayerEntity player) {
		super(player, SpellCapability.SPELL_CAPABILITY, null);
	}

	/**
	 * Create an instance of the bulk update message.
	 *
	 * @param windowID The window ID of the Container
	 * @param items    The items list
	 * @return The bulk update message
	 */
	@Override
	protected BulkUpdateSpellMessage createBulkUpdateMessage(final int windowID, final NonNullList<ItemStack> items) {
		return new BulkUpdateSpellMessage(null, windowID, items);
	}

	/**
	 * Create an instance of the single update message.
	 *
	 * @param windowID    The window ID of the Container
	 * @param slotNumber  The slot's index in the Container
	 * @param iSpell The capability handler instance
	 * @return The single update message
	 */
	@Override
	protected UpdateSpellMessage createSingleUpdateMessage(final int windowID, final int slotNumber, final ISpell iSpell) {
		return new UpdateSpellMessage(null, windowID, slotNumber, iSpell);
	}
}