package fr.emmuliette.rune.exception;

import net.minecraft.entity.Entity;

public class CasterCapabilityException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CasterCapabilityException(Entity e) {
		super(e.getName() + " doesn't have a CasterCapability capability !");
	}
}
