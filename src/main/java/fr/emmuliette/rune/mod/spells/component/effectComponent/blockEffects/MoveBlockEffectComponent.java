package fr.emmuliette.rune.mod.spells.component.effectComponent.blockEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.BlockGrid;
import fr.emmuliette.rune.mod.spells.properties.EnumElement;
import fr.emmuliette.rune.mod.spells.properties.EnumProperty;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import fr.emmuliette.rune.mod.tileEntity.AnchoredTileEntity;
import fr.emmuliette.rune.mod.tileEntity.IllusionTileEntity;
import fr.emmuliette.rune.mod.tileEntity.PhasedTileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MoveBlockEffectComponent extends BlockEffectComponent {
	public MoveBlockEffectComponent(AbstractSpellComponent parent) {
		super(PROPFACT, parent);
	}

	@Override
	public Cost<?> getCost() {
		Cost<?> retour = new ManaCost(1);
		retour.add(super.getCost());
		return retour;
	}

	@Override
	protected boolean blockEffects(World world, BlockPos block, SpellContext context) {
		Direction direction = Direction.byName(this.getEnumProperty(KEY_DIRECTION));
		BlockGrid grid = this.getGridProperty(KEY_BLOCKS_LEVEL);
		Direction dir = (context.getBlockDirection() == null) ? context.getCasterFacing() : context.getBlockDirection();
		for (BlockPos newBlock : grid.getBlockPos(world, block, dir)) {
			if (world.getBlockEntity(newBlock) != null) {
				if (world.getBlockEntity(newBlock) instanceof PhasedTileEntity
						|| world.getBlockEntity(newBlock) instanceof AnchoredTileEntity
						|| world.getBlockEntity(newBlock) instanceof IllusionTileEntity)
					continue;
			}
			System.out.println("Applying on " + newBlock.toShortString());
			BlockPos blockpos = newBlock.relative(direction);
			if (world.getBlockState(blockpos).getBlockState() == Blocks.AIR.defaultBlockState()) {
				// TODO le 3eme paramètr ecékoa ? Et aussi apparemment ça met pas le lboc à jour
				// tout le temps ici
				world.setBlockAndUpdate(blockpos, world.getBlockState(newBlock));
				world.setBlockAndUpdate(newBlock, Blocks.AIR.defaultBlockState());
			}
		}
		return true;
	}

	protected static final String KEY_DIRECTION = "direction";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties buildInternal() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					Map<EnumElement, Supplier<? extends Cost<?>>> directions = new HashMap<EnumElement, Supplier<? extends Cost<?>>>();
					for (Direction direction : Direction.values()) {
						directions.put(new EnumElement(direction.getName(), Grade.WOOD), Cost.ZERO_COST);
					}

					this.addNewProperty(
							new EnumProperty(KEY_DIRECTION, Grade.WOOD, Direction.UP.getName(), directions));
				}
			};
			return retour;
		}
	};
}
