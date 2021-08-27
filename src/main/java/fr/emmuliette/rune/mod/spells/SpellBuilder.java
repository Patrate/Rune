package fr.emmuliette.rune.mod.spells;

import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import fr.emmuliette.rune.mod.spells.tags.RestrictionTag;
import fr.emmuliette.rune.mod.spells.tags.SpellTag;
import fr.emmuliette.rune.mod.spells.tags.Tag;

public class SpellBuilder {

	public static Spell buildSpell(String name, List<AbstractSpellComponent> componentList)
			throws RunePropertiesException {
		if (componentList.size() < 2) {
			RuneMain.LOGGER.info("[BUILD SPELL] components list is too small: " + componentList.size());
			return null;
		}
		boolean requiredCastEffect = false, requiredEffect = false;
		AbstractCastComponent<?> start = null;
		AbstractSpellComponent previous = null;
		List<SpellTag> spellTags = new ArrayList<SpellTag>();
		for (AbstractSpellComponent current : componentList) {
			current.clear();
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
				if (!previous.validate(current) || !previous.addNextPart(current)) {
					return null;
				}
			}
			for(Tag tag:current.getTags().getTagSet()) {
				if(tag instanceof SpellTag) {
					spellTags.add((SpellTag) tag);
				} else if(tag instanceof RestrictionTag) {
					// TODO
				}
			}
			previous = current;
		}
		if (requiredCastEffect && requiredEffect) {
			return new Spell(name, start, componentList, spellTags);
		}
		if(!requiredCastEffect)
			RuneMain.LOGGER.info("[BUILD SPELL] No required Cast Effect");
		if(!requiredEffect)
			RuneMain.LOGGER.info("[BUILD SPELL] No required Effect");
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
		return parseSpellComponents(runeListToComponents(runeList));
	}
	
	public static boolean parseSpellComponents(List<AbstractSpellComponent> componentsList) {
		if (componentsList.size() < 2) {
			return false;
		}
		boolean requiredCastEffect = false, requiredEffect = false;
		AbstractSpellComponent previous = null;
		for (AbstractSpellComponent current:componentsList) {
			current.clear();
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
				if (!previous.validate(current) || !previous.addNextPart(current)) {
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
