package fr.emmuliette.rune.exception;

import net.minecraft.entity.LivingEntity;

public class CasterCapabilityException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CasterCapabilityException(LivingEntity e) {
		super(e.getName() + " doesn't have a CasterCapability capability !");
	}
}
