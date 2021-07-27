package fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.PotionEffectComponent;
import net.minecraft.potion.Effects;

public class InvisibilityEffectComponent extends PotionEffectComponent {

	public InvisibilityEffectComponent(AbstractSpellComponent parent) {
		super(parent, Effects.INVISIBILITY);
	}


}
