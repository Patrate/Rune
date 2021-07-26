package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
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
		target.setSecondsOnFire((int) (1 + context.getPower()));
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

	@Override
	public float getMaxPower() {
		return 10; // TODO mettre les apramètres à la place
	}

	// PROPERTIES

	private static final String KEY_DAMAGE_LEVEL = "damage_level";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public ComponentProperties build() {
			ComponentProperties retour = new ComponentProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.WOOD, new LevelProperty(KEY_DAMAGE_LEVEL, 10, new ManaCost(1)));
				}
			};
			return retour;
		}
	};
}
