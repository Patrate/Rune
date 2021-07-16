package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Function;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.DuplicatePropertyException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.SpellProperties;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleInt;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChargingModComponent extends AbstractCastModComponent {
	private static final Set<ChargingModComponent.Callback> listeningCB = new HashSet<ChargingModComponent.Callback>();

	public ChargingModComponent() throws RunePropertiesException {
		super();
	}
	
	@Override
	protected Callback modInternalCast(SpellContext context) {
		return new Callback(this, context, this.getProperty(KEY_CHARGE_TIME, new Integer(0)).getValue()) {
			private float oldSpeed = 0;

			@Override
			public boolean begin() {
				oldSpeed = context.getCaster().getSpeed();
				context.getCaster().setSpeed(oldSpeed / 10);
				context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
						context.getCaster().getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 1.0f, 0.4f);
				return true;
			}

			@Override
			public boolean _callBack() {
				return true;
			}

			@Override
			public boolean finalize(boolean result) {
				context.getCaster().setSpeed(oldSpeed);
				context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
						context.getCaster().getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1.0f, 0.4f);
				return true;
			}

			@Override
			public boolean tick() {
				return false;
			}
			
			@Override
			public void register() {
				super.register();
				listeningCB.add(this);
			}
			
			@Override
			public void unregister() {
				super.unregister();
				if(listeningCB.contains(this)) {
					listeningCB.remove(this);
				}
			}

		};
	}

	@SubscribeEvent
	public static void cancelOnDamage(LivingDamageEvent event) {
		if(event.getAmount() <= 0) {
			return;
		}
		for(ChargingModComponent.Callback cb:listeningCB) {
			if(cb.getContext().getCaster() == event.getEntityLiving()) {
				cb.cancel(true);
			}
		}
	}
	
	// PROPERTIES
	
	
	private static final String KEY_CHARGE_TIME = "charge_time";
	private static SpellProperties DEFAULT_PROPERTIES;
	{
		if (DEFAULT_PROPERTIES == null) {
			try {
				DEFAULT_PROPERTIES = new SpellProperties();
				DEFAULT_PROPERTIES.addNewProperty(Grade.WOOD, new Property<Integer>(KEY_CHARGE_TIME,
						new PossibleInt(100, 100, 600, 10), new Function<Integer, Float>() {
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
	
	public float applyManaMod(float in) {
		int chargeTime = this.getProperty(KEY_CHARGE_TIME, new Integer(0)).getValue();
		return in - (chargeTime/100);
	}
	public int applyCDMod(int in) {
		return in;
	}

}
