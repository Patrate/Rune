package fr.emmuliette.rune;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class RandomNameUtils {
	private static final List<String> list = Arrays.asList(new String[] {"Boule de feu", "Fixorizator", "Magificicus", "Demoniskito", "Bouledefeu0z", "VEGETA", "SHARINGAN", "pouet"});
	public static String getName() {
		Collections.shuffle(list);
		return list.get(0);
	}
}
