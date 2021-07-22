package fr.emmuliette.rune.mod.spells.component.castComponent.manaMod;

import com.google.common.base.Function;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractManaModComponent;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleEnum;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleInt;

public class ManaLifeModComponent extends AbstractManaModComponent {
	private static final float MANA_TO_LIFE_RATIO = 0.5f;

	public ManaLifeModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
	}

	@Override
	protected boolean internalCast(SpellContext context) {
		String mode = getMode();
		float cost = getBaseCost();
		try {
			float casterMana = this.getCasterMana(context);
			int availableLife = (int) Math.max(0, context.getCaster().getHealth() - 1);
			int lifeCost;
			int manaCost;
			switch (mode) {
			case MODE_PRIO:
			case MODE_RATIO:
				lifeCost = (int) Math.ceil((getRate() * cost));
				manaCost = (int) (cost - lifeCost);
				lifeCost *= MANA_TO_LIFE_RATIO;
				if (casterMana >= manaCost && availableLife >= lifeCost) {
					delCasterMana(context, manaCost);
					context.getCaster().setHealth(context.getCaster().getHealth() - lifeCost);
					return true;
				}
				return false;
			case MODE_COMPLETE:
			default:
				if (casterMana >= cost) {
					return true;
				}
				float manaOverflow = casterMana - cost;
				manaCost = (int) (cost - manaOverflow);
				lifeCost = (int) Math.ceil(manaOverflow * MANA_TO_LIFE_RATIO);
				if (availableLife >= lifeCost) {
					delCasterMana(context, manaCost);
					context.getCaster().setHealth(context.getCaster().getHealth() - lifeCost);
					return true;
				}
				return false;
			}
		} catch (CasterCapabilityException | NotEnoughManaException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getMode() {
		return getPropertyValue(KEY_MODE, MODE_COMPLETE);
	}

	private float getRate() {
		return ((float) getPropertyValue(KEY_RATE, 0)) / 100;
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
		return Math.max(0, baseCost);
	}

	@Override
	protected void payManaCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
	}

	// PROPERTIES

	private static final String KEY_RATE = "ratio";
	private static final String KEY_MODE = "mode";
	private static final String MODE_RATIO = "RATIO", MODE_COMPLETE = "COMPLETE", MODE_PRIO = "PRIO";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public ComponentProperties build() {
			ComponentProperties retour = new ComponentProperties() {
				@Override
				protected void init() {
					this.addNewProperty(Grade.WOOD, new Property<Integer>(KEY_RATE, new PossibleInt(100, 10, 100, 10),
							new Function<Integer, Float>() {
								@Override
								public Float apply(Integer val) {
									return 0f;
								}
							})).addNewProperty(Grade.WOOD,
									new Property<String>(KEY_MODE,
											new PossibleEnum(MODE_COMPLETE, MODE_RATIO, MODE_COMPLETE, MODE_PRIO),
											new Function<String, Float>() {
												@Override
												public Float apply(String val) {
													return 0f;
												}
											}));
				}
			};
			return retour;
		}
	};
}
