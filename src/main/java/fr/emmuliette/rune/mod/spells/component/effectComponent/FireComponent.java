package fr.emmuliette.rune.mod.spells.component.effectComponent;

import java.util.HashMap;
import java.util.Map;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireComponent extends AbstractEffectComponent {
	public FireComponent(AbstractSpellComponent parent) {
		super(PROPFACT, parent);
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		target.setSecondsOnFire((int) (1 + Math.pow(1.4f, this.getIntProperty(KEY_DURATION, context.getPower()))));
		return true;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		// Should probably that at random to the X block around
		if (world.getBlockState(position.above()) == Blocks.AIR.defaultBlockState()
				&& world.getBlockState(position).isSolidRender(world, position)) {
			world.setBlockAndUpdate(position.above(), AbstractFireBlock.getState(world, position.above()));
		}

		return true;
	}

	@Override
	public Cost<?> getCost() {
		Cost<?> retour = new ManaCost(3);
		retour.add(super.getCost());
		return retour;
	}

	// PROPERTIES

	private static final String KEY_DURATION = "duration";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties build() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					Map<Grade, Integer> durationLevels = new HashMap<Grade, Integer>();
					durationLevels.put(Grade.WOOD, 2);
					durationLevels.put(Grade.IRON, 3);
					durationLevels.put(Grade.REDSTONE, 5);
					durationLevels.put(Grade.NETHERITE, 7);
					
					this.addNewProperty(new LevelProperty(KEY_DURATION, durationLevels, () -> new ManaCost(1), true));
				}
			};
			return retour;
		}
	};
}
