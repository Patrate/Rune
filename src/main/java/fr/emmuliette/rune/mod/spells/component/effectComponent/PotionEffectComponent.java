package fr.emmuliette.rune.mod.spells.component.effectComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.ModEffectInstance;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.entities.AreaEffectCloudEntity;
import fr.emmuliette.rune.mod.spells.properties.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class PotionEffectComponent extends AbstractEffectComponent implements ChannelEffect {
	public static final Predicate<LivingEntity> MAGIC_SENSITIVE = LivingEntity::isSensitiveToWater; // TODO is sensitive
																									// to magic
	private List<Effect> effectList;
	private boolean showIcon;
	private int secondPerLevel;
	private List<ModEffectInstance> activeEffects;

	protected PotionEffectComponent(AbstractSpellComponent parent, Effect effect, int secondPerLevel,
			boolean showIcon) {
		super(PROPFACT, parent);
		this.effectList = new ArrayList<Effect>();
		this.effectList.add(effect);
		this.showIcon = showIcon;
		this.secondPerLevel = secondPerLevel;
		this.activeEffects = new ArrayList<ModEffectInstance>();
	}

	protected PotionEffectComponent(AbstractSpellComponent parent, Effect effect, int secondPerLevel) {
		this(parent, effect, secondPerLevel, true);
	}

	protected PotionEffectComponent(AbstractSpellComponent parent, Effect effect) {
		this(parent, effect, 1, true);
	}

	public EffectInstance getEffectInstance(Effect effect, float power, boolean isChanneled) {
		if (isChanneled) {
			ModEffectInstance mei = new ModEffectInstance(effect, getDuration(power), getAmplifier(power), isAmbient(),
					isVisible(), isShowIcon(), false);
			activeEffects.add(mei);
			return mei;
		} else {
			return new ModEffectInstance(effect, getDuration(power), getAmplifier(power), isAmbient(), isVisible(),
					isShowIcon(), true);
		}
	}

	protected int getDuration(float power) {
		return this.getIntProperty(KEY_POTION_DURATION, power) * (secondPerLevel * 20);
	}

	protected int getAmplifier(float power) {
		return this.getIntProperty(KEY_POTION_AMPLIFIER, power);
	}

	protected boolean isLingering() {
		return this.getBoolProperty(KEY_POTION_LINGERING);
	}

	protected boolean isVisible() {
		return this.getBoolProperty(KEY_POTION_VISIBLE);
	}

	protected boolean isAmbient() {
		return this.getBoolProperty(KEY_POTION_AMBIENT);
	}

	protected boolean isShowIcon() {
		return showIcon;
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		registerChannelEffect(context);
		for (Effect effect : effectList) {
			target.addEffect(getEffectInstance(effect, context.getPower(), context.isChanneling()));
		}
		if (this.isLingering()) {
			this.makeAreaOfEffectCloud(context);
		} else {
			this.applySplash(context);
		}
		// event ptet ?

		return true;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		if (world == null || position == null || context == null)
			return false;
		registerChannelEffect(context);
		Potion potion = PotionUtils.getPotion(context.getItemStack());
		List<Effect> list = effectList;
		boolean flag = potion == Potions.WATER && list.isEmpty();
		Direction direction = Direction.UP;
		BlockPos blockpos1 = position.relative(direction);
		if (flag) {
			this.dowseFire(context, blockpos1, direction);
			this.dowseFire(context, blockpos1.relative(direction.getOpposite()), direction);

			for (Direction direction1 : Direction.Plane.HORIZONTAL) {
				this.dowseFire(context, blockpos1.relative(direction1), direction1);
			}
		}
		return true;
	}

	// PROPERTIES

	// boolean lingering, int duration, int amplifier, boolean ambient, boolean
	// visible, boolean showIcon
	// Lingering = splash cloud
	// ambient = ???
	// visible = ???
	// showIcon = ???
	private static final String KEY_POTION_DURATION = "potion_duration";
	private static final String KEY_POTION_AMPLIFIER = "potion_amplifier";
	private static final String KEY_POTION_LINGERING = "potion_lingering";
	private static final String KEY_POTION_AMBIENT = "potion_ambient";
	private static final String KEY_POTION_VISIBLE = "potion_visible";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties build() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.WOOD,
							new LevelProperty(KEY_POTION_DURATION, 10, () -> new ManaCost(1), true))
							.addNewProperty(Grade.WOOD,
									new LevelProperty(KEY_POTION_AMPLIFIER, 10, () -> new ManaCost(1), true))
							.addNewProperty(Grade.IRON, new BoolProperty(KEY_POTION_LINGERING, () -> new ManaCost(3)))
							.addNewProperty(Grade.SECRET, new BoolProperty(KEY_POTION_AMBIENT, () -> new ManaCost(1)))
							.addNewProperty(Grade.SECRET, new BoolProperty(KEY_POTION_VISIBLE, () -> new ManaCost(1)));
				}
			};
			return retour;
		}
	};

	private void applySplash(SpellContext context) {
		if (context.getWorld() == null || context.getBlock() == null || context.getCaster() == null)
			return;
		// AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D,
		// 4.0D);
		AxisAlignedBB axisalignedbb = context.getCaster().getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = context.getWorld().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
		if (list.isEmpty()) {
			return;
		}
		for (LivingEntity livingentity : list) {
			if (!livingentity.isAffectedByPotions()) {
				continue;
			}
			double d0 = context.getBlock().distSqr(livingentity.blockPosition());
			if (d0 >= 16.0D) {
				continue;
			}
			double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
			if (livingentity == context.getCaster()) {
				d1 = 1.0D;
			}

			for (Effect effect : effectList) {
				if (effect.isInstantenous()) {
					effect.applyInstantenousEffect(null, context.getCaster(), livingentity,
							this.getAmplifier(context.getPower()), d1);
				} else {
					int i = (int) (d1 * this.getDuration(getMaxPower()) + 0.5D);
					if (i > 20) {
						livingentity.addEffect(getEffectInstance(effect, context.getPower(), context.isChanneling()));
					}
				}
			}
		}
	}

	private void makeAreaOfEffectCloud(SpellContext context) {

		AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(context.getWorld(),
				context.getBlock().getX(), context.getBlock().getY(), context.getBlock().getZ());
		if (context.getCaster() instanceof LivingEntity) {
			areaeffectcloudentity.setOwner((LivingEntity) context.getCaster());
		}

		areaeffectcloudentity.setRadius(3.0F);
		areaeffectcloudentity.setRadiusOnUse(-0.5F);
		areaeffectcloudentity.setWaitTime(10);
		areaeffectcloudentity
				.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
		List<EffectInstance> eInstanceList = new ArrayList<EffectInstance>();
		for (Effect effect : effectList) {
			eInstanceList.add(this.getEffectInstance(effect, context.getPower(), context.isChanneling()));
		}
		areaeffectcloudentity.setEffects(eInstanceList);

		for (EffectInstance effectinstance : PotionUtils.getCustomEffects(context.getItemStack())) {
			areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
		}

		CompoundNBT compoundnbt = context.getItemStack().getTag();
		if (compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99)) {
			areaeffectcloudentity.setFixedColor(compoundnbt.getInt("CustomPotionColor"));
		}

		context.getWorld().addFreshEntity(areaeffectcloudentity);
	}

	private void dowseFire(SpellContext context, BlockPos pos, Direction direction) {
		if (context.getWorld() == null)
			return;
		BlockState blockstate = context.getWorld().getBlockState(pos);
		if (blockstate.is(BlockTags.FIRE)) {
			context.getWorld().removeBlock(pos, false);
		} else if (CampfireBlock.isLitCampfire(blockstate)) {
			context.getWorld().levelEvent((PlayerEntity) null, 1009, pos, 0);
			CampfireBlock.dowse(context.getWorld(), pos, blockstate);
			context.getWorld().setBlockAndUpdate(pos, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
		}

	}

	/**
	 * Is the effect currently started, either canalised or with a timer ?
	 * 
	 * @return true if the effect is started
	 */
	public boolean isStarted() {
		return !this.activeEffects.isEmpty();
	}

	/**
	 * Stop the effect, wether from a timer or stopping canalisation
	 * 
	 * @return true if the effect was running and is now succesfully stopped
	 */
	public boolean stop() {
		if (!isStarted()) {
			return false;
		}
		Iterator<ModEffectInstance> it = this.activeEffects.iterator();
		while (it.hasNext()) {
			ModEffectInstance current = it.next();
			current.stop();
		}
		return true;
	}
}
