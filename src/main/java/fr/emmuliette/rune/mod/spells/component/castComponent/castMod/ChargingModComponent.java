package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.data.client.ModSounds;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.event.StopCastingEvent;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.Callback;
import fr.emmuliette.rune.mod.spells.component.castComponent.CallbackMod;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleBoolean;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleInt;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChargingModComponent extends AbstractCastModComponent implements CallbackMod {
	private static final Set<Callback> listeningCB = new HashSet<Callback>();

	public ChargingModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
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
			System.out.println("STARTING CHARGE: " + power + "/ " + maxPower);
			modulo = ((ChargingModComponent) getParent()).getChargeSpeed();
			listeningCB.add(this);
			this.getContext().getWorld().playSound(null, this.getContext().getCaster().getX(), this.getContext().getCaster().getY(),
					this.getContext().getCaster().getZ(), ModSounds.CHARGING_BEGIN, SoundCategory.AMBIENT, 1.0f, 0.4f);
			return true;
		}

		@Override
		public boolean _callBack() {
			return true;
		}

		@Override
		public boolean finalize(boolean result) {
			if (this.getContext().getCaster().isUsingItem())
				this.getContext().getCaster().stopUsingItem();
			if (result)
				this.getContext().getWorld().playSound(null, this.getContext().getCaster().getX(), this.getContext().getCaster().getY(),
						this.getContext().getCaster().getZ(), ModSounds.CHARGING_END, SoundCategory.AMBIENT, 1.0f, 0.4f);
			return result;
		}

		@Override
		public boolean tick() {
			if(power >= maxPower) {
				return true;
			}
			++tick;
			if (tick == modulo) {
				tick = 0;
				try {
					ICaster cap = this.getContext().getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
							.orElseThrow(new CasterCapabilityExceptionSupplier(this.getContext().getCaster()));
					Cost<?> cost = getCost(); // TODO Change for spell tickCost ?
					if (cost.canPay(cap, this.getContext())) {
						payCost(cap, this.getContext());
						this.getContext().getWorld().playSound(null, this.getContext().getCaster().getX(), this.getContext().getCaster().getY(),
								this.getContext().getCaster().getZ(), ModSounds.CHARGING_TICK, SoundCategory.AMBIENT, 1.0f,
								0.4f);
						power += 1f;
						System.out.println("CURRENT CHARGE: " + power + "/ " + maxPower);
						return true;
					} else {
						return true;
					}
				} catch (CasterCapabilityException | NotEnoughManaException e) {
					e.printStackTrace();
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
			cb.getContext().setPower(cb.power);
			cb.castChildren();
			cb.finish(true);
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
						.getPropertyValue(KEY_IGNORE_CANCEL_ON_DAMAGE, false)) {
					cancelledCB.add(cb);
				}
			}
		}
		for (Callback cb : cancelledCB) {
			cb.cancel(true);
		}
	}

	protected int getChargeSpeed() {
		return 100 - 10 * getPropertyValue(KEY_CHARGE_SPEED, 1);
	}

	// PROPERTIES

	private static final String KEY_CHARGE_SPEED = "charge_speed";
	private static final String KEY_IGNORE_CANCEL_ON_DAMAGE = "ignore_cancel_on_damage";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public ComponentProperties build() {
			ComponentProperties retour = new ComponentProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.WOOD,
							new Property<Integer>(KEY_CHARGE_SPEED, new PossibleInt(1, 1, 5, 1),
									PossibleInt.ONE_FOR_ONE))
							.addNewProperty(Grade.GOLD, new Property<Boolean>(KEY_IGNORE_CANCEL_ON_DAMAGE,
									new PossibleBoolean(false), PossibleBoolean.PLUS_ONE_IF_TRUE));
				}
			};
			return retour;
		}
	};

	@Override
	public Cost<?> applyCostMod(Cost<?> in) {
		int chargeTime = (int) this.getPropertyValue(KEY_CHARGE_SPEED, 1);
		in.add(new ManaCost(null, chargeTime));
		return in;
	}

	@Override
	public int applyCDMod(int in) {
		return in;
	}
}
