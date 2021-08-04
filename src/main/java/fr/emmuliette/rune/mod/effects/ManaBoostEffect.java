package fr.emmuliette.rune.mod.effects;

import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.util.NonNullConsumer;

public class ManaBoostEffect extends Effect {
	public ManaBoostEffect(EffectType type, int color) {
		super(type, color);
	}

	public void removeAttributeModifiers(LivingEntity entity, AttributeModifierManager attribManager, int p_111187_3_) {
		super.removeAttributeModifiers(entity, attribManager, p_111187_3_);
		entity.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(new NonNullConsumer<ICaster>() {
			@Override
			public void accept(ICaster cap) {
				if (cap.getMana() > cap.getMaxMana()) {
					cap.setMana(cap.getMaxMana());
				}
			}
		});
	}
}
