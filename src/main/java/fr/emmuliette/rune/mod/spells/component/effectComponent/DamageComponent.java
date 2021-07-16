package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DamageComponent extends AbstractEffectComponent {
	public DamageComponent() {
		super(PropertyFactory.EMPTY_FACTORY);
	}
	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		target.hurt(DamageSource.GENERIC, 6.0F);
		return true;
	}
	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		return false;
	}
	
	@Override
	public float getManaCost() {
		return 1f;
	}
}
