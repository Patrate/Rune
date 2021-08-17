package fr.emmuliette.rune.mod.spells.component.effectComponent.blockEffects;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.BlockGrid;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MoveBlockEffectComponent extends BlockEffectComponent {
	public MoveBlockEffectComponent(AbstractSpellComponent parent) {
		super(parent);
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
		for (BlockPos newBlock : grid.getBlockPos(world, block)) {
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
}
