package fr.emmuliette.rune.mod.spells.component.castComponent.castEffect;

import java.util.List;

import com.google.common.base.Function;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleEnum;

public class InstanProjectileComponent extends AbstractCastEffectComponent implements TargetAir {
	public InstanProjectileComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
	}

	@Override
	public boolean internalCast(SpellContext context) {
		String mode = this.getPropertyValue(KEY_MODE, "laser");
		List<Object> targets;
		switch (mode) {
		case ("eclair"):
			// TODO
			// Targets list = get all valid target in range, kinda around where we looking tho ?
			// for now, all livingentity != caster
			break;
		default:
		case ("laser"):
			// TODO
			// Targets list = get all valid target between player & next solid block
			// (limited range tho)
			// Also get next solid block.
			break;
		}
		// TODO: For every target in target list, apply on position or target as needed.
		// TODO: Find a way to draw the laser or the eclair
//		applyOnPosition(this.level, block.getBlockPos(), context);
//		applyOnTarget((LivingEntity) entity.getEntity(), context);
//		
//		Vector3d lookAngle = context.getCaster().getLookAngle();
//		context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
//				context.getCaster().getZ(), ModSounds.PROJECTILE_LAUNCH, SoundCategory.AMBIENT, 1.0f, 0.4f);
		return true;
	}

	private static final String KEY_MODE = "mode";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public ComponentProperties build() {
			ComponentProperties retour = new ComponentProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.WOOD, new Property<String>(KEY_MODE,
							new PossibleEnum("laser", "laser", "eclair"), new Function<String, Float>() {
								@Override
								public Float apply(String input) {
									return (input.equals("laser")) ? 1f : 2f;
								}
							}));
				}
			};
			return retour;
		}
	};
}
