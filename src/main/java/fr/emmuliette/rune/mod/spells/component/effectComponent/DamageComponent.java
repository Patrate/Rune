package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DamageComponent extends AbstractEffectComponent {
	public DamageComponent(AbstractSpellComponent parent) {
		super(PropertyFactory.EMPTY_FACTORY, parent);
	}
	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		// Damage = ceil(1.4 ^ power - 0.5)
		System.out.println("Damage component with " + context.getPower() + " power doing " + (float) Math.ceil(Math.pow(1.4f, context.getPower()) - 0.5) + " damages");
		target.hurt(DamageSource.GENERIC, (float) Math.ceil(Math.pow(1.4f, context.getPower()) - 0.5));
		return true;
	}
	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		return false;
	}
	
	@Override
	public Cost<?> getCost() {
		Cost<?> retour = new ManaCost(1);
		retour.add(super.getCost());
		return retour;
	}
	
	@Override
	public float getMaxPower() {
		return 10; // TODO mettre les apramètres à la place
	}
}
