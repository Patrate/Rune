package fr.emmuliette.rune.mod.spells.build.parts;

public interface IBuildPart {
	public abstract int getLevel();
	public default boolean checkNextPart(IBuildPart other) {
		return this.getLevel() >= other.getLevel();
	}
}
