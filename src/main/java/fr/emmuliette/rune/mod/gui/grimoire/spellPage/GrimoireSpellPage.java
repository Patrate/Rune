package fr.emmuliette.rune.mod.gui.grimoire.spellPage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.gui.grimoire.GrimoireScreen;
import fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.ComponentGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireSpellPage extends AbstractGui implements IRenderable, IGuiEventListener {
	public static final ResourceLocation SPELL_PAGE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/component_page.png");
	private static final ResourceLocation SPELL_BUTTON_LOCATION = new ResourceLocation(
			"textures/gui/recipe_button.png");

	private int xOffset;
	private int width;
	private int height;

	private Minecraft minecraft;
	private ToggleWidget forwardButton;
	private ToggleWidget backButton;

	private ImageButton getSpellButton;
	private int totalPages;
	private int currentPage;
//	private int spellId;
	protected GrimoireScreen menu;

	public FontRenderer getFont() {
		return menu.getFont();
	}

	public GrimoireSpellPage() {
	}

//	public void getSpell() {
//		menu.getSpellServer(spellId);
//	}

//	public void setSpell(int spellId) {
//		this.spellId = spellId;
//	}

	private void updateArrowButtons() {
		this.forwardButton.visible = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
		this.backButton.visible = this.totalPages > 1 && this.currentPage > 0;
	}

	public boolean mouseClicked(double p_198955_1_, double p_198955_3_, int p_198955_5_, int p_198955_6_,
			int p_198955_7_, int p_198955_8_, int p_198955_9_) {
		if (this.forwardButton.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
			++this.currentPage;
			// this.updateButtonsForPage();
			return true;
		} else if (this.backButton.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
			--this.currentPage;
			// this.updateButtonsForPage();
			return true;
		}
		return false;
	}

	public Minecraft getMinecraft() {
		return this.minecraft;
	}

	public boolean isVisible() {
		return menu.getSelectedSpell() != null;
	}

	public void init(int width, int height, Minecraft minecraft, boolean initVisuals, GrimoireScreen menu) {
		System.out.println("INIT GRIMOIRE SPELL PAGE");
		this.minecraft = minecraft;
		this.menu = menu;
		this.width = width;
		this.height = height;

		this.forwardButton = new ToggleWidget(93, 137, 12, 17, false);
		this.forwardButton.initTextureValues(153, 29, 13, 18, ComponentGui.COMPONENT_PAGE_LOCATION);
		this.backButton = new ToggleWidget(38, 137, 12, 17, true);
		this.backButton.initTextureValues(153, 29, 13, 18, ComponentGui.COMPONENT_PAGE_LOCATION);
		this.getSpellButton = new ImageButton(32, 32, 20, 18, 0, 0, 19, SPELL_BUTTON_LOCATION, (button) -> {
		});
		updateArrowButtons();
		minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	public void initVisuals(boolean widthTooNarrow) {
		this.xOffset = widthTooNarrow ? 0 : 86;
	}

	public int updateScreenPosition(boolean widthTooNarrow, int x, int y) {
		int i;
		if (this.isVisible() && !widthTooNarrow) {
			i = 177 + (x - y - 200) / 2;
		} else {
			i = (x - y) / 2;
		}

		return i;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (this.isVisible()) {
			RenderSystem.pushMatrix();
			RenderSystem.translatef(0.0F, 0.0F, 100.0F);
			this.minecraft.getTextureManager().bind(SPELL_PAGE_LOCATION);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			int i = (this.width - 147) / 2 - this.xOffset;
			int j = (this.height - 166) / 2;
			this.blit(mStack, i, j, 1, 1, 147, 166);
			RenderSystem.popMatrix();

			// Pages
			if (this.totalPages > 1) {
				String s = this.currentPage + 1 + "/" + this.totalPages;
				int fontWidth = this.minecraft.font.width(s);
				this.minecraft.font.draw(mStack, s, (float) (p_230430_2_ - fontWidth / 2 + 73),
						(float) (p_230430_3_ + 141), -1);
			}

			this.backButton.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
			this.forwardButton.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
			this.getSpellButton.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);

		}
	}
}