package fr.emmuliette.rune.mod;

import fr.emmuliette.rune.setup.Configuration;
import net.minecraft.item.ItemGroup;

public abstract class AbstractModObject {
	private String name;
	private ItemGroup group;
	
	public AbstractModObject(String name, ItemGroup group) {
		this.name = name;
		this.group = group;
		
		ModObjectsManager.registerEntity(this);
	}

	public boolean isActive() {
		return Configuration.Server.inactiveItem.contains(name);
	}
	
	public String getName() {
		return name;
	}
	
	public ItemGroup getGroup() {
		return group;
	}
}
