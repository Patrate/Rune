package fr.emmuliette.rune.mod.spells.build.parts;

import fr.emmuliette.rune.mod.spells.build.PartPriority;

public interface IManaMod extends IBuildPart{
	@Override
	public default int getLevel() {
		return PartPriority.MANAMOD.level;
	}
}
