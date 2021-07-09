package fr.emmuliette.rune.mod.items;

import java.util.function.Supplier;

import fr.emmuliette.rune.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public abstract class AdvancedModItem extends ModItem {
	public AdvancedModItem(String name, ItemGroup group) {
		super(name, group);
	}
	
	@Override
	public RegistryObject<Item> register() {
		return Registration.ITEMS.register(getName(),
				 getItemSupplier());
	}
	
	protected abstract Supplier<? extends Item> getItemSupplier();
}
