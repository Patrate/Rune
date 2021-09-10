package fr.emmuliette.rune.mod.spells.tags;

import java.util.HashSet;
import java.util.Set;

public class BuildTag extends Tag {
	public static BuildTag CAST_MOD = new BuildTag();
	public static BuildTag CAST = new BuildTag();
	public static BuildTag EFFECT = new BuildTag();
	public static BuildTag MAGIC_ENTITY = new BuildTag();
	public static BuildTag ENTITY_AI = new BuildTag();
	public static BuildTag POST_CAST = new BuildTag();
	public static BuildTag NULL = new BuildTag();

	private Set<BuildTag> allowedNext;

	private BuildTag() {
		allowedNext = new HashSet<BuildTag>();
	}

	static void init() {
		CAST_MOD.init(CAST);
		CAST.init(EFFECT, MAGIC_ENTITY);
		EFFECT.init(EFFECT, MAGIC_ENTITY);
		MAGIC_ENTITY.init(ENTITY_AI, CAST);
		ENTITY_AI.init(ENTITY_AI, CAST);
	}

	private void init(BuildTag... possibleNext) {
		for (BuildTag bt : possibleNext) {
			allowedNext.add(bt);
		}
	}

	public boolean isAllowedAsNext(BuildTag other) {
		return allowedNext.contains(other);
	}
}
