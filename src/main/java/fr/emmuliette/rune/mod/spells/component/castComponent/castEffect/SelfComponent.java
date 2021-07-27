package fr.emmuliette.rune.mod.spells.component.castComponent.castEffect;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;

public class SelfComponent extends AbstractCastEffectComponent implements TargetAir {

	public SelfComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PropertyFactory.EMPTY_FACTORY, parent);
	}

	@Override
	public boolean internalCast(SpellContext context) {
		if(context.getCaster() instanceof LivingEntity)
			return applyOnSelf((LivingEntity) context.getCaster(), context);
		return false;
	}
}
