package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetBlock;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetLivingEntity;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;

public abstract class AbstractCastEffectComponent extends AbstractCastComponent<AbstractEffectComponent> {
	private List<AbstractEffectComponent> children;

	public AbstractCastEffectComponent() throws RunePropertiesException {
		super();
		children = new ArrayList<AbstractEffectComponent>();
	}

	@Override
	public boolean canCast(SpellContext context) {
		try {
			ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));

			if (cap.isCooldown()) {
				return false;
			}

			if (this.getManaCost() > cap.getMana()) {
				return false;
			}
			// target entity
			if (context.getTargetType() == SpellContext.TargetType.ENTITY && this instanceof TargetLivingEntity) {
				return true;
			}
			// target block
			if (context.getTargetType() == SpellContext.TargetType.BLOCK && this instanceof TargetBlock) {
				return true;
			}
			// target air
			if (context.getTargetType() == SpellContext.TargetType.AIR && this instanceof TargetAir) {
				return true;
			}
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getMaxSize() {
		return 999;
	}

	@Override
	public List<AbstractEffectComponent> getChildrens() {
		return children;
	}

	@Override
	public int getSize() {
		return children.size();
	}

	@Override
	public boolean canAddChildren(AbstractSpellComponent children) {
		return (children instanceof AbstractEffectComponent);
	}
}
