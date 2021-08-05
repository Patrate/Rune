package fr.emmuliette.rune.mod.items.wand;

import net.minecraft.item.ItemStack;

public interface ManaSource {
	float getMana(ItemStack item);

	float getMaxMana(ItemStack item);

	boolean useMana(ItemStack item, float amount);

	int getChargeSpeed(ItemStack item);
}
