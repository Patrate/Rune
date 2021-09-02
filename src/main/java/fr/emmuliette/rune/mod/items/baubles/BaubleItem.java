package fr.emmuliette.rune.mod.items.baubles;

import fr.emmuliette.rune.mod.items.magicItems.MagicItem;
import lazy.baubles.api.bauble.BaubleType;
import lazy.baubles.api.bauble.IBauble;
import net.minecraft.item.ItemStack;

public class BaubleItem extends MagicItem implements IBauble {
	BaubleType type;

	public BaubleItem(MagicItemProperties prop, BaubleType type) {
		super(prop);
		this.type = type;
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return type;
	}

}
