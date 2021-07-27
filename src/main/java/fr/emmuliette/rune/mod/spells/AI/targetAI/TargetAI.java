package fr.emmuliette.rune.mod.spells.AI.targetAI;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.AI.AbstractAI;
import fr.emmuliette.rune.mod.spells.entities.MagicEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

public abstract class TargetAI extends AbstractAI {
	public static Supplier<TargetAI> DEFAULT = () ->  new TargetAI() {
		@Override
		public LivingEntity getEntityTarget(MagicEntity entity) {
			return null;
		}
		@Override
		public BlockPos getBlockTarget(MagicEntity entity) {
			return null;
		}
	};
	public abstract LivingEntity getEntityTarget(MagicEntity entity);
	public abstract BlockPos getBlockTarget(MagicEntity entity);
}
