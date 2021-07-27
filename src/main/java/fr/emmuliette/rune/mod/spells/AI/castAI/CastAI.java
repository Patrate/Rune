package fr.emmuliette.rune.mod.spells.AI.castAI;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.AI.AbstractAI;
import fr.emmuliette.rune.mod.spells.entities.MagicEntity;

public abstract class CastAI extends AbstractAI {
	private int cast_count = 0;
	public static Supplier<CastAI> DEFAULT = () -> new CastAI(){
		private int tickCount = 10;
		@Override
		public boolean canCast(SpellContext context, MagicEntity entity) {
			return (getCastCount() == 0 && context.getTarget() != null);
		}
		@Override
		public boolean isAlive(SpellContext context, MagicEntity entity) {
			return entity.tickCount <= tickCount || getCastCount() > 0;
		}
	};
	public abstract boolean canCast(SpellContext context, MagicEntity entity);
	public abstract boolean isAlive(SpellContext context, MagicEntity entity);
	
	public final void cast() {
		cast_count++;
	}
	public final int getCastCount() {
		return cast_count;
	}
}
