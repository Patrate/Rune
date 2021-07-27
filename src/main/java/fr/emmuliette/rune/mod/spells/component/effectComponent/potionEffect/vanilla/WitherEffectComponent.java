package fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.PotionEffectComponent;
import net.minecraft.potion.Effects;

public class WitherEffectComponent extends PotionEffectComponent {

	public WitherEffectComponent(AbstractSpellComponent parent) {
		super(parent, Effects.WITHER);
	}


}
