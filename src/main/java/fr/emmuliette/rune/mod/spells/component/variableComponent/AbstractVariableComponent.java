package fr.emmuliette.rune.mod.spells.component.variableComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractVariableComponent extends AbstractSpellComponent {
	private String name;
//	private AbstractSpellComponent who;
	
	public AbstractVariableComponent(PropertyFactory propFact, AbstractSpellComponent parent) {
		super(propFact, parent);
		// TODO Auto-generated constructor stub
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos target, SpellContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addNextPart(AbstractSpellComponent other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
	}

}
