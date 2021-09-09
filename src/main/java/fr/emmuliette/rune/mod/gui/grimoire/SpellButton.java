package fr.emmuliette.rune.mod.gui.grimoire;

import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

public class SpellButton extends ImageButton {
	private static final ResourceLocation SPELL_BUTTON_LOCATION = new ResourceLocation(
			"textures/gui/recipe_button.png");
	private final SpellWidget parent;

	public SpellButton(SpellWidget parent, GrimoireScreen screen, Grimoire grimoire, int x, int y) {
		super(x, y, 14, 14, 0, 0, 15, SPELL_BUTTON_LOCATION, (button) -> {
			SpellButton sButton = (SpellButton) button;
			sButton.parent.getSpellServer(sButton.parent.getSpellId());
			// TODO
			// parent.selectSpell(spellId);
		});
		this.parent = parent;
		this.visible = true;
	}
}
