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
import fr.emmuliette.rune.mod.spells.tags.OtherTag;
import fr.emmuliette.rune.mod.spells.tags.RestrictionTag;
import fr.emmuliette.rune.mod.spells.tags.RestrictionTag.Context;
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
				if (!previous.validate(current, Context.BUILD) || !previous.addNextPart(current)) {
					RuneMain.LOGGER.info("[BUILD SPELL] components " + current.getClass().getSimpleName()
							+ " can't go after " + previous.getClass().getSimpleName());
					return null;
				}
			}
			for (Tag tag : current.getTags().getTagSet()) {
				if (tag instanceof SpellTag) {
					spellTags.add((SpellTag) tag);
				} else if (tag instanceof RestrictionTag) {
					// TODO restriction tags
				}
			}
			previous = current;
		}
		if (requiredCastEffect && requiredEffect) {
			return new Spell(name, start, componentList, spellTags);
		}
		if (!requiredCastEffect)
			RuneMain.LOGGER.info("[BUILD SPELL] No required Cast Effect");
		if (!requiredEffect)
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

	public static String parseSpellComponents(List<AbstractSpellComponent> componentsList, boolean isSocket) {
		if (componentsList.size() < 2) {
			return "Must contain at least 2 components";
		}
		boolean requiredCastEffect = false, requiredEffect = false;
		AbstractSpellComponent previous = null;
		for (AbstractSpellComponent current : componentsList) {
			current.clear();
			if (!requiredCastEffect && current instanceof AbstractCastEffectComponent) {
				if (isSocket && !((AbstractCastEffectComponent) current).getTags().hasTag(OtherTag.SOCKETABLE)) {
					return "This casting rune can't be socketed";
				}
				requiredCastEffect = true;
			}
			if (!requiredEffect && current instanceof AbstractEffectComponent) {
				requiredEffect = true;
			}
			if (previous == null) {
				if (!(current instanceof AbstractCastComponent<?>)) {
					return "First rune must be cast rune or cast mod rune";
				}
			} else {
				current.setParent(previous);
				if (!previous.validate(current, Context.BUILD)) {
					return current.getClass().getSimpleName() + " isn't valide";
				}
				if(!previous.addNextPart(current)) {
					return current.getClass().getSimpleName() + " can't be after a " + previous.getClass().getSimpleName();
				}
			}
			previous = current;
		}
		if(!requiredCastEffect)
			return "Require a cast rune";
		
		if(!requiredEffect)
			return "Require at least one effect rune";
		
		return null;
	}
}
