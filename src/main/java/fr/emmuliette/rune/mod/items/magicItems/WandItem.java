package fr.emmuliette.rune.mod.items.magicItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;

public class WandItem extends MagicTieredItem {

	public WandItem(ItemTier tier, Item.Properties prop) {
		super(tier, prop);
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return false;
	}
}
