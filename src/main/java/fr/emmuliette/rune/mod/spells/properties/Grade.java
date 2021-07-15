package fr.emmuliette.rune.mod.spells.properties;

public enum Grade {

	WOOD("GRADE_WOOD", 0), STONE("GRADE_STONE", 1), IRON("GRADE_IRON", 2), REDSTONE("GRADE_REDSTONE", 3),
	LAPIS("GRADE_LAPIS", 3), GOLD("GRADE_GOLD", 3), DIAMOND("GRADE_DIAMOND", 4), NETHERITE("GRADE_NETHERITE", 4);

	private String key;
	private int level;

	private Grade(String key, int level) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public int getLevel() {
		return level;
	}
}
