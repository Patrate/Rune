package fr.emmuliette.rune.mod.spells.component.castComponent.castEffect;

import java.util.HashMap;
import java.util.Map;

import fr.emmuliette.rune.data.client.ModSounds;
import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetBlock;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetLivingEntity;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import fr.emmuliette.rune.mod.spells.properties.common.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.common.LevelProperty;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class ProjectileComponent extends AbstractCastEffectComponent
		implements TargetAir, TargetBlock, TargetLivingEntity {
	public ProjectileComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
	}

	@Override
	public boolean internalCast(SpellContext context) {
		ProjectileItemEntity projectile = new ProjectileItemEntity(EntityType.EGG, context.getCaster().getX(),
				context.getCaster().getEyeY(), context.getCaster().getZ(), context.getWorld()) {
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
		projectile.setNoGravity(this.getBoolProperty(KEY_GRAVITY));

		Vector3d lookAngle = context.getCaster().getLookAngle();
		projectile.shoot(lookAngle.x, lookAngle.y, lookAngle.z,
				(this.getIntProperty(KEY_SPEED, context.getPower()) + 2) * 4f * 0.1f, 0F);
		context.getWorld().addFreshEntity(projectile);
		context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
				context.getCaster().getZ(), ModSounds.PROJECTILE_LAUNCH, SoundCategory.AMBIENT, 1.0f, 0.4f);
		return true;
	}

	private static final String KEY_SPEED = "speed";
	private static final String KEY_GRAVITY = "no_gravity";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties buildInternal() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					Map<Grade, Integer> speedLevels = new HashMap<Grade, Integer>();
					speedLevels.put(Grade.WOOD, 2);
					speedLevels.put(Grade.IRON, 3);
					speedLevels.put(Grade.NETHERITE, 7);

					this.addNewProperty(new LevelProperty(KEY_SPEED, speedLevels, () -> new ManaCost(1), true)
							.setDescription("Vitesse du projectile"));

					this.addNewProperty(new BoolProperty(KEY_GRAVITY, Grade.IRON, () -> new ManaCost(10))
							.setDescription("Gravité affecte le projectile"));
				}
			};
			return retour;
		}
	};
}
