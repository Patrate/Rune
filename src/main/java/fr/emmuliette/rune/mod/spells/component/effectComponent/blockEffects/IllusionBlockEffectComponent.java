package fr.emmuliette.rune.mod.spells.component.effectComponent.blockEffects;

import fr.emmuliette.rune.mod.blocks.IllusionBlock;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.BlockGrid;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IllusionBlockEffectComponent extends BlockEffectComponent {
	public IllusionBlockEffectComponent(AbstractSpellComponent parent) {
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
		BlockGrid grid = this.getGridProperty(KEY_BLOCKS_LEVEL);
		Direction dir = (context.getBlockDirection() == null) ? context.getCasterFacing() : context.getBlockDirection();
		for (BlockPos newBlock : grid.getBlockPos(world, block, dir)) {
			IllusionBlock.createIllusionBlock(world, newBlock, 300, Blocks.DIRT.defaultBlockState());
		}
		return true;
	}
	
	// TODO ajouter un type de bloc à phaser
}
