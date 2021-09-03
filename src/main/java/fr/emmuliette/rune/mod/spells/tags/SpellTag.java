package fr.emmuliette.rune.mod.spells.tags;

public class SpellTag extends OtherTag {
	public static final SpellTag CHARGING = new SpellTag(true);
	public static final SpellTag LOADING = new SpellTag(true);
	public static final SpellTag CHANNELING = new SpellTag(true);
	public static final SpellTag TOGGLE = new SpellTag(true);
	// public static final RestrictionTag NO_AGGRO = new RestrictionTag();
	// public static final RestrictionTag NO_PARCHMENT = new RestrictionTag();
	// public static final RestrictionTag NO_IA = new RestrictionTag();

	private boolean unique;

	private SpellTag(boolean unique) {
		this.unique = unique;
	}

	static void init() {
	}

	public boolean isUnique() {
		return unique;
	}
}
