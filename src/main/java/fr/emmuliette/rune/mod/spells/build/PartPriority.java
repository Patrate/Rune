package fr.emmuliette.rune.mod.spells.build;

public enum PartPriority {
	POSTCAST(-1), EFFECT(0), CAST(1), CASTMOD(2), MANAMOD(3);
	public int level;
	
	private PartPriority(int lvl) {
		level = lvl;
	}
}
