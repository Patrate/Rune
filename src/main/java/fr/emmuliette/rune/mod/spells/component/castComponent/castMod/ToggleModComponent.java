package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import java.util.HashMap;
import java.util.Map;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.data.client.ModSounds;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.Callback;
import fr.emmuliette.rune.mod.spells.component.castComponent.CallbackManager;
import fr.emmuliette.rune.mod.spells.component.castComponent.CallbackMod;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ToggleModComponent extends AbstractCastModComponent implements CallbackMod {
	private Callback listeningCB = null;

	public ToggleModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
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
				modulo = ((ToggleModComponent) getParent()).getCastSpeed(context);
				listeningCB = this;
				context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
						context.getCaster().getZ(), ModSounds.TOGGLE_BEGIN, SoundCategory.AMBIENT, 1.0f, 0.4f);
				return true;
			}

			@Override
			public boolean _callBack() {
				return false;
			}

			@Override
			public boolean finalize(boolean result) {
				listeningCB = null;
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
	
	@Override
	public Boolean canCast(SpellContext context) {
		if(isActive()) {
			return true;
		}
		return super.canCast(context);
	}
	
	@Override
	public boolean cast(SpellContext context) {
		if(isActive()) {
			return toggleOff(context);
		} else {
			return toggleOn(context);
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
		public RuneProperties buildInternal() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					Map<Grade, Integer> durationLevels = new HashMap<Grade, Integer>();
					durationLevels.put(Grade.WOOD, 2);
					durationLevels.put(Grade.IRON, 3);
					durationLevels.put(Grade.NETHERITE, 7);

					this.addNewProperty(new LevelProperty(KEY_CAST_SPEED, durationLevels, () -> new ManaCost(1)));
					this.addNewProperty(
							new BoolProperty(KEY_IGNORE_CANCEL_ON_DAMAGE, Grade.GOLD, () -> new ManaCost(10)));
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
	
	private boolean toggleOn(SpellContext context) {
		Callback cb = ((CallbackMod) this).castCallback(context);
		if (cb != null) {
			try {
				payCost(context);
				cb.begin();
				CallbackManager.register(cb);
				return true;
			} catch (NotEnoughManaException | CasterCapabilityException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private boolean toggleOff(SpellContext context) {
		this.listeningCB.finish(true);
		return false;
	}
	
	private boolean isActive() {
		return listeningCB != null;
	}
}
