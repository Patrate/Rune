package fr.emmuliette.rune.mod.spells.component.castComponent.manaMod;

import com.google.common.base.Function;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.data.client.ModSounds;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.exception.SpellCapabilityException;
import fr.emmuliette.rune.exception.SpellCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.capability.ISpell;
import fr.emmuliette.rune.mod.spells.capability.SpellCapability;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractManaModComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleInt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class ManaTankModComponent extends AbstractManaModComponent {

	public ManaTankModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
	}

	@Override
	protected boolean internalCast(SpellContext context) {
		int currentMana = getCurrentMana(context);
		int cost = (int) Math.ceil(getBaseCost().getManaCost());
		if (currentMana >= cost) {
			try {
				setCurrentMana(currentMana - cost, context);
				setCooldown(context);
				return true;
			} catch (CasterCapabilityException e) {
				e.printStackTrace();
				return false;
			}
		}
		try {
			int remaining = (int) Math.ceil(getCost().getManaCost());
			int manaSaved = (int) Math.min(getCasterMana(context), remaining);
			if (manaSaved > 0) {
				context.getWorld().playSound(null, context.getCaster().getX(), context.getCaster().getY(),
						context.getCaster().getZ(), ModSounds.MANA_TANK_FILL, SoundCategory.AMBIENT, 1.0f, 0.4f);
				delCasterMana(context, (float) manaSaved);
				setCurrentMana(currentMana + manaSaved, context);
			}
		} catch (CasterCapabilityException | NotEnoughManaException e) {
			System.err.println("CAN'T COLLECT MANA");
			e.printStackTrace();
		}
		return false;
	}

	private int getCurrentMana() {
		return getPropertyValue(KEY_CURRENT_MANA, 8);
	}

	private int getCurrentMana(SpellContext context) {
		int defaut = 8;
		ItemStack item = context.getItemStack();
		if (item == null) {
			RuneMain.LOGGER.error("item est null (getCurrentMana)");
			return defaut;
		}

		return this.getPropertyValue(KEY_CURRENT_MANA, defaut);
		/*
		 * ISpell ispell = item.getCapability(SpellCapability.SPELL_CAPABILITY)
		 * .orElseThrow(new SpellCapabilityExceptionSupplier(context.getItemStack()));
		 * return ispell.getSpell().getPropertyValue(this.getSpellInternalId(),
		 * KEY_CURRENT_MANA, defaut);
		 */
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
	protected Boolean checkCost(ICaster cap, SpellContext context) {
		if (this.getCost().getManaCost() <= cap.getMana()) {
			if (cap.getMana() >= 1) {
				return null;
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public Cost<?> getCost() {
		ManaCost paid = new ManaCost(null, this.getCurrentMana());
		Cost<?> supCost = super.getCost();
		supCost.remove(paid);
		return supCost;
	}

	@Override
	public Cost<?> applyCostMod(Cost<?> in) {
		in.add(new ManaCost(null, (float) Math.ceil(in.getManaCost() * 0.3)));
		return super.applyCostMod(in);
	}

	@Override
	protected void payCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
		super.payCost(cap, context);
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
}
