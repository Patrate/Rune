package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.tags.BuildTag;

public abstract class AbstractCastEffectComponent extends AbstractCastComponent<AbstractEffectComponent> {
	private List<AbstractEffectComponent> children;

	public AbstractCastEffectComponent(PropertyFactory propFactory, AbstractSpellComponent parent)
			throws RunePropertiesException {
		super(propFactory, parent);
		children = new ArrayList<AbstractEffectComponent>();
		this.addTag(BuildTag.CAST);
	}

	@Override
	public int getMaxSize() {
		return 999;
	}

	@Override
	public List<AbstractEffectComponent> getChildrens() {
		return children;
	}

	@Override
	public int getSize() {
		return children.size();
	}

	@Override
	public boolean canAddChildren(AbstractSpellComponent children) {
		return (children instanceof AbstractEffectComponent);
	}
	
	public void clearChildrens() {
		this.children.clear();
	}
}
