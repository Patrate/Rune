package fr.emmuliette.rune.mod.spells.AI.targetAI;

import java.util.List;

import fr.emmuliette.rune.mod.spells.entities.MagicEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

public class ClosestEntityAI extends TargetAI {
	private boolean includeCaster = false;
	
	public ClosestEntityAI(boolean includeCaster) {
		this.includeCaster = includeCaster;
	}

	@Override
	public LivingEntity getEntityTarget(MagicEntity entity) {
		List<LivingEntity> inRange = entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox());
		double closest = Float.MAX_VALUE;
		LivingEntity retour = null;
		for(LivingEntity e:inRange) {
			if(!includeCaster && e.getUUID() == entity.getContext().getOriginalCaster().getUUID())
				continue;
			double dist = e.distanceToSqr(entity);
			if(dist < closest) {
				retour = e;
				closest = dist;
			}
		}
		return retour;
	}

	@Override
	public BlockPos getBlockTarget(MagicEntity entity) {
		return null;
	}
	
}
