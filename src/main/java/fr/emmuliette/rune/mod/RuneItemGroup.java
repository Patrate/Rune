package fr.emmuliette.rune.mod;

import fr.emmuliette.rune.mod.items.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class RuneItemGroup extends ItemGroup {

	public RuneItemGroup(String label) {
		super(label);
	}

	@Override
	public ItemStack makeIcon() {
		return ModItems.PROJECTILE_RUNE.getItem().getDefaultInstance();
	}

}
