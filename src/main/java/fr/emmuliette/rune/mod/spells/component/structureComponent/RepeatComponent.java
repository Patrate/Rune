package fr.emmuliette.rune.mod.spells.component.structureComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RepeatComponent extends AbstractEffectComponent {
	private AbstractCastComponent<?> toRepeat;
	private int maxRepeat, repeatCount;

	public RepeatComponent(PropertyFactory propFactory, AbstractSpellComponent parent) {
		super(propFactory, parent);
		maxRepeat = 4; // TODO mettre ça en property, le mettre boostable evidemment
		repeatCount = 0;
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		if (canCastChildren(context)) {
			return castChildren(context);
		}
		return false;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos target, SpellContext context) {
		if (canCastChildren(context)) {
			return castChildren(context);
		}
		return false;
	}

	public boolean castChildren(SpellContext context) {
		repeatCount++;
		return toRepeat.cast(context, false);
	}

	public boolean canCastChildren(SpellContext context) {
		return repeatCount < maxRepeat;
	}
}
