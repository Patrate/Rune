package fr.emmuliette.rune.mod.packets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class SocketCapability implements ICapabilitySerializable<CompoundNBT> {
	public static final String SOCKET_CAPABILITY_NAME = "socket_capability";

	@CapabilityInject(ISocket.class)
	public static final Capability<ISocket> SOCKET_CAPABILITY = null;
	private LazyOptional<ISocket> instance = LazyOptional.of(SOCKET_CAPABILITY::getDefaultInstance);

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return SOCKET_CAPABILITY.orEmpty(cap, instance);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) SOCKET_CAPABILITY.getStorage().writeNBT(SOCKET_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		SOCKET_CAPABILITY.getStorage().readNBT(SOCKET_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}