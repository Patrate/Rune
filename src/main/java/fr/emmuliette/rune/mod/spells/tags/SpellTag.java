package fr.emmuliette.rune.mod.spells.tags;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModContainerComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.ManaTankModComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;

public class SpellTag {
	public static final Map<Class<? extends AbstractSpellComponent>, SpellTag> spellTagMap = new HashMap<Class<? extends AbstractSpellComponent>, SpellTag>();

	private Set<Tag> tags;
	private BuildTag buildTag;

	public Set<Tag> getTagSet() {
		return tags;
	}

	public BuildTag getBuildTag() {
		return buildTag;
	}

	public SpellTag(BuildTag bTag, OtherTag... tags) {
		if (bTag == null) {
			RuneMain.LOGGER.error("BuildTag cannot be null !");
		}
		this.tags = new HashSet<Tag>();
		if (bTag != null) {
			buildTag = bTag;
		}
		for (OtherTag f : tags) {
			this.tags.add(f);
		}
	}

	private static void register(Class<? extends AbstractSpellComponent> clazz, SpellTag tags) {
		RuneMain.LOGGER.info("Registering spellTag for " + clazz.getSimpleName());
		if (spellTagMap.containsKey(clazz))
			RuneMain.LOGGER.error("Registering the spellTag for " + clazz.getSimpleName() + " twice !");
		spellTagMap.put(clazz, tags);
	}

	public static SpellTag getTags(Class<? extends AbstractSpellComponent> clazz) {
		Class<?> clazz2 = clazz;
		while (clazz2 != null && !spellTagMap.containsKey(clazz2)) {
			clazz2 = clazz2.getSuperclass();
		}
		if (clazz2 == null) {
			return null;
		}
		return spellTagMap.get(clazz2);
	}

	public static SpellTag getTags(AbstractSpellComponent component) {
		return getTags(component.getClass());
	}

	public static void register() {
		BuildTag.init();
		
		register(AbstractSpellComponent.class, new SpellTag(BuildTag.NULL));
		register(AbstractEffectComponent.class, new SpellTag(BuildTag.EFFECT));
		register(AbstractCastModContainerComponent.class, new SpellTag(BuildTag.CAST_MOD));
		register(AbstractCastModComponent.class, new SpellTag(BuildTag.CAST_MOD));
		register(AbstractCastEffectComponent.class, new SpellTag(BuildTag.CAST));
		register(AbstractCastComponent.class, new SpellTag(BuildTag.CAST));
		register(ManaTankModComponent.class, new SpellTag(BuildTag.MANA_MOD));
	}
}