package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.properties.SpellProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TeleportComponent extends AbstractEffectComponent {
	public TeleportComponent() {
		super();
	}
	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		context.getPlayer().teleportTo(target.blockPosition().getX(), target.blockPosition().getY(), target.blockPosition().getZ());
		return true;
	}
	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		context.getPlayer().teleportTo(position.getX(), position.getY()+1, position.getZ());
		return true;
	}
	
	@Override
	public float getManaCost() {
		return 5f;
	}
	@Override
	public SpellProperties getDefaultProperties() {
		SpellProperties retour = new SpellProperties();
		return retour;
	}
}
