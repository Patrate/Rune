package fr.emmuliette.rune.mod.items;

import fr.emmuliette.rune.mod.AbstractModObject;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModItem extends AbstractModObject {
	private RegistryObject<Item> modItem;
	
	public ModItem(String name, ItemGroup group) {
		super(name, group);
		modItem = register();
		
	}
	
	public RegistryObject<Item> register() {
		return Registration.ITEMS.register(getName(),
				() -> new Item(new Item.Properties().tab(getGroup())));
	}
	
	public RegistryObject<Item> getModItem() {
		return modItem;
	}
}
