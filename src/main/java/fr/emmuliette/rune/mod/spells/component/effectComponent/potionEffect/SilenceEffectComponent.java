package fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect;

import fr.emmuliette.rune.mod.effects.ModEffects;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.PotionEffectComponent;

public class SilenceEffectComponent extends PotionEffectComponent {

	public SilenceEffectComponent(AbstractSpellComponent parent) {
		super(parent, ModEffects.SILENCED.get());
	}


}
