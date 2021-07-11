package fr.emmuliette.rune.mod.spells.capability.currency;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CurrencyCapability implements ICapabilitySerializable<IntNBT> {

	@CapabilityInject(ICurrency.class)
	public static final Capability<ICurrency> CURRENCY_CAPABILITY = null;
	private LazyOptional<ICurrency> instance = LazyOptional.of(CURRENCY_CAPABILITY::getDefaultInstance);

	public static void register()
    {
        CapabilityManager.INSTANCE.register(ICurrency.class, new CurrencyStorage(), Currency::new);
    }
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return CURRENCY_CAPABILITY.orEmpty(cap, instance);
	}

	@Override
	public IntNBT serializeNBT() {
		return (IntNBT) CURRENCY_CAPABILITY.getStorage().writeNBT(CURRENCY_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(IntNBT nbt) {
		CURRENCY_CAPABILITY.getStorage().readNBT(CURRENCY_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}