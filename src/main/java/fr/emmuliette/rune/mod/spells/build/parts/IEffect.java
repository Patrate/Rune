package fr.emmuliette.rune.mod.spells.build.parts;

import fr.emmuliette.rune.mod.spells.build.PartPriority;

public interface IEffect extends IBuildPart{
	@Override
	public default int getLevel() {
		return PartPriority.EFFECT.level;
	}
}
