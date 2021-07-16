package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TeleportComponent extends AbstractEffectComponent {
	public TeleportComponent() {
		super(PropertyFactory.EMPTY_FACTORY);
	}
	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		context.getCaster().teleportTo(target.blockPosition().getX(), target.blockPosition().getY(), target.blockPosition().getZ());
		return true;
	}
	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		context.getCaster().teleportTo(position.getX(), position.getY()+1, position.getZ());
		return true;
	}
	
	@Override
	public float getManaCost() {
		return 5f;
	}
}
