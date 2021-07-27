package fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.PotionEffectComponent;
import net.minecraft.potion.Effects;

public class WeaknessEffectComponent extends PotionEffectComponent {

	public WeaknessEffectComponent(AbstractSpellComponent parent) {
		super(parent, Effects.WEAKNESS);
	}


}
