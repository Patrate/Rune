package fr.emmuliette.rune.mod;

import fr.emmuliette.rune.exception.NotAnItemException;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class RuneItemGroup extends ItemGroup {

	public RuneItemGroup(String label) {
		super(label);
	}

	@Override
	public ItemStack makeIcon() {
		try {
			return ModObjects.BLANK_RUNE.getModItem().getDefaultInstance();
		} catch (NotAnItemException e) {
		}
		return null;
	}

}
