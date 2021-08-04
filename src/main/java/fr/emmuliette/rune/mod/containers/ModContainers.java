package fr.emmuliette.rune.mod.containers;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.gui.grimoire.GrimoireContainer;
import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingContainer;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ModContainers {
	

	public static final RegistryObject<ContainerType<SpellBindingContainer>> SPELLBINDING = register("spellbinding",
			() -> IForgeContainerType.create(SpellBindingContainer::new));
	
	public static final RegistryObject<ContainerType<GrimoireContainer>> GRIMOIRE = register("grimoire",
			() -> IForgeContainerType.create(GrimoireContainer::new));

	private static <T extends Container> RegistryObject<ContainerType<T>> register(String name,
			Supplier<ContainerType<T>> supplier) {
		return Registration.CONTAINER.register(name, supplier);
	}

	public static void register() {
	}

}
