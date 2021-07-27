package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import fr.emmuliette.rune.mod.spells.properties.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChannelingModComponent extends AbstractCastModComponent implements CallbackMod {
	private static final Set<Callback> listeningCB = new HashSet<Callback>();

	public ChannelingModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
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
				modulo = ((ChannelingModComponent) getParent()).getCastSpeed(context);
				listeningCB.add(this);
				context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
						context.getCaster().getZ(), ModSounds.CHANNELING_BEGIN, SoundCategory.AMBIENT, 1.0f, 0.4f);
				return true;
			}

			@Override
			public boolean _callBack() {
				return false;
			}

			@Override
			public boolean finalize(boolean result) {
				if(context.getCaster() instanceof LivingEntity) {
					if (((LivingEntity) context.getCaster()).isUsingItem())
						((LivingEntity) context.getCaster()).stopUsingItem();
				}
				return result;
			}

			@Override
			public boolean tick() {
				if (++tick >= modulo) {
					tick = 0;
					try {
						ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
								.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
						Cost<?> cost = getCost();
						if (cost.canPay(cap, context)) {
							payCost(cap, context);
							context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
									context.getCaster().getZ(), ModSounds.CHANNELING_TICK, SoundCategory.AMBIENT, 1.0f,
									0.4f);
							return castChildren();
						} else {
							return false;
						}
					} catch (CasterCapabilityException | NotEnoughManaException e) {
						e.printStackTrace();
						return false;
					}
				}
				return true;
			}

		};
	}

	@SubscribeEvent
	public static void stopOnRelease(StopCastingEvent event) {
		List<Callback> finishedCB = new ArrayList<Callback>();
		Iterator<Callback> cbIterator = listeningCB.iterator();
		while (cbIterator.hasNext()) {
			Callback cb = cbIterator.next();
			if (cb.getContext().getCaster() == event.getCaster()) {
				finishedCB.add(cb);
				cbIterator.remove();
			}
		}
		for (Callback cb : finishedCB) {
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
			if (cb.getParent() instanceof ChannelingModComponent) {
				if (cb.getContext().getCaster() == event.getEntityLiving() && !((ChannelingModComponent) cb.getParent())
						.getBoolProperty(KEY_IGNORE_CANCEL_ON_DAMAGE)) {
					cancelledCB.add(cb);
				}
			}
		}
		for (Callback cb : cancelledCB) {
			cb.cancel(true);
		}
	}

	protected int getCastSpeed(SpellContext context) {
		return 75 - 10 * getIntProperty(KEY_CAST_SPEED, context.getPower());
	}

	// PROPERTIES

	private static final String KEY_CAST_SPEED = "cast_speed";
	private static final String KEY_IGNORE_CANCEL_ON_DAMAGE = "ignore_cancel_on_damage";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties build() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.WOOD, new LevelProperty(KEY_CAST_SPEED, 10, () -> new ManaCost(1))).addNewProperty(Grade.GOLD,
							new BoolProperty(KEY_IGNORE_CANCEL_ON_DAMAGE, () -> new ManaCost(10)));
				}
			};
			return retour;
		}
	};

	@Override
	public Cost<?> applyCostMod(Cost<?> in) {
		int chargeTime = (int) this.getIntProperty(KEY_CAST_SPEED);
		in.add(new ManaCost(chargeTime));
		return in;
	}

	@Override
	public int applyCDMod(int in) {
		return in;
	}
}
