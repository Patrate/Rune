package fr.emmuliette.rune.mod.items.magicItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.UseAction;

public class WandItem extends MagicTieredItem {

	public WandItem(ItemTier tier, Item.Properties prop) {
		super(tier, prop);
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack item, ItemStack p_82789_2_) {
		return false;
	}
	
	@Override
	public UseAction getUseAnimation(ItemStack item) {
		return UseAction.SPEAR;
	}
}
