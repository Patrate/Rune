package fr.emmuliette.rune.mod.spells.component.effectComponent;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DamageComponent extends AbstractEffectComponent {
	@Override
	public boolean applyEffect(LivingEntity target) {
		target.hurt(DamageSource.GENERIC, 6.0F);
		return true;
	}
	@Override
	public boolean applyEffect(World world, BlockPos position) {
		return false;
	}
}
