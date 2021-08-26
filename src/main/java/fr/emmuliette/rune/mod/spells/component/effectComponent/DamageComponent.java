package fr.emmuliette.rune.mod.spells.component.effectComponent;

import java.util.HashMap;
import java.util.Map;

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
		float damages = (float) Math
				.ceil(Math.pow(1.4f, this.getIntProperty(KEY_DAMAGE_LEVEL, context.getPower())) - 0.5);
		System.out.println("Damage component level " + this.getIntProperty(KEY_DAMAGE_LEVEL) + " with "
				+ context.getPower() + " power (total " + this.getIntProperty(KEY_DAMAGE_LEVEL, context.getPower())
				+ ") = " + damages + " damages");
		target.hurt(DamageSource.MAGIC, damages);
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
					Map<Grade, Integer> damageLevels = new HashMap<Grade, Integer>();
					damageLevels.put(Grade.WOOD, 2);
					damageLevels.put(Grade.IRON, 3);
					damageLevels.put(Grade.NETHERITE, 7);

					this.addNewProperty(new LevelProperty(KEY_DAMAGE_LEVEL, damageLevels, () -> new ManaCost(1), true));
				}
			};
			return retour;
		}
	};
}
