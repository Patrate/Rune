package fr.emmuliette.rune.mod.spells.capability.spell;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class SpellCapability implements ICapabilitySerializable<CompoundNBT> {
	public static final String SPELL_CAPABILITY_NAME = "spell_capability";

	@CapabilityInject(ISpell.class)
	public static final Capability<ISpell> SPELL_CAPABILITY = null;
	private LazyOptional<ISpell> instance = LazyOptional.of(SPELL_CAPABILITY::getDefaultInstance);

	public static void register()
    {
        CapabilityManager.INSTANCE.register(ISpell.class, new SpellStorage(), SpellImpl::new);
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
}