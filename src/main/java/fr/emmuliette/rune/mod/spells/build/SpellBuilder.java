package fr.emmuliette.rune.mod.spells.build;

import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;

public class SpellBuilder {

	public static Spell buildSpell(String name, List<AbstractSpellComponent> componentList)
			throws RunePropertiesException {
		if (componentList.size() < 2) {
			return null;
		}
		boolean requiredCastEffect = false, requiredEffect = false;
		AbstractCastComponent<?> start = null;
		AbstractSpellComponent previous = null;
		for (AbstractSpellComponent current : componentList) {
			if (!requiredCastEffect && current instanceof AbstractCastEffectComponent) {
				requiredCastEffect = true;
			}
			if (!requiredEffect && current instanceof AbstractEffectComponent) {
				requiredEffect = true;
			}
			if (previous == null) {
				start = (AbstractCastComponent<?>) current;
			} else {
				current.setParent(previous);
				if (!previous.checkNextPart(current) || !previous.addNextPart(current)) {
					RuneMain.LOGGER.error("INVALID NEXT PART: " + current.getClass().getSimpleName()
							+ " can't go after " + previous.getClass().getSimpleName());
					return null;
				}
			}
			previous = current;
		}
		if (requiredCastEffect && requiredEffect) {
			return new Spell(name, start, componentList);
		}
		return null;
	}

	public static Spell runeToSpell(String name, List<RuneItem> runeList) throws RunePropertiesException {
		return buildSpell(name, runeListToComponents(runeList));
	}

	private static List<AbstractSpellComponent> runeListToComponents(List<RuneItem> runeList) {
		List<AbstractSpellComponent> retour = new ArrayList<AbstractSpellComponent>();
		for (RuneItem rItem : runeList) {
			retour.add(rItem.getSpellComponent());
		}
		return retour;
	}

	public static boolean parseSpell(List<RuneItem> runeList) {
		if (runeList.size() < 2) {
			return false;
		}
		boolean requiredCastEffect = false, requiredEffect = false;
		AbstractSpellComponent current;
		AbstractSpellComponent previous = null;
		for (RuneItem runeItem : runeList) {
			current = runeItem.getSpellComponent();
			if (!requiredCastEffect && current instanceof AbstractCastEffectComponent) {
				requiredCastEffect = true;
			}
			if (!requiredEffect && current instanceof AbstractEffectComponent) {
				requiredEffect = true;
			}
			if (previous == null) {
				if (!(current instanceof AbstractCastComponent<?>)) {
					return false;
				}
			} else {
				current.setParent(previous);
				if (!previous.checkNextPart(current) || !previous.addNextPart(current)) {
					// RuneMain.LOGGER.error("INVALID NEXT PART: " +
					// current.getClass().getSimpleName() + " can't go after " +
					// previous.getClass().getSimpleName());
					return false;
				}
			}
			previous = current;
		}
		return (requiredCastEffect && requiredEffect);
	}
}
