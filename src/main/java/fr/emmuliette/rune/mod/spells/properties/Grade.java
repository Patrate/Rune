package fr.emmuliette.rune.mod.spells.properties;

import java.util.Set;

public enum Grade {

	WOOD(0), STONE(1), IRON(2), GOLD(3), DIAMOND(4), NETHERITE(5), SECRET(99), UNKNOWN(-1);

	private int level;

	private Grade(int level) {
		this.level = level;
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
		if(retour == null)
			retour = getMin(keySet);
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
