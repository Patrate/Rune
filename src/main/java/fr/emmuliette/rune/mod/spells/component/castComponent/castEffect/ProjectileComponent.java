package fr.emmuliette.rune.mod.spells.component.castComponent.castEffect;

import com.google.common.base.Function;

import fr.emmuliette.rune.data.client.ModSounds;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleBoolean;
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
	public ProjectileComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
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
		projectile.setNoGravity(!this.getPropertyValue(KEY_GRAVITY, false));

		Vector3d lookAngle = context.getCaster().getLookAngle();
		projectile.shoot(lookAngle.x, lookAngle.y, lookAngle.z, this.getPropertyValue(KEY_SPEED, 8) / 10f, 0F);// 12.0F);
		context.getWorld().addFreshEntity(projectile);
		context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
				context.getCaster().getZ(), ModSounds.PROJECTILE_LAUNCH, SoundCategory.AMBIENT, 1.0f, 0.4f);
		return true;
	}

	private static final String KEY_SPEED = "speed";
	private static final String KEY_GRAVITY = "gravity";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public ComponentProperties build() {
			ComponentProperties retour = new ComponentProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.WOOD, new Property<Integer>(KEY_SPEED, new PossibleInt(16, 8, 24, 1),
							new Function<Integer, Float>() {
								@Override
								public Float apply(Integer val) {
									return 0f;
								}
							})).addNewProperty(Grade.IRON, new Property<Boolean>(KEY_GRAVITY, new PossibleBoolean(true),
									new Function<Boolean, Float>() {
										@Override
										public Float apply(Boolean input) {
											return (input) ? 1f : 0f;
										}
									}));
				}
			};
			return retour;
		}
	};
}
