package fr.emmuliette.rune.mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.emmuliette.rune.mod.blocks.ModBlock;
import fr.emmuliette.rune.mod.items.ModItem;

public class ModObjectsManager {
		
	private static ModObjectsManager INSTANCE = new ModObjectsManager();
	private Map<String, AbstractModObject> register;
	
	private ModObjectsManager() {
		register = new HashMap<String, AbstractModObject>();
	}
	
	public static void registerEntity(AbstractModObject e) {
		INSTANCE.register.put(e.getName(), e);
	}
	
	public static Collection<AbstractModObject> getRegister() {
		return INSTANCE.register.values();
	}
	
	public static List<ModBlock> getBlockRegister() {
		List<ModBlock> blockRegister = new ArrayList<ModBlock>();
		for(AbstractModObject entity:INSTANCE.register.values()) {
			if(entity instanceof ModBlock) {
				blockRegister.add((ModBlock) entity);
			}
		}
		return blockRegister;
	}
	public static List<ModItem> getItemRegister() {
		List<ModItem> itemRegister = new ArrayList<ModItem>();
		for(AbstractModObject entity:INSTANCE.register.values()) {
			if(entity instanceof ModItem) {
				itemRegister.add((ModItem) entity);
			}
		}
		return itemRegister;
	}
}
