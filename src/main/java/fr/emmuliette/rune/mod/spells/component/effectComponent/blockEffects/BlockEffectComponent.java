package fr.emmuliette.rune.mod.spells.component.effectComponent.blockEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.EnumElement;
import fr.emmuliette.rune.mod.spells.properties.EnumProperty;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.GridProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockEffectComponent extends AbstractEffectComponent {
	public BlockEffectComponent(AbstractSpellComponent parent) {
		super(PROPFACT, parent);
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		return blockEffects(target.level, target.blockPosition(), context);
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		return blockEffects(world, position, context);
	}

	protected abstract boolean blockEffects(World world, BlockPos block, SpellContext context);

	// PROPERTIES

	protected static final String KEY_BLOCKS_LEVEL = "blocks", KEY_DIRECTION = "direction";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties build() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					Map<Grade, Integer> blockLevels = new HashMap<Grade, Integer>();
					blockLevels.put(Grade.WOOD, 15);
					blockLevels.put(Grade.IRON, 30);
					blockLevels.put(Grade.NETHERITE, 120);

					Map<EnumElement, Supplier<? extends Cost<?>>> directions = new HashMap<EnumElement, Supplier<? extends Cost<?>>>();
					for (Direction direction : Direction.values()) {
						directions.put(new EnumElement(direction.getName(), Grade.WOOD), Cost.ZERO_COST);
					}

					this.addNewProperty(new GridProperty(KEY_BLOCKS_LEVEL, blockLevels, () -> new ManaCost(1), true));
					this.addNewProperty(
							new EnumProperty(KEY_DIRECTION, Grade.WOOD, Direction.UP.getName(), directions));
				}
			};
			return retour;
		}
	};
}
