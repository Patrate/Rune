package fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.PotionEffectComponent;
import net.minecraft.potion.Effects;

public class MoveSpeedEffectComponent extends PotionEffectComponent {

	public MoveSpeedEffectComponent(AbstractSpellComponent parent) {
		super(parent, Effects.MOVEMENT_SPEED);
	}


}
