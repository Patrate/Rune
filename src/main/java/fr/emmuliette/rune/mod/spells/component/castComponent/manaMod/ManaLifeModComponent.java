package fr.emmuliette.rune.mod.spells.component.castComponent.manaMod;

import java.util.HashMap;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractManaModComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.LifeCost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.ComponentProperties;
import fr.emmuliette.rune.mod.spells.properties.EnumProperty;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;

public class ManaLifeModComponent extends AbstractManaModComponent {
	private static final float MANA_TO_LIFE_RATIO = 0.5f;

	public ManaLifeModComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PROPFACT, parent);
	}

	/*
	 * @Override protected boolean internalCast(SpellContext context) { String mode
	 * = getMode(); float cost = getBaseCost().getLifeCost(); try { float casterMana
	 * = this.getCasterMana(context); int availableLife = (int) Math.max(0,
	 * context.getCaster().getHealth() - 1); int lifeCost; int manaCost; switch
	 * (mode) { case MODE_PRIO: case MODE_RATIO: lifeCost = (int)
	 * Math.ceil((getRate() * cost)); manaCost = (int) (cost - lifeCost); lifeCost
	 * *= MANA_TO_LIFE_RATIO; if (casterMana >= manaCost && availableLife >=
	 * lifeCost) { delCasterMana(context, manaCost);
	 * context.getCaster().setHealth(context.getCaster().getHealth() - lifeCost);
	 * return true; } return false; case MODE_COMPLETE: default: if (casterMana >=
	 * cost) { return true; } float manaOverflow = casterMana - cost; manaCost =
	 * (int) (cost - manaOverflow); lifeCost = (int) Math.ceil(manaOverflow *
	 * MANA_TO_LIFE_RATIO); if (availableLife >= lifeCost) { delCasterMana(context,
	 * manaCost); context.getCaster().setHealth(context.getCaster().getHealth() -
	 * lifeCost); return true; } return false; } } catch (CasterCapabilityException
	 * | NotEnoughManaException e) { e.printStackTrace(); } return false; }
	 */

	@Override
	public Cost<?> applyCostMod(Cost<?> in) {
		float oldMana = in.getManaCost();
		float lifeCost = oldMana * this.getRate();
		ManaCost removedMana = new ManaCost(lifeCost);
		LifeCost addedLife = new LifeCost(null, lifeCost * MANA_TO_LIFE_RATIO);
		in.add(addedLife);
		in.remove(removedMana);
		return in;
	}

//	private String getMode() {
//		return getPropertyValue(KEY_MODE, MODE_RATIO);
//	}

	private float getRate() {
		return ((float) getIntProperty(KEY_RATE)) / 100;
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
					HashMap<String, Cost<?>> modeMap = new HashMap<String, Cost<?>>();
					modeMap.put(MODE_RATIO, Cost.getZeroCost());
					modeMap.put(MODE_COMPLETE, Cost.getZeroCost());
					modeMap.put(MODE_PRIO, Cost.getZeroCost());
					this.addNewProperty(Grade.WOOD, new LevelProperty(KEY_RATE, 10, new ManaCost(1)))
						.addNewProperty(Grade.WOOD, new EnumProperty(KEY_MODE, MODE_COMPLETE, modeMap));
				}
			};
			return retour;
		}
	};
}
