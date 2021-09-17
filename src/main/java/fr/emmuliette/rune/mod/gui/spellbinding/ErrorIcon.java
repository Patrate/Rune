package fr.emmuliette.rune.mod.gui.spellbinding;

import java.util.Iterator;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ErrorIcon extends ImageButton {
	public static enum SpellError {
		MISSING_SPELL_NAME("gui.error.spellname"), NOT_A_RUNE("gui.error.notarune"),
		MISSING_PAPER("gui.error.missingpaper"), TOO_MUCH_PAPER("gui.error.toomuchpaper"), GENERIC("gui.error.generic"),
		EXCEPTION("gui.error.exception"), CANT_SOCKET("gui.error.cantsocket"), FIRST_RUNE("gui.error.firstrune"),
		INVALID("gui.error.invalid"), CANT_SUCCEED("gui.error.cantsucceed"), MISSING_CAST("gui.error.missingcast"),
		MISSING_EFFECT("gui.error.missingeffect"),;
		// ""

		private String message;

		private SpellError(String message) {
			this.message = message;
		}

		public TextComponent getTextComponent() {
			return new TranslationTextComponent(message);
		}
	}

	private static final ResourceLocation ERROR_BUTTON_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/spell_buttons.png");
	private SpellBindingScreen screen;

	public ErrorIcon(SpellBindingScreen screen, int x, int y) {
		super(x, y, 15, 15, 32, 0, 16, ERROR_BUTTON_LOCATION, (button) -> {
		});
		this.screen = screen;
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (screen.getMenu().errorMessage == null) {
			return;
		}
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	@SuppressWarnings("resource")
	@Override
	public void renderToolTip(MatrixStack mStack, int p_230443_2_, int p_230443_3_) {
		if (screen.getMenu().errorMessage != null) {
			TextComponent message = new StringTextComponent("");
			Iterator<SpellError> it = screen.getMenu().errorMessage.iterator();
			while (it.hasNext()) {
				SpellError e = it.next();
				message.append(e.getTextComponent());
				if (it.hasNext())
					message.append("\n");
			}
			screen.renderTooltip(mStack,
					screen.getMinecraft().font.split(message, Math.max(screen.width / 2 - 43, 170)), p_230443_2_,
					p_230443_3_);
		}
	}

}