package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DamageComponent extends AbstractEffectComponent {
	public DamageComponent(AbstractSpellComponent parent) {
		super(PROPFACT, parent);
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		//target.addEffect(new EffectInstance(Effects.HARM, 1, (int) Math.ceil(Math.pow(1.4f, context.getPower()) - 0.5), false, true));
		
		System.out.println("Damage component level " + this.getIntProperty(KEY_DAMAGE_LEVEL) + " with "
				+ context.getPower() + " power (total " + this.getIntProperty(KEY_DAMAGE_LEVEL, context.getPower())  + ") = "
				+ (float) Math.ceil(Math.pow(1.4f, this.getIntProperty(KEY_DAMAGE_LEVEL, context.getPower())) - 0.5)
				+ " damages");
		target.hurt(DamageSource.GENERIC, (float) Math.ceil(Math.pow(1.4f, context.getPower()) - 0.5));
		return true;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		return false;
	}

	// PROPERTIES

	private static final String KEY_DAMAGE_LEVEL = "damage_level";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties build() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.WOOD, new LevelProperty(KEY_DAMAGE_LEVEL, 10, () -> new ManaCost(1), true));
				}
			};
			return retour;
		}
	};
}
