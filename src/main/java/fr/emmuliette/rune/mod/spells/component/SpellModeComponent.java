package fr.emmuliette.rune.mod.spells.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import fr.emmuliette.rune.mod.spells.properties.common.StringProperty;
import fr.emmuliette.rune.mod.spells.tags.RestrictionTag.Context;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellModeComponent extends AbstractSpellComponent implements ComponentContainer<AbstractSpellComponent> {
	private Map<String, AbstractSpellComponent> childrenModes;
	private List<AbstractSpellComponent> childrens;

	public SpellModeComponent(AbstractSpellComponent parent) {
		super(PROPFACT, parent);
		this.childrenModes = new HashMap<String, AbstractSpellComponent>();
		childrens = new ArrayList<AbstractSpellComponent>();
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		if (!childrenModes.containsKey(context.getMode())) {
			System.err.println("Le mode " + context.getMode() + " n'existe pas !!!");
			return false;
		}
		return childrenModes.get(context.getMode()).applyOnTarget(target, context);
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos target, SpellContext context) {
		if (!childrenModes.containsKey(context.getMode())) {
			System.err.println("Le mode " + context.getMode() + " n'existe pas !!!"); 
			return false;
		}
		return childrenModes.get(context.getMode()).applyOnPosition(world, target, context);
	}

	@Override
	public boolean addNextPart(AbstractSpellComponent other) {
		if (other instanceof SpellModeComponent) {
			childrenModes.put(other.getStringProperty(KEY_NAME), other);
			return true;
		} else {
			return addChildren(other);
		}
	}

	@Override
	public boolean validate(Context context) {
		if(context == Context.BUILD)
			childrenModes.put(this.getStringProperty(KEY_NAME), this);
		return super.validate(context);
	}
	
	@Override
	public void clear() {
	}

	private static final String KEY_NAME = "name";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties buildInternal() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					this.addNewProperty(new StringProperty(KEY_NAME, Grade.WOOD).setDescription("Mode name"));
				}
			};
			return retour;
		}
	};

	@Override
	public int getMaxSize() {
		return 999;
	}

	@Override
	public List<AbstractSpellComponent> getChildrens() {
		return childrens;
	}

	@Override
	public int getSize() {
		return childrens.size();
	}

	@Override
	public boolean canAddChildren(AbstractSpellComponent children) {
		if (childrens.isEmpty())
			return (children instanceof AbstractEffectComponent) || (children instanceof AbstractCastComponent);
		if (childrens.get(0) instanceof AbstractCastComponent)
			return false;
		return (children instanceof AbstractEffectComponent);
	}

	public void clearChildrens() {
		this.childrens.clear();
	}

	public boolean addChildren(AbstractSpellComponent newEffect) {
		if (canAddChildren(newEffect)) {
			childrens.add(newEffect);
			return true;
		}
		System.out.println("Can't add children");
		return false;
	}
}
