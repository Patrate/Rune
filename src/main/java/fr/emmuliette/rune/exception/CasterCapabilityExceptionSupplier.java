package fr.emmuliette.rune.exception;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.util.NonNullSupplier;

public class CasterCapabilityExceptionSupplier implements NonNullSupplier<CasterCapabilityException> {
	private LivingEntity player;
	
	public CasterCapabilityExceptionSupplier(LivingEntity livingEntity) {
		this.player = livingEntity;
	}
	@Override
	public CasterCapabilityException get() {
		return new CasterCapabilityException(player);
	}
}
