package fr.emmuliette.rune.mod.spells.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.spells.capability.sync.SpellContainerListener;
import fr.emmuliette.rune.mod.spells.capability.sync.global.CapabilityContainerListenerManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class SpellCapability implements ICapabilitySerializable<CompoundNBT> {
	public static final String SPELL_CAPABILITY_NAME = "spell_capability";

	@CapabilityInject(ISpell.class)
	public static final Capability<ISpell> SPELL_CAPABILITY = null;
	private LazyOptional<ISpell> instance = LazyOptional.of(SPELL_CAPABILITY::getDefaultInstance);

	public static void register()
    {
        CapabilityManager.INSTANCE.register(ISpell.class, new SpellStorage(), SpellImpl::new);
		CapabilityContainerListenerManager.registerListenerFactory(SpellContainerListener::new);
    }
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return SPELL_CAPABILITY.orEmpty(cap, instance);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) SPELL_CAPABILITY.getStorage().writeNBT(SPELL_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		SPELL_CAPABILITY.getStorage().readNBT(SPELL_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
	
	
	public static LazyOptional<ISpell> getNbt(final ItemStack itemStack) {
		return itemStack.getCapability(SPELL_CAPABILITY);
	}
	
	@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID)
	public static class EventHandler {
		/**
		 * Update the {@link ISPell} of the player's held item when they right click.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerInteract(final PlayerInteractEvent.RightClickItem event) {
			final ItemStack itemStack = event.getItemStack();

			itemStack.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(new NonNullConsumer<ISpell>() {
				@Override
				public void accept(ISpell iSpell) {
					iSpell.sync();
				}
			});
		}
	}
}