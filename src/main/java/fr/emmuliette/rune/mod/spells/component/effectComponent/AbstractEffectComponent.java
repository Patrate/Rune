package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractEffectComponent extends AbstractSpellComponent {
	public AbstractEffectComponent() {
		super();
	}
	//public abstract boolean applyEffect(Entity target);
	public abstract boolean applyOnTarget(LivingEntity target, SpellContext context);
	public abstract boolean applyOnPosition(World world, BlockPos target, SpellContext context);
	public boolean applyOnSelf(LivingEntity self, SpellContext context) {
		return applyOnTarget(self, context);
	}
}
