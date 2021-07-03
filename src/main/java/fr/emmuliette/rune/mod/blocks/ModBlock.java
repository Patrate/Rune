package fr.emmuliette.rune.mod.blocks;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.AbstractModObject;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModBlock extends AbstractModObject {
	private RegistryObject<? extends Block> modBlock;
	private Supplier<? extends Block> properties;
	
	public ModBlock(String name, ItemGroup group, Supplier<? extends Block> properties) {
		super(name, group);
		this.properties = properties;
		modBlock = register();
	}

	public RegistryObject<? extends Block> register() {
		RegistryObject<? extends Block> ret = Registration.BLOCKS.register(getName(), properties);
		Registration.ITEMS.register(getName(), () -> new BlockItem(ret.get(), new Item.Properties().tab(getGroup())));
		return ret;
	}

	public RegistryObject<? extends Block> getModBlock() {
		return modBlock;
	}
	
	public Supplier<? extends Block> getProperties(){
		return properties;
	}
}
