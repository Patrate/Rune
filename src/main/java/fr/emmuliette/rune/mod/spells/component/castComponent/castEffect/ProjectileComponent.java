package fr.emmuliette.rune.mod.spells.component.castComponent.castEffect;

import com.google.common.base.Function;

import fr.emmuliette.rune.exception.DuplicatePropertyException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.SpellProperties;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleInt;
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
public class ProjectileComponent extends AbstractCastEffectComponent implements TargetAir {
	public ProjectileComponent() throws RunePropertiesException {
		super();
	}

	@Override
	public boolean internalCast(SpellContext context) {
		ProjectileItemEntity projectile = new ProjectileItemEntity(EntityType.EGG, context.getCaster(),
				context.getWorld()) {
			@Override
			protected Item getDefaultItem() {
				return Items.SNOWBALL;
			}

			@Override
			protected void onHitEntity(EntityRayTraceResult entity) {
				if (entity.getEntity() instanceof LivingEntity) {
					applyOnTarget((LivingEntity) entity.getEntity(), context);
					this.remove();
				}
			}

			@Override
			protected void onHitBlock(BlockRayTraceResult block) {
				super.onHitBlock(block);
				if (block == null) {
					return;
				}
				applyOnPosition(this.level, block.getBlockPos(), context);
				this.remove();
			}
		};
		projectile.setNoGravity(true);

		Vector3d lookAngle = context.getCaster().getLookAngle();
		projectile.shoot(lookAngle.x, lookAngle.y, lookAngle.z, ((float)this.getProperty(KEY_SPEED, new Integer(0)).getValue()) * 0.1f, 0F);// 12.0F);
		context.getWorld().addFreshEntity(projectile);
		context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
				context.getCaster().getZ(), SoundEvents.SNOW_GOLEM_SHOOT, SoundCategory.AMBIENT, 1.0f, 0.4f);
		return true;
	}

	private static final String KEY_SPEED = "speed";
	private static SpellProperties DEFAULT_PROPERTIES;
	{
		if(DEFAULT_PROPERTIES == null) {
			try {
				DEFAULT_PROPERTIES = new SpellProperties();
				DEFAULT_PROPERTIES.addNewProperty(Grade.WOOD, new Property<Integer>(KEY_SPEED, new PossibleInt(16, 8, 24, 1), new Function<Integer, Float>() {
					@Override
					public Float apply(Integer val) {
						return 0f;
					}
				}));
			} catch (DuplicatePropertyException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public SpellProperties getDefaultProperties() {
		return DEFAULT_PROPERTIES;
	}

}
