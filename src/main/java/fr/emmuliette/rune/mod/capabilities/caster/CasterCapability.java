package fr.emmuliette.rune.mod.capabilities.caster;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CasterCapability implements ICapabilitySerializable<CompoundNBT> {
	public static final String CASTER_CAPABILITY_NAME = "caster_capability";

	@CapabilityInject(ICaster.class)
	public static final Capability<ICaster> CASTER_CAPABILITY = null;
	private LazyOptional<ICaster> instance = LazyOptional.of(CASTER_CAPABILITY::getDefaultInstance);

	public CasterCapability(Entity owner) {
		this.instance.ifPresent(c -> c.setOwner(owner));
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return CASTER_CAPABILITY.orEmpty(cap, instance);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) CASTER_CAPABILITY.getStorage().writeNBT(CASTER_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		CASTER_CAPABILITY.getStorage().readNBT(CASTER_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}