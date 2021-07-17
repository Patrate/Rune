package fr.emmuliette.rune.mod.spells.build.parts;

import fr.emmuliette.rune.mod.spells.build.PartPriority;

public interface ICastMod extends IBuildPart{
	@Override
	public default int getLevel() {
		return PartPriority.CASTMOD.level;
	}
}
