package fr.emmuliette.rune.mod.spells.properties;

import java.util.Set;

public enum Grade {

	WOOD("GRADE_WOOD", 0), STONE("GRADE_STONE", 1), IRON("GRADE_IRON", 2), REDSTONE("GRADE_REDSTONE", 3),
	LAPIS("GRADE_LAPIS", 3), GOLD("GRADE_GOLD", 3), DIAMOND("GRADE_DIAMOND", 4), NETHERITE("GRADE_NETHERITE", 4),
	SECRET("GRADE_SECRET", 99), UNKNOWN("GRADE_UNKNOWN", 99);

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

	public static Grade getMax(Set<Grade> keySet, Grade currentGrade) {
		int level = currentGrade.level;
		Grade retour = null;
		for (Grade grade : keySet) {
			if (grade.level > level)
				continue;
			if (retour == null || grade.level > retour.level)
				retour = grade;
		}
		return retour;
	}

	public static Grade getMin(Set<Grade> keySet) {
		Grade retour = null;
		for (Grade grade : keySet) {
			if (retour == null || grade.level < retour.level)
				retour = grade;
		}
		return retour;
	}
}
