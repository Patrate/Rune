package fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class ModEffectInstance extends EffectInstance {
	private boolean isDuration;
	private int localTick;
	private int localMaxTick;
	private boolean cont;

	public ModEffectInstance(Effect effect, int duration, int amplifier, boolean ambiant, boolean visible,
			boolean showIcon, boolean isDuration) {
		super(effect, (isDuration)?duration:20, amplifier, ambiant, visible, showIcon);
		this.isDuration = isDuration;
		this.localTick = duration;
		this.localMaxTick = duration * 2;
		this.cont = true;
	}

	public boolean tick(LivingEntity entity, Runnable runnable) {
		if (isDuration)
			return super.tick(entity, runnable);

		if (getEffect().isDurationEffectTick(localTick, this.getAmplifier()))
			this.applyEffect(entity);

		localTickDown();

		return cont;
	}

	private void localTickDown() {
		--localTick;
		if (localTick <= 0)
			localTick += localMaxTick;
	}

	public void stop() {
		cont = false;
	}
}
