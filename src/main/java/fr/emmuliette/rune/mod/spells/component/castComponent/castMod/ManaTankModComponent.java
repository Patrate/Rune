package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import com.google.common.base.Function;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleInt;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class ManaTankModComponent extends AbstractCastModComponent {

	public ManaTankModComponent() throws RunePropertiesException {
		super(PROPFACT);
	}

	@Override
	protected Callback modInternalCast(SpellContext context) {
		return new Callback(this, context) {

			@Override
			public boolean _callBack() {
				int currentMana = (int) Math.floor(((ManaTankModComponent) this.getParent()).getCurrentMana());
				int cost = (int) Math.ceil(getBaseCost());
				if (currentMana >= cost) {
					setCurrentMana(currentMana - cost);
					return true;
				}
				try {
					ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
							.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
					int remaining = (int) Math.ceil(getManaCost());
					int manaSaved = (int) Math.min(cap.getMana(), remaining);
					if (manaSaved > 0) {
						context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
								context.getCaster().getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.AMBIENT, 1.0f, 0.4f);
						cap.delMana((float) manaSaved);
						setCurrentMana(currentMana + manaSaved);
					}
				} catch (CasterCapabilityException | NotEnoughManaException e) {
					System.err.println("CAN'T COLLECT MANA");
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public boolean begin() { // SKIP
				return true;
			}

			@Override
			public boolean finalize(boolean result) { // SKIP
				return true;
			}

			@Override
			public boolean tick() { // SKIP
				System.err.println("Method tick of Class ManaTankModComponent.Callback should never be called");
				return false;
			}
		};
	}

	// PROPERTIES

	private static final String KEY_CURRENT_MANA = "current_mana";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public ComponentProperties build() {
			ComponentProperties retour = new ComponentProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.SECRET,
							new Property<Integer>(KEY_CURRENT_MANA, new PossibleInt(), new Function<Integer, Float>() {
								@Override
								public Float apply(Integer val) {
									return 0f;
								}
							}));
				}
			};
			return retour;
		}
	};

	private float getCurrentMana() {
		return (float) this.getPropertyValue(KEY_CURRENT_MANA, 0);
	}

	private void setCurrentMana(int currentMana) {
		getProperties().getProperty(KEY_CURRENT_MANA).setValue(currentMana);
	}

	@Override
	protected Boolean checkManaCost(ICaster cap, SpellContext context) {
		if (this.getManaCost() <= cap.getMana()) {
			if (cap.getMana() >= 1) {
				return null;
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public float getManaCost() {
		float baseCost = super.getManaCost();
		int currentMana = (int) Math.floor(getCurrentMana());
		return Math.max(0, baseCost - currentMana);
	}

	private float getBaseCost() {
		return super.getManaCost();
	}

	@Override
	protected void payManaCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
		// cap.delMana(this.getManaCost());
	}

	@Override
	public float applyManaMod(float in) {
		int currentMana = (int) Math.floor(getCurrentMana());
		return Math.max(0, in - currentMana);
	}

	@Override
	public int applyCDMod(int in) {
		return in;
	}
}
