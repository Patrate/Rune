package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.variable.BlockPosProperty;
import net.minecraft.util.math.BlockPos;

public class BlockPosWidget extends VariableWidget<BlockPos, BlockPosProperty> {

	protected BlockPosWidget(Grade grade, BlockPosProperty property, AbstractSpellComponent component, int x, int y) {
		super(grade, property, component, x, y);
	}
}
