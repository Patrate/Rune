package fr.emmuliette.rune.setup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {

	public static final RegistryObject<Item> BLANK_RUNE = Registration.ITEMS.register("blank_rune",
			() -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> PROJECTILE_RUNE = Registration.ITEMS.register("projectile_rune",
			() -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> FIRE_RUNE = Registration.ITEMS.register("fire_rune",
			() -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));

	static void register() {

	}

}
