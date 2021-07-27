package fr.emmuliette.rune.exception;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.NonNullSupplier;

public class CasterCapabilityExceptionSupplier implements NonNullSupplier<CasterCapabilityException> {
	private Entity player;
	
	public CasterCapabilityExceptionSupplier(Entity livingEntity) {
		this.player = livingEntity;
	}
	@Override
	public CasterCapabilityException get() {
		return new CasterCapabilityException(player);
	}
}
