package fr.emmuliette.rune.mod.spells.component.effectComponent;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractEffectComponent {
	//public abstract boolean applyEffect(Entity target);
	public abstract boolean applyEffect(LivingEntity target);
	public abstract boolean applyEffect(World world, BlockPos target);
}
