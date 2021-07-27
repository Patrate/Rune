package fr.emmuliette.rune.mod.spells.component.structureComponent.AI;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.AI.AbstractAI;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractAIComponent extends AbstractSpellComponent {
	public AbstractAIComponent(PropertyFactory propFact, AbstractSpellComponent parent) {
		super(propFact, parent);
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		return false;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos target, SpellContext context) {
		return false;
	}

	@Override
	public boolean addNextPart(AbstractSpellComponent other) {
		return this.getParent().addNextPart(other);
	}
	
	public abstract AbstractAI getAI(SpellContext context);

}
