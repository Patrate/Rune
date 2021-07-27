package fr.emmuliette.rune.mod.spells.AI.moveAI;

import com.google.common.base.Supplier;

import fr.emmuliette.rune.mod.spells.AI.AbstractAI;
import net.minecraft.entity.Entity;

public abstract class MoveAI extends AbstractAI {
	public static Supplier<MoveAI> DEFAULT = () -> new MoveAI(){
		@Override
		public void move(Entity e) {
			return;
		}
	};
	public abstract void move(Entity e);
}
