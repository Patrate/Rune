package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEffectComponent extends AbstractEffectComponent {
	public BlockEffectComponent(AbstractSpellComponent parent) {
		super(PropertyFactory.EMPTY_FACTORY, parent);
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		return false;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		return moveBlocks(world, position, Direction.UP);
	}

	@Override
	public Cost<?> getCost() {
		return new ManaCost(null, 1);
	}

	private boolean moveBlocks(World world, BlockPos block, Direction moveDir) {
		BlockPos blockpos = block.relative(moveDir);
		if (world.getBlockState(blockpos).getBlockState() == Blocks.AIR.defaultBlockState()) {
			System.out.println("ITS AIR ALRIGHT");
			world.setBlock(blockpos, world.getBlockState(block), 0);
			world.setBlock(block, Blocks.AIR.defaultBlockState(), 18);
		}

		return true;
	}
}