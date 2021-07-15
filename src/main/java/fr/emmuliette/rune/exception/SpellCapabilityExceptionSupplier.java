package fr.emmuliette.rune.exception;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.NonNullSupplier;

public class SpellCapabilityExceptionSupplier implements NonNullSupplier<SpellCapabilityException> {
	private ItemStack item;
	
	public SpellCapabilityExceptionSupplier(ItemStack item) {
		this.item = item;
	}
	@Override
	public SpellCapabilityException get() {
		return new SpellCapabilityException(item);
	}
}
