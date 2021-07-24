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
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractManaModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.ChannelingModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.ChargingModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.LoadingModComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;

public class MainTag {
	public static final Map<Class<? extends AbstractSpellComponent>, MainTag> spellTagMap = new HashMap<Class<? extends AbstractSpellComponent>, MainTag>();

	private Set<Tag> tags;
	private BuildTag buildTag;

	public Set<Tag> getTagSet() {
		return tags;
	}

	public BuildTag getBuildTag() {
		return buildTag;
	}

	public MainTag(BuildTag bTag, OtherTag... tags) {
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

	private static void register(Class<? extends AbstractSpellComponent> clazz, MainTag tags) {
		RuneMain.LOGGER.info("Registering spellTag for " + clazz.getSimpleName());
		if (spellTagMap.containsKey(clazz))
			RuneMain.LOGGER.error("Registering the spellTag for " + clazz.getSimpleName() + " twice !");
		spellTagMap.put(clazz, tags);
	}

	public static MainTag getTags(Class<? extends AbstractSpellComponent> clazz) {
		Class<?> clazz2 = clazz;
		while (clazz2 != null && !spellTagMap.containsKey(clazz2)) {
			clazz2 = clazz2.getSuperclass();
		}
		if (clazz2 == null) {
			return null;
		}
		return spellTagMap.get(clazz2);
	}

	public static MainTag getTags(AbstractSpellComponent component) {
		return getTags(component.getClass());
	}

	public static void register() {
		BuildTag.init();
		SpellTag.init();

		register(AbstractSpellComponent.class, new MainTag(BuildTag.NULL));
		register(AbstractEffectComponent.class, new MainTag(BuildTag.EFFECT));
		register(AbstractCastComponent.class, new MainTag(BuildTag.CAST));
		register(AbstractCastEffectComponent.class, new MainTag(BuildTag.CAST));
		register(AbstractCastModComponent.class, new MainTag(BuildTag.CAST_MOD));
		register(AbstractCastModContainerComponent.class, new MainTag(BuildTag.CAST_MOD));
		
		
		register(LoadingModComponent.class, new MainTag(BuildTag.CAST_MOD, SpellTag.LOADING));
		register(ChargingModComponent.class, new MainTag(BuildTag.CAST_MOD, SpellTag.CHARGING));
		register(ChannelingModComponent.class, new MainTag(BuildTag.CAST_MOD, SpellTag.CHANNELING));

		register(AbstractManaModComponent.class, new MainTag(BuildTag.MANA_MOD));
	}
}