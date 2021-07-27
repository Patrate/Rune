package fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.PotionEffectComponent;
import net.minecraft.potion.Effects;

public class DamageResistanceEffectComponent extends PotionEffectComponent {

	public DamageResistanceEffectComponent(AbstractSpellComponent parent) {
		super(parent, Effects.DAMAGE_RESISTANCE);
	}


}
