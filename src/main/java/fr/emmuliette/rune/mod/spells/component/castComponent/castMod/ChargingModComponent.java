package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.data.client.ModSounds;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.event.StopCastingEvent;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.Callback;
import fr.emmuliette.rune.mod.spells.component.castComponent.CallbackMod;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import fr.emmuliette.rune.mod.spells.tags.SpellTag;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChargingModComponent extends AbstractCastModComponent implements CallbackMod {
	private static final Set<Callback> listeningCB = new HashSet<Callback>();

	public ChargingModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
		this.addTag(SpellTag.CHARGING);
	}

	private class ChargingCallback extends Callback {
		public ChargingCallback(AbstractCastModComponent parent, SpellContext context) {
			super(parent, context, -1, true);
		}

		private int tick;
		private int modulo;
		private float power, maxPower;

		@Override
		public boolean begin() {
			tick = 0;
			power = this.getContext().getPower();
			maxPower = getMaxPower();
			modulo = ((ChargingModComponent) getParent()).getChargeSpeed(this.getContext());
			listeningCB.add(this);
			this.getContext().getWorld().playSound(null, this.getContext().getCaster().getX(),
					this.getContext().getCaster().getY(), this.getContext().getCaster().getZ(),
					ModSounds.CHARGING_BEGIN, SoundCategory.AMBIENT, 1.0f, 0.4f);
			return true;
		}

		@Override
		public boolean _callBack() {
			return true;
		}

		@Override
		public boolean finalize(boolean result) {
			if(getContext().getCaster() instanceof LivingEntity) {
				if (((LivingEntity) getContext().getCaster()).isUsingItem())
					((LivingEntity) getContext().getCaster()).stopUsingItem();
			}
			if (result)
				this.getContext().getWorld().playSound(null, this.getContext().getCaster().getX(),
						this.getContext().getCaster().getY(), this.getContext().getCaster().getZ(),
						ModSounds.CHARGING_END, SoundCategory.AMBIENT, 1.0f, 0.4f);
			if(listeningCB.contains(this))
				listeningCB.remove(this);
			return result;
		}

		@Override
		public boolean tick() {
			if (power >= maxPower) {
				return true;
			}
			++tick;
			if (tick == modulo) {
				tick = 0;
				try {
					ICaster cap = this.getContext().getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
							.orElseThrow(new CasterCapabilityExceptionSupplier(this.getContext().getCaster()));
					Cost<?> cost = getBoostCost();
					if (cost.canPay(cap, this.getContext())) {
						payCost(cap, this.getContext());
						this.getContext().getWorld().playSound(null, this.getContext().getCaster().getX(),
								this.getContext().getCaster().getY(), this.getContext().getCaster().getZ(),
								ModSounds.CHARGING_TICK, SoundCategory.AMBIENT, 1.0f, 0.4f);
						power += 1f;
						return true;
					} else {
						return true;
					}
				} catch (CasterCapabilityException e) {
					e.printStackTrace();
				} catch(NotEnoughManaException e) {
					
				}
			}
			return true;
		}
	}

	@Override
	public Callback castCallback(SpellContext context) {
		return new ChargingCallback(this, context);
	}

	@SubscribeEvent
	public static void castOnRelease(StopCastingEvent event) {
		List<ChargingCallback> finishedCB = new ArrayList<ChargingCallback>();
		for (Callback cb : listeningCB) {
			if (cb instanceof ChargingCallback && cb.getContext().getCaster() == event.getCaster()) {
				finishedCB.add((ChargingCallback) cb);
			}
		}
		for (ChargingCallback cb : finishedCB) {
			// Number of tick divided by tickCount for 1 power
			System.out.println("Charged power is " + cb.power);
			cb.getContext().setPower(cb.power);
			cb.castChildren();
			cb.finish(true);
//			listeningCB.remove(cb);
		}
	}

	@SubscribeEvent
	public static void cancelOnDamage(LivingDamageEvent event) {
		if (event.getAmount() <= 0) {
			return;
		}
		List<Callback> cancelledCB = new ArrayList<Callback>();
		for (Callback cb : listeningCB) {
			if (cb.getParent() instanceof ChargingModComponent) {
				if (cb.getContext().getCaster() == event.getEntityLiving() && !((ChargingModComponent) cb.getParent())
						.getBoolProperty(KEY_IGNORE_CANCEL_ON_DAMAGE)) {
					cancelledCB.add(cb);
				}
			}
		}
		for (Callback cb : cancelledCB) {
			cb.cancel(true);
		}
	}

	protected int getChargeSpeed(SpellContext context) {
		return 100 - 10 * getIntProperty(KEY_CHARGE_SPEED, context.getPower());
	}

	// PROPERTIES

	private static final String KEY_CHARGE_SPEED = "charge_speed";
	private static final String KEY_IGNORE_CANCEL_ON_DAMAGE = "ignore_cancel_on_damage";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties buildInternal() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					Map<Grade, Integer> durationLevels = new HashMap<Grade, Integer>();
					durationLevels.put(Grade.WOOD, 2);
					durationLevels.put(Grade.IRON, 3);
					durationLevels.put(Grade.NETHERITE, 7);

					this.addNewProperty(new LevelProperty(KEY_CHARGE_SPEED, durationLevels, () -> new ManaCost(1)));
					this.addNewProperty(
							new BoolProperty(KEY_IGNORE_CANCEL_ON_DAMAGE, Grade.GOLD, () -> new ManaCost(10)));
				}
			};
			return retour;
		}
	};

	@Override
	public Cost<?> applyCostMod(Cost<?> in) {
		int chargeTime = (int) this.getIntProperty(KEY_CHARGE_SPEED);
		in.add(new ManaCost(chargeTime));
		return in;
	}

	@Override
	public int applyCDMod(int in) {
		return in;
	}
}
