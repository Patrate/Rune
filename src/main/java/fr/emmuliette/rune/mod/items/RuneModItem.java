package fr.emmuliette.rune.mod.items;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class RuneModItem extends AdvancedModItem {
	private Class<? extends AbstractSpellComponent> component;
	public RuneModItem(String name, ItemGroup group, Class<? extends AbstractSpellComponent> component) {
		super(name, group);
		this.component = component;
	}
	
	@Override
	public RegistryObject<Item> register() {
		return Registration.ITEMS.register(getName(),
				 getItemSupplier());
	}
	
	@Override
	protected Supplier<? extends Item> getItemSupplier() {
		return () -> new RuneItem(component, new Item.Properties().tab(getGroup()));
	}
}
