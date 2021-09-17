package fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.PotionEffectComponent;
import net.minecraft.potion.Effects;

public class NauseaEffectComponent extends PotionEffectComponent {

	public NauseaEffectComponent(AbstractSpellComponent parent) {
		super(parent, Effects.CONFUSION);
	}


}
