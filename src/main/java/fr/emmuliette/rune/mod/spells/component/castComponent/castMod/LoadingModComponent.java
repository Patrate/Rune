package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.data.client.ModSounds;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.RunePropertiesException;
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
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LoadingModComponent extends AbstractCastModComponent implements CallbackMod {
	private static final Set<Callback> listeningCB = new HashSet<Callback>();

	public LoadingModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
	}

	@Override
	public Callback castCallback(SpellContext context) {
		return new Callback(this, context, getChargeTime(context)) {

			@Override
			public boolean begin() {
				listeningCB.add(this);
				context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
						context.getCaster().getZ(), ModSounds.LOADING_BEGIN, SoundCategory.AMBIENT, 1.0f, 0.4f);
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
					try {
						ICaster cap;
						cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
								.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
						setCooldown(cap, context);
					} catch (CasterCapabilityException e) {
						e.printStackTrace();
					}
				}
				if (result) {
					context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
							context.getCaster().getZ(), ModSounds.LOADING_END, SoundCategory.AMBIENT, 1.0f, 0.4f);
					return true;
				}
				return false;
			}

			@Override
			public boolean tick() {
				return false;
			}
		};
	}

	@SubscribeEvent
	public static void cancelOnRelease(StopCastingEvent event) {
		List<Callback> cancelledCB = new ArrayList<Callback>();
		for (Callback cb : listeningCB) {
			if (cb.getContext().getCaster() == event.getCaster()) {
				cancelledCB.add(cb);
			}
		}
		for (Callback cb : cancelledCB) {
			cb.cancel(true);
		}
	}

	@SubscribeEvent
	public static void cancelOnDamage(LivingDamageEvent event) {
		if (event.getAmount() <= 0) {
			return;
		}
		List<Callback> cancelledCB = new ArrayList<Callback>();
		for (Callback cb : listeningCB) {
			if (cb.getParent() instanceof LoadingModComponent) {
				if (cb.getContext().getCaster() == event.getEntityLiving() && !((LoadingModComponent) cb.getParent())
						.getBoolProperty(KEY_IGNORE_CANCEL_ON_DAMAGE)) {
					cancelledCB.add(cb);
				}
			}
		}
		for (Callback cb : cancelledCB) {
			cb.cancel(true);
		}
	}

	// PROPERTIES

	private static final String KEY_CHARGE_TIME = "charge_time";
	private static final String KEY_IGNORE_CANCEL_ON_DAMAGE = "ignore_cancel_on_damage";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties build() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.WOOD, new LevelProperty(KEY_CHARGE_TIME, 10, () -> new ManaCost(1)))
							.addNewProperty(Grade.GOLD, new BoolProperty(KEY_IGNORE_CANCEL_ON_DAMAGE, () -> new ManaCost(10)));
				}
			};
			return retour;
		}
	};

	@Override
	public Cost<?> applyCostMod(Cost<?> in) {
		int chargeTime = (int) this.getIntProperty(KEY_CHARGE_TIME);
		in.remove(new ManaCost(chargeTime));
		return in;
	}

	private int getChargeTime(SpellContext context) {
		return 100 * this.getIntProperty(KEY_CHARGE_TIME, context.getPower());
	}

	@Override
	public int applyCDMod(int in) {
		return (int) (in * 0.8);
	}

}
