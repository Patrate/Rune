package fr.emmuliette.rune.mod.effects;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fml.RegistryObject;

public class ModEffects extends Effect {
	// TODO
	public static final RegistryObject<? extends Effect> MANA_GAIN = register("instant_mana_gain",
			() -> new ModInstantEffect(EffectType.BENEFICIAL, 16262179));
	public static final RegistryObject<? extends Effect> MANA_LOSS = register("instant_mana_loss",
			() -> new ModInstantEffect(EffectType.HARMFUL, 4393481));
	public static final RegistryObject<? extends Effect> MANA_REGENERATION = register("mana_regeneration",
			() -> new ModEffects(EffectType.BENEFICIAL, 13458603));
	public static final RegistryObject<? extends Effect> MANA_POISON = register("mana_poison",
			() -> new ModEffects(EffectType.HARMFUL, 5149489));
	public static final RegistryObject<? extends Effect> MANA_BOOST = register("mana_boost",
			() -> (new ManaBoostEffect(EffectType.BENEFICIAL, 16284963)));
	// TODO .addAttributeModifier(Attributes.MAX_HEALTH,
	// "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0D,
	// AttributeModifier.Operation.ADDITION));

	public static final RegistryObject<? extends Effect> FREE_SPELL = register("free_spell",
			() -> new ModEffects(EffectType.BENEFICIAL, 16284963));
	public static final RegistryObject<? extends Effect> SILENCED = register("silenced",
			() -> new ModEffects(EffectType.HARMFUL, 5578058));

	private static RegistryObject<? extends Effect> register(String name, Supplier<? extends Effect> effectSupplier) {
		return Registration.EFFECTS.register(name, effectSupplier);
	}

	protected ModEffects(EffectType type, int color) {
		super(type, color);
	}

	public void applyEffectTick(LivingEntity entity, int tick) {
		if (this == ModEffects.MANA_REGENERATION.get()) {
			entity.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(new NonNullConsumer<ICaster>() {
				@Override
				public void accept(ICaster cap) {
					if (cap.getMana() < cap.getMaxMana()) {
						cap.addMana(1.0F);
					}
				}
			});
		} else if (this == ModEffects.MANA_POISON.get()) {
			entity.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(new NonNullConsumer<ICaster>() {
				@Override
				public void accept(ICaster cap) {
					if (cap.getMana() > 1.0F) {
						try {
							cap.delMana(1.0F);
						} catch (NotEnoughManaException e) {
							e.printStackTrace();
						}
					}
				}
			});
		} else if (this == ModEffects.MANA_LOSS.get()) {
			entity.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(new NonNullConsumer<ICaster>() {
				@Override
				public void accept(ICaster cap) {
					float val = (float) (6 << tick);
					if (cap.getMana() > val) {
						try {
							cap.delMana(val);
						} catch (NotEnoughManaException e) {
							e.printStackTrace();
						}
					}
				}
			});
		} else if (this == ModEffects.MANA_GAIN.get()) {
			entity.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(new NonNullConsumer<ICaster>() {
				@Override
				public void accept(ICaster cap) {
					float val = (float) Math.max(4 << tick, 0);
					cap.addMana(val);
				}
			});
		} else {
			super.applyEffectTick(entity, tick);
		}

	}

	public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity caster, LivingEntity target, int tick,
			double power) {
		if (this == ModEffects.MANA_GAIN.get()) {
			target.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(new NonNullConsumer<ICaster>() {
				@Override
				public void accept(ICaster cap) {
					int i = (int) (power * (double) (4 << tick) + 0.5D);
					cap.addMana((float) i);
				}
			});

		} else if (this == ModEffects.MANA_LOSS.get()) {
			target.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(new NonNullConsumer<ICaster>() {
				@Override
				public void accept(ICaster cap) {
					int j = (int) (power * (double) (6 << tick) + 0.5D);
					try {
						cap.delMana((float) j);
					} catch (NotEnoughManaException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			this.applyEffectTick(target, tick);
		}
	}

	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		if (this == ModEffects.MANA_REGENERATION.get()) {
			int k = 50 >> p_76397_2_;
			if (k > 0) {
				return p_76397_1_ % k == 0;
			} else {
				return true;
			}
		} else if (this == ModEffects.MANA_POISON.get()) {
			int j = 25 >> p_76397_2_;
			if (j > 0) {
				return p_76397_1_ % j == 0;
			} else {
				return true;
			}
		} else {
			return super.isDurationEffectTick(p_76397_1_, p_76397_2_);
		}
	}
	
	public static void register() {
	}
}
