package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
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
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChargingModComponent extends AbstractCastModComponent implements CallbackMod {
	private static final Set<Callback> listeningCB = new HashSet<Callback>();

	public ChargingModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
	}

	@Override
	public Callback castCallback(SpellContext context) {
		return new Callback(this, context, -1, true) {
			private int tick;
			private int modulo;
			
			@Override
			public boolean begin() {
				tick = 0;
				modulo = ((ChargingModComponent) getParent()).getChargeSpeed();
				listeningCB.add(this);
				context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
						context.getCaster().getZ(), SoundEvents.CHAIN_PLACE, SoundCategory.AMBIENT, 1.0f, 0.4f);
				return true;
			}

			@Override
			public boolean _callBack() {
				return true;
			}

			@Override
			public boolean finalize(boolean result) {
				if (context.getCaster().isUsingItem())
					context.getCaster().stopUsingItem();
				if (result)
					context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
							context.getCaster().getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1.0f,
							0.4f);
				return result;
			}

			@Override
			public boolean tick() {
				++tick;
				if(tick == modulo) {
					tick = 0;
					try {
						ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
								.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
						Cost<?> cost = getCost();
						if(cost.canPay(cap, context)) {
							System.out.println("CHARGE ++");
							payCost(cap, context);
							context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
									context.getCaster().getZ(), SoundEvents.BOTTLE_EMPTY, SoundCategory.AMBIENT, 1.0f,
									0.4f);
							return true;
						} else {
							return false;
						}
					} catch (CasterCapabilityException | NotEnoughManaException e) {
						e.printStackTrace();
					}
				}
				return true;
			}

		};
	}
	
	@SubscribeEvent
	public static void castOnRelease(StopCastingEvent event) {
		List<Callback> finishedCB = new ArrayList<Callback>();
		for (Callback cb : listeningCB) {
			if (cb.getContext().getCaster() == event.getCaster()) {
				finishedCB.add(cb);
			}
		}
		for (Callback cb : finishedCB) {
			// Number of tick divided by tickCount for 1 power
			cb.getContext().setPower(event.getCount()
					/ ((ChargingModComponent) cb.getParent()).getChargeSpeed());
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
		return 100 - 10	 * getPropertyValue(KEY_CHARGE_SPEED, 1);
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
							new Property<Integer>(KEY_CHARGE_SPEED, new PossibleInt(1, 1, 5, 1), PossibleInt.ONE_FOR_ONE))
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
