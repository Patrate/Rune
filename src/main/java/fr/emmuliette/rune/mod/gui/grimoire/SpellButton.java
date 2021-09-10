package fr.emmuliette.rune.mod.gui.grimoire;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

public class SpellButton extends ImageButton {
	private static final ResourceLocation SPELL_BUTTON_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/spell_buttons.png");
	private final SpellWidget parent;

	public SpellButton(SpellWidget parent, GrimoireScreen screen, Grimoire grimoire, int x, int y, int imgX, int imgY, Button.IPressable onclick) {
		super(x, y, 15, 15, imgX, imgY, 16, SPELL_BUTTON_LOCATION, onclick);
		this.parent = parent;
		this.visible = true;
	}

	protected SpellWidget getParent() {
		return parent;
	}
}
