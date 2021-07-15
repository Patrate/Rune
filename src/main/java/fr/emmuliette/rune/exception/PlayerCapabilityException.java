package fr.emmuliette.rune.exception;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerCapabilityException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerCapabilityException(PlayerEntity e) {
		super(e.getName() + " doesn't have a PlayerCapability capability !");
	}
}
