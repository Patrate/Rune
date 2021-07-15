package fr.emmuliette.rune.mod.spells;

import java.util.HashMap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class RuneProperties {
	private HashMap<String, INBT> properties;

	public RuneProperties() {
		properties = new HashMap<String, INBT>();
	}

	public void setProperty(String key, INBT value) {
		properties.put(key, value);
	}

	public INBT getProperty(String name) {
		return properties.get(name);
	}

	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		for (String key : properties.keySet()) {
			retour.put(key, properties.get(key));
		}
		return retour;
	}

	public static RuneProperties fromNBT(CompoundNBT data) {
		RuneProperties retour = new RuneProperties();
		for (String key : data.getAllKeys()) {
			retour.setProperty(key, data.get(key));
		}
		return retour;
	}
}
