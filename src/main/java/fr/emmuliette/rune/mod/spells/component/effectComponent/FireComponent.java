package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.RuneProperties;
import fr.emmuliette.rune.mod.spells.SpellContext;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireComponent extends AbstractEffectComponent {
	public FireComponent(RuneProperties properties) {
		super(properties);
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		target.setSecondsOnFire(5);
		return true;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		// Should probably that at random to the X block around
		if (world.getBlockState(position.above()) == Blocks.AIR.defaultBlockState()  && world.getBlockState(position).isSolidRender(world, position)) {
            world.setBlockAndUpdate(position.above(), AbstractFireBlock.getState(world, position.above()));
		}
        
		return true;
	}
	
	@Override
	public float getManaCost() {
		return 5f;
	}

}
