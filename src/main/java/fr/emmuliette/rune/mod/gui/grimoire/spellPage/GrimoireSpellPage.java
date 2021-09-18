package fr.emmuliette.rune.mod.gui.grimoire.spellPage;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.gui.grimoire.CGrimoireSpellPacket;
import fr.emmuliette.rune.mod.gui.grimoire.CGrimoireSpellPacket.Action;
import fr.emmuliette.rune.mod.gui.grimoire.GrimoireScreen;
import fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.ComponentGui;
import fr.emmuliette.rune.mod.sync.SyncHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireSpellPage extends Widget {
	public static final ResourceLocation SPELL_PAGE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/spell_page.png");
	private static final ResourceLocation SPELL_BUTTON_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/spell_buttons.png");

	private int xOffset;
	private int width;
	private int height;

	private Minecraft minecraft;
	private ToggleWidget forwardButton;
	private ToggleWidget backButton;
	private ImageButton deleteButton;
	private ImageButton renameButton;

	private int totalPages;
	private int currentPage;
	protected GrimoireScreen menu;
	private TextFieldWidget spellNameBox;

	public FontRenderer getFont() {
		return menu.getFont();
	}

	public GrimoireSpellPage() {// int x, int y) {
		super(0, 0, 135, 17, StringTextComponent.EMPTY);
	}

	void removeSpellServer() {
		SyncHandler.sendToServer(new CGrimoireSpellPacket(menu.getSelectedSpellId(), Action.REMOVE));
	}

	private void updateArrowButtons() {
		this.forwardButton.visible = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
		this.backButton.visible = this.totalPages > 1 && this.currentPage > 0;
	}

	public boolean mouseClicked(double p_198955_1_, double p_198955_3_, int p_198955_5_, int p_198955_6_,
			int p_198955_7_, int p_198955_8_, int p_198955_9_) {
		if (this.forwardButton.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
			++this.currentPage;
			return true;
		} else if (this.backButton.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
			--this.currentPage;
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
		this.minecraft = minecraft;
		this.menu = menu;
		this.width = width;
		this.height = height;

		int i = (this.width - 147) / 2 - this.xOffset;
		int j = (this.height - 166) / 2;
		this.x = i;
		this.y = j;

		this.forwardButton = new ToggleWidget(93, 137, 12, 17, false);
		this.forwardButton.initTextureValues(153, 29, 13, 18, ComponentGui.COMPONENT_PAGE_LOCATION);
		this.backButton = new ToggleWidget(38, 137, 12, 17, true);
		this.backButton.initTextureValues(153, 29, 13, 18, ComponentGui.COMPONENT_PAGE_LOCATION);

		String s = this.spellNameBox != null ? this.spellNameBox.getValue() : "";
		this.spellNameBox = new TextFieldWidget(this.minecraft.font, (this.width - 200) / 2 - this.xOffset,
				(this.height - 152) / 2, 50, 14, new StringTextComponent(""));
		this.spellNameBox.setMaxLength(50);
		this.spellNameBox.setBordered(false);
		this.spellNameBox.setVisible(true);
		this.spellNameBox.setTextColor(16777215);
		this.spellNameBox.setValue(s);

		this.renameButton = new ImageButton(i + 32, j + 12, 15, 15, 48, 0, 16, SPELL_BUTTON_LOCATION, (button) -> {
			updateSpellName();
		});

		// Remove spell
		this.deleteButton = new ImageButton(i + 32, j + 32, 15, 15, 16, 0, 16, SPELL_BUTTON_LOCATION, (button) -> {
			removeSpellServer();
		});

		updateArrowButtons();
		minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	private void updateSpellName() {
		ISpell spell = menu.getSelectedSpell();
		if (spell != null && spell.getSpell() != null) {
			String newName = this.spellNameBox.getValue();
			if (newName.equals(spell.getSpell().getName()))
				return;
			spell.getSpell().setName(newName);
			spell.sync();
		}
	}

	public void selectSpell() {
		ISpell spell = menu.getSelectedSpell();
		if (spell != null && spell.getSpell() != null) {
			this.spellNameBox.setValue(spell.getSpell().getName());
		}
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
	public void render(MatrixStack mStack, int mouseX, int mouseY, float z) {
		if (this.isVisible()) {
			mStack.pushPose();
			this.minecraft.getTextureManager().bind(SPELL_PAGE_LOCATION);
			int i = (this.width - 147) / 2 - this.xOffset;
			int j = (this.height - 166) / 2;
			this.blit(mStack, i, j, 1, 1, 147, 181);
			mStack.popPose();

			ISpell spell = menu.getSelectedSpell();
			if (spell != null && spell.getSpell() != null) {
//				if (!this.spellNameBox.isFocused() && this.spellNameBox.getValue().isEmpty()) {
//					String s = this.spellNameBox.getValue();
//					int fontWidth = this.minecraft.font.width(s);
//					this.minecraft.font.draw(mStack, s, (float) (i - fontWidth / 2 + 73), (float) (j + 13), 0);
//				} else {
					this.spellNameBox.render(mStack, mouseX, mouseY, z);
//				}
				String s = spell.getSpell().getName();
				int fontWidth = this.minecraft.font.width(s);
				this.minecraft.font.draw(mStack, s, (float) (i - fontWidth / 2 + 73), (float) (j + 13), 0);
				int k = 34;
				for (ITextComponent txt : spell.getSpell().getTooltips()) {
					this.minecraft.font.draw(mStack, txt, (float) i + 16, (float) (j + k), 0);
					k += 12;
				}
			}

			// Pages
			if (this.totalPages > 1) {
				String s = this.currentPage + 1 + "/" + this.totalPages;
				int fontWidth = this.minecraft.font.width(s);
				this.minecraft.font.draw(mStack, s, (float) (i - fontWidth / 2 + 73), (float) (j + 141), -1);
			}

			this.backButton.render(mStack, i, j, z);
			this.forwardButton.render(mStack, i, j, z);
			this.deleteButton.render(mStack, i, j, z);
			this.renameButton.render(mStack, i, j, z);
		}
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (this.spellNameBox.mouseClicked(x, y, mouseButton)) {
			this.menu.setFocused(this.spellNameBox);
			return true;
		}
		if (backButton.mouseClicked(x, y, mouseButton))
			return true;
		if (forwardButton.mouseClicked(x, y, mouseButton))
			return true;
		if (deleteButton.mouseClicked(x, y, mouseButton))
			return true;
		if (renameButton.mouseClicked(x, y, mouseButton))
			return true;
		return false;
	}

	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		if (this.spellNameBox.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
//			this.updateSpellName();
			return true;
		} else if (this.spellNameBox.isFocused() && this.spellNameBox.isVisible() && p_231046_1_ != 256) {
//			this.updateSpellName();
			return true;
		} else if (this.minecraft.options.keyChat.matches(p_231046_1_, p_231046_2_) && !this.spellNameBox.isFocused()) {
			this.spellNameBox.setFocus(true);
//			this.updateSpellName();
			return true;
		} else {
			return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
		}
	}

	@Override
	public boolean charTyped(char p_231042_1_, int p_231042_2_) {
		if (this.spellNameBox.charTyped(p_231042_1_, p_231042_2_)) {
//			this.updateSpellName();
			return true;
		} else {
			return super.charTyped(p_231042_1_, p_231042_2_);
		}
	}

}