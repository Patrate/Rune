package fr.emmuliette.rune.mod.spells.tags;

public abstract class RestrictionTag extends OtherTag {
	//public static final RestrictionTag NO_AGGRO = new RestrictionTag();
	//public static final RestrictionTag NO_PARCHMENT = new RestrictionTag();
	//public static final RestrictionTag NO_IA = new RestrictionTag();

	public RestrictionTag() {}
	
	public abstract boolean isValid();
}
