package fr.emmuliette.rune;

import java.util.HashSet;
import java.util.Set;

public class DebugUtils {
	private static Set<String> printed = new HashSet<String>();
	
	public static void debug(String s) {
		System.out.println("[DEBUG] " + s);
	}
	
	public static void warn(String s) {
		System.out.println("[WARN] " + s);
	}
	
	public static void print(String s) {
		System.out.println("[INFO] " + s);
	}
	
	public static void printOnce(String s) {
		if(!printed.contains(s)) {
			print(s);
			printed.add(s);
		}
	}
	
}
