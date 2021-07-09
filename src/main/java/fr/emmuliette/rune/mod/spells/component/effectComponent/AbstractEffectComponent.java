package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.RuneProperties;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractEffectComponent extends AbstractSpellComponent {
	public AbstractEffectComponent(RuneProperties properties) {
		super(properties);
	}
	//public abstract boolean applyEffect(Entity target);
	public abstract boolean applyEffect(LivingEntity target);
	public abstract boolean applyEffect(World world, BlockPos target);
}
