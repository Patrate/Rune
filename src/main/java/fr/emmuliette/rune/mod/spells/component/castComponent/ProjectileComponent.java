package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.RuneProperties;
import fr.emmuliette.rune.mod.spells.SpellContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class ProjectileComponent extends AbstractCastComponent implements TargetAir {

	public ProjectileComponent(RuneProperties properties) throws RunePropertiesException {
		super(properties);
	}

	@Override
	public boolean internalCast(SpellContext context) {
		ProjectileItemEntity projectile = new ProjectileItemEntity(EntityType.EGG, context.getPlayer(), context.getWorld()) {
			@Override
			protected Item getDefaultItem() {
				return Items.SNOWBALL;
			}
			@Override
			protected void onHitEntity(EntityRayTraceResult entity) {
				if(entity.getEntity() instanceof LivingEntity) {
					applyChildOnEntity((LivingEntity)entity.getEntity(), context);
					this.remove();
				}
			}
				

			@Override
			protected void onHitBlock(BlockRayTraceResult block) {
				super.onHitBlock(block);
				if(block == null) {
					return;
				}
				applyChildOnBlock(this.level, block.getBlockPos(), context);
				this.remove();
			}
		};
		projectile.setNoGravity(true);
		
		Vector3d lookAngle = context.getPlayer().getLookAngle();
		projectile.shoot(lookAngle.x, lookAngle.y, lookAngle.z, 1.6F, 0F);//12.0F);
		context.getWorld().addFreshEntity(projectile);
		context.getWorld().playSound(null, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), 
				SoundEvents.SNOW_GOLEM_SHOOT, SoundCategory.AMBIENT, 1.0f, 0.4f);
		return true;
	}

}
