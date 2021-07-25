package fr.emmuliette.rune.mod.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class ModInstantEffect extends Effect {

	protected ModInstantEffect(EffectType type, int color) {
		super(type, color);
	}

	@Override
	public boolean isInstantenous() {
		return true;
	}

	@Override
	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		return p_76397_1_ >= 1;
	}

}
