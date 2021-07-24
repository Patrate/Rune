package fr.emmuliette.rune.mod.spells.component.castComponent.castEffect;

import java.util.List;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class ZoneComponent extends AbstractCastEffectComponent implements TargetAir {

	public ZoneComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PropertyFactory.EMPTY_FACTORY, parent);
	}

	@Override
	public boolean internalCast(SpellContext context) {
		List<Entity> entities = context.getWorld().getEntities(context.getCaster(),
				context.getCaster().getBoundingBox().expandTowards(5.0D, 5.0D, 5.0D));
		for (Entity e : entities) {
			if (e instanceof LivingEntity)
				this.applyOnTarget((LivingEntity) e, context);
		}
		return true;
	}
}
