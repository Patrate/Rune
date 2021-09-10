package fr.emmuliette.rune.mod.spells.component.effectComponent.blockEffects;

import fr.emmuliette.rune.exception.UnknownPropertyException;
import fr.emmuliette.rune.mod.blocks.IllusionBlock;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import fr.emmuliette.rune.mod.spells.properties.blockGrid.BlockGrid;
import fr.emmuliette.rune.mod.spells.properties.variable.BlockTypeProperty;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IllusionBlockEffectComponent extends BlockEffectComponent {
	public IllusionBlockEffectComponent(AbstractSpellComponent parent) {
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
		BlockState type = null;
		try {
			type = ((BlockTypeProperty) this.getProperty(KEY_TYPE)).getValue();
		} catch (UnknownPropertyException e) {
			e.printStackTrace();
		}
		if (type == null)
			return false;
		BlockGrid grid = this.getGridProperty(KEY_BLOCKS_LEVEL);
		Direction dir = (context.getBlockDirection() == null) ? context.getCasterFacing() : context.getBlockDirection();
		for (BlockPos newBlock : grid.getBlockPos(world, block, dir)) {
			IllusionBlock.createIllusionBlock(world, newBlock, 300, type);
		}
		return true;
	}

	private static final String KEY_TYPE = "type";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties buildInternal() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					this.addNewProperty(new BlockTypeProperty(KEY_TYPE, 0));
				}
			};
			return retour;
		}
	};
}
