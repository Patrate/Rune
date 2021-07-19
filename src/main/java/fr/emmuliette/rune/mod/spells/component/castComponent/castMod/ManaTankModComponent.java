package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import com.google.common.base.Function;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.exception.SpellCapabilityException;
import fr.emmuliette.rune.exception.SpellCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.capability.ISpell;
import fr.emmuliette.rune.mod.spells.capability.SpellCapability;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleInt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class ManaTankModComponent extends AbstractCastModComponent {

	public ManaTankModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
	}

	@Override
	protected Callback modInternalCast(SpellContext context) {
		return new Callback(this, context) {

			@Override
			public boolean _callBack() {
				int currentMana = ((ManaTankModComponent) this.getParent()).getCurrentMana(context);
				int cost = (int) Math.ceil(getBaseCost());
				if (currentMana >= cost) {
					setCurrentMana(currentMana - cost, context);
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
						setCurrentMana(currentMana + manaSaved, context);
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

	private int getCurrentMana() {
		return getPropertyValue(KEY_CURRENT_MANA, 8);
	}

	private int getCurrentMana(SpellContext context) {
		int defaut = 8;
		try {
			ItemStack item = context.getItemStack();
			if (item == null) {
				RuneMain.LOGGER.error("item est null (getCurrentMana)");
				return defaut;
			}
			ISpell ispell = item.getCapability(SpellCapability.SPELL_CAPABILITY)
					.orElseThrow(new SpellCapabilityExceptionSupplier(context.getItemStack()));
			return ispell.getSpell().getPropertyValue(this.getSpellInternalId(), KEY_CURRENT_MANA, defaut);
		} catch (SpellCapabilityException e) {
			e.printStackTrace();
		}
		return defaut;
	}

	private void setCurrentMana(int currentMana, SpellContext context) {
		try {
			ItemStack item = context.getItemStack();
			if (item == null) {
				RuneMain.LOGGER.error("item est null (setCurrentMana)");
				return;
			}
			ISpell ispell = item.getCapability(SpellCapability.SPELL_CAPABILITY)
					.orElseThrow(new SpellCapabilityExceptionSupplier(context.getItemStack()));
			ispell.getSpell().setPropertyValue(this.getSpellInternalId(), KEY_CURRENT_MANA, currentMana);
		} catch (SpellCapabilityException e) {
			e.printStackTrace();
		}
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
		float baseCost = getBaseCost();
		int currentMana = getCurrentMana();
		return Math.max(0, baseCost - currentMana);
	}

	private float getBaseCost() {
		float totalCost = 0f;
		for (AbstractSpellComponent sc : getChildrens()) {
			totalCost += sc.getManaCost();
		}
		return totalCost;
	}

	@Override
	protected void payManaCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
		// cap.delMana(this.getManaCost());
	}

	@Override
	public float applyManaMod(float in) {
		return in;
	}

	@Override
	public int applyCDMod(int in) {
		return in;
	}
}
