package fr.emmuliette.rune.exception;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.NonNullSupplier;

public class PlayerCapabilityExceptionSupplier implements NonNullSupplier<PlayerCapabilityException> {
	private PlayerEntity player;
	
	public PlayerCapabilityExceptionSupplier(PlayerEntity player) {
		this.player = player;
	}
	@Override
	public PlayerCapabilityException get() {
		return new PlayerCapabilityException(player);
	}
}
