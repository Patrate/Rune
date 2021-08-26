package fr.emmuliette.rune.mod.spells.component.structureComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import fr.emmuliette.rune.mod.spells.component.structureComponent.AI.AbstractAIComponent;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.entities.MagicEntity;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import fr.emmuliette.rune.mod.spells.tags.BuildTag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MagicEntityComponent<T extends AbstractSpellComponent> extends AbstractEffectComponent
		implements ComponentContainer<AbstractSpellComponent> {
	private AbstractCastEffectComponent children;
	private List<AbstractAIComponent> magicEntityAI;

	public MagicEntityComponent(AbstractSpellComponent parent) {
		super(PROPFACT, parent);
		magicEntityAI = new ArrayList<AbstractAIComponent>();
		this.addTag(BuildTag.MAGIC_ENTITY);
	}

	private MagicEntity summonMagicEntity(World world, BlockPos position, SpellContext context) {
		MagicEntity me = new MagicEntity(context, this, world, position);
		world.addFreshEntity(me);
		return me;
	}

	public void castChildren(SpellContext context) {
		children.cast(context, false);
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		MagicEntity me = summonMagicEntity(target.level, target.blockPosition(), context);
		me.setTarget(target);
		return true;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		summonMagicEntity(world, position, context);
		return true;
	}

	// PROPERTIES

	private static final String KEY_DURATION = "duration";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties buildInternal() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					Map<Grade, Integer> durationLevels = new HashMap<Grade, Integer>();
					durationLevels.put(Grade.WOOD, 2);
					durationLevels.put(Grade.IRON, 3);
					durationLevels.put(Grade.NETHERITE, 7);

					this.addNewProperty(new LevelProperty(KEY_DURATION, durationLevels, () -> new ManaCost(1), true));
				}
			};
			return retour;
		}
	};

	@Override
	public int getMaxSize() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getSize() {
		return (children == null) ? 0 : 1 + magicEntityAI.size();
	}

	@Override
	public boolean canAddChildren(AbstractSpellComponent newChild) {
		return (getSize() < getMaxSize() && (newChild instanceof AbstractCastEffectComponent)
				|| (newChild instanceof AbstractAIComponent));
	}

	@Override
	public boolean addChildren(AbstractSpellComponent newChild) {
		if (this.children == null && newChild instanceof AbstractCastEffectComponent) {
			this.children = (AbstractCastEffectComponent) newChild;
			return true;
		}
		if (newChild instanceof AbstractAIComponent) {
			this.magicEntityAI.add((AbstractAIComponent) newChild);
			return true;
		}
		return false;
	}

	@Override
	public List<AbstractSpellComponent> getChildrens() {
		List<AbstractSpellComponent> retour = new ArrayList<AbstractSpellComponent>();
		if (children != null) {
			retour.add(children);
		}
		retour.addAll(this.magicEntityAI);
		return retour;
	}

	public AbstractCastEffectComponent getCastChildren() {
		return children;
	}

	public List<AbstractAIComponent> getAIChildren() {
		return this.magicEntityAI;
	}

	@Override
	public boolean addNextPart(AbstractSpellComponent other) {
		if (isStartingComponent()) {
			return false;
		}
		return addChildren(other);
	}

	@Override
	public void clear() {
		clearChildrens();
	}

	@Override
	public void clearChildrens() {
		this.children.clear();
	}
}
