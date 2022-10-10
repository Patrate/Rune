package fr.emmuliette.rune.mod.spells.component.variableComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PositionVariableComponent extends AbstractVariableComponent {

	public PositionVariableComponent(PropertyFactory propFact, AbstractSpellComponent parent) {
		super(propFact, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos target, SpellContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addNextPart(AbstractSpellComponent other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
	}

}
