package fr.emmuliette.rune.mod.spells.tags;

import java.util.HashSet;
import java.util.Set;

public class BuildTag extends Tag {
	public static BuildTag MANA_MOD = new BuildTag();
	public static BuildTag CAST_MOD = new BuildTag();
	public static BuildTag CAST = new BuildTag();
	public static BuildTag EFFECT = new BuildTag();
	public static BuildTag POST_CAST = new BuildTag();
	public static BuildTag NULL = new BuildTag();
	
	private Set<BuildTag> allowedNext;
	
	private BuildTag() {
		allowedNext = new HashSet<BuildTag>();
	}
	
	static void init() {
		MANA_MOD.init(MANA_MOD, CAST_MOD, CAST);
		CAST_MOD.init(CAST_MOD, CAST);
		CAST.init(EFFECT);
		EFFECT.init(EFFECT);
	}
	
	private void init(BuildTag ... possibleNext) {
		for(BuildTag bt:possibleNext) {
			allowedNext.add(bt);
		}
	}
	
	public boolean isAllowedAsNext(BuildTag other) {
		return allowedNext.contains(other);
	}
}
