package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class ProjectileComponent extends AbstractCastComponent implements TargetAir {

	public ProjectileComponent(AbstractEffectComponent nextComponent) {
		super(nextComponent);
	}

	@Override
	public boolean cast(SpellContext context) {
		ProjectileItemEntity projectile = new ProjectileItemEntity(EntityType.EGG, context.getPlayer(), context.getWorld()) {
			@Override
			protected Item getDefaultItem() {
				return Items.SNOWBALL;
			}
			@Override
			protected void onHitEntity(EntityRayTraceResult entity) {
				if(entity.getEntity() instanceof LivingEntity) {
					getNextComponent().applyEffect((LivingEntity)entity.getEntity());
					this.remove();
				}
			}
				

			@Override
			protected void onHitBlock(BlockRayTraceResult block) {
				super.onHitBlock(block);
				if(block == null) {
					System.err.println("IL N'Y A PAS DE BLOCK CALIF DE TABERNAK");
					return;
				}
				getNextComponent().applyEffect(this.level, block.getBlockPos());
				this.remove();
			}
		};
		projectile.setNoGravity(true);
		
		Vector3d lookAngle = context.getPlayer().getLookAngle();
		projectile.shoot(lookAngle.x, lookAngle.y, lookAngle.z, 1.6F, 12.0F);
		context.getPlayer().playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F );
		context.getWorld().addFreshEntity(projectile);
		return true;
	}

}
