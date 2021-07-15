package fr.emmuliette.rune.mod.player.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapability implements ICapabilitySerializable<CompoundNBT> {
	public static final String PLAYER_CAPABILITY_NAME = "player_capability";

	@CapabilityInject(IPlayer.class)
	public static final Capability<IPlayer> PLAYER_CAPABILITY = null;
	private LazyOptional<IPlayer> instance = LazyOptional.of(PLAYER_CAPABILITY::getDefaultInstance);

	public PlayerCapability(Entity owner) {
		this.instance.ifPresent(c -> c.setOwner(owner));
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlayer.class, new PlayerStorage(), PlayerImpl::new);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return PLAYER_CAPABILITY.orEmpty(cap, instance);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) PLAYER_CAPABILITY.getStorage().writeNBT(PLAYER_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		PLAYER_CAPABILITY.getStorage().readNBT(PLAYER_CAPABILITY,
				instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}