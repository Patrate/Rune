package fr.emmuliette.rune.mod.spells.tags;

import java.util.HashSet;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;

public class MainTag {
	private Set<Tag> tags;
	private BuildTag buildTag;

	public Set<Tag> getTagSet() {
		return tags;
	}

	public BuildTag getBuildTag() {
		return buildTag;
	}

	public MainTag() {
		this.tags = new HashSet<Tag>();
	}

	public boolean hasTag(Tag tag) {
		if (tag instanceof BuildTag)
			return this.buildTag.equals(tag);
		else
			return this.tags.contains(tag);
	}

	public MainTag setTag(Tag tag) {
		if (tag instanceof BuildTag)
			this.buildTag = (BuildTag) tag;
		else
			this.tags.add(tag);
		return this;
	}

	public MainTag removeTag(Tag tag) {
		if (tag instanceof BuildTag) {
			this.buildTag = null;
			RuneMain.LOGGER.error("Removing the buildTag " + tag.getClass().getSimpleName() + " wtf");
		} else
			this.tags.remove(tag);
		return this;
	}

	public static void register() {
		BuildTag.init();
		SpellTag.init();
		OtherTag.init();
	}
}