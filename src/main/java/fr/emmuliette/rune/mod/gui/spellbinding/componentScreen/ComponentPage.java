package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingContainer;
import fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets.PropertyWidget;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeOverlayGui;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.item.crafting.RecipeBook;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ComponentPage {
	private final static int COMP_PER_PAGE = 4;
	private final List<PropertyWidget<?>> buttons = Lists.newArrayListWithCapacity(20);
	private PropertyWidget<?> hoveredButton;
	private final RecipeOverlayGui overlay = new RecipeOverlayGui();
	private Minecraft minecraft;
	private ToggleWidget forwardButton;
	private ToggleWidget backButton;
	private int totalPages;
	private int currentPage;
	private RecipeBook recipeBook;
	private AbstractSpellComponent currentComponent;
	protected SpellBindingContainer menu;

	public ComponentPage() {
		currentComponent = null;
	}

	private int propertyX = 0;
	private int propertyY = 0;

	public void init(Minecraft minecraft, int x, int y) {
		this.minecraft = minecraft;
		this.recipeBook = minecraft.player.getRecipeBook();

		for (int i = 0; i < this.buttons.size(); ++i) {
			this.buttons.get(i).setPosition(x + 11 + 25 * (i % 5), y + 31 + 25 * (i / 5));
		}

		this.forwardButton = new ToggleWidget(x + 93, y + 137, 12, 17, false);
		this.forwardButton.initTextureValues(153, 29, 13, 18, ComponentGui.COMPONENT_PAGE_LOCATION);
		this.backButton = new ToggleWidget(x + 38, y + 137, 12, 17, true);
		this.backButton.initTextureValues(153, 29, 13, 18, ComponentGui.COMPONENT_PAGE_LOCATION);
		this.propertyX = x;
		this.propertyY = y;
	}

	public void setComponent(Grade grade, AbstractSpellComponent comp) {
		this.buttons.clear();
		currentComponent = comp;
		if (comp == null)
			return;
		int i = this.propertyX + 7;
		int j = this.propertyY + 7;
		int height = 32;
		int k = 0;
		this.totalPages = 1;
		for (Property<?> property : comp.getProperties().getProperties(grade)) {
			PropertyWidget<?> widget = PropertyWidget.buildWidget(this, grade, property, currentComponent, i,
					j + (k % COMP_PER_PAGE) * height);
			if (widget.getSize() + k > COMP_PER_PAGE) {
				widget.y = j;
				k = 0;
				this.totalPages += 1;
			}
			this.buttons.add(widget);
			k += widget.getSize();
		}
		this.currentPage = 0;

		updateButtonsForPage();
	}

	private void updateButtonsForPage() {
//		int i = COMP_PER_PAGE * this.currentPage;
		int k = 0;
		int currentPage = 0;
		for (int j = 0; j < this.buttons.size(); ++j) {
			PropertyWidget<?> propertyWidget = this.buttons.get(j);
			k += propertyWidget.getSize();
			if (k > COMP_PER_PAGE)
				currentPage++;
			if (currentPage == this.currentPage) {
				propertyWidget.visible = true;
			} else {
				propertyWidget.visible = false;
			}
//			if (k >= i && k < i + COMP_PER_PAGE) {
//				propertyWidget.visible = true;
//			} else {
//				propertyWidget.visible = false;
//			}
		}

		this.updateArrowButtons();
	}

	private void updateArrowButtons() {
		this.forwardButton.visible = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
		this.backButton.visible = this.totalPages > 1 && this.currentPage > 0;
	}

	public void render(MatrixStack mStack, int p_238927_2_, int p_238927_3_, int p_238927_4_, int p_238927_5_,
			float p_238927_6_) {
		// Pages
		if (this.totalPages > 1) {
			String s = this.currentPage + 1 + "/" + this.totalPages;
			int i = this.minecraft.font.width(s);
			this.minecraft.font.draw(mStack, s, (float) (p_238927_2_ - i / 2 + 73), (float) (p_238927_3_ + 141), -1);
		}

		this.hoveredButton = null;

		for (PropertyWidget<?> propertyWidget : this.buttons) {
			propertyWidget.render(mStack, p_238927_4_, p_238927_5_, p_238927_6_);
			if (propertyWidget.visible && propertyWidget.isHovered()) {
				this.hoveredButton = propertyWidget;
			}
		}

		this.backButton.render(mStack, p_238927_4_, p_238927_5_, p_238927_6_);
		this.forwardButton.render(mStack, p_238927_4_, p_238927_5_, p_238927_6_);
		this.overlay.render(mStack, p_238927_4_, p_238927_5_, p_238927_6_);
	}

	public void renderTooltip(MatrixStack p_238926_1_, int p_238926_2_, int p_238926_3_) {
		if (this.minecraft.screen != null && this.hoveredButton != null && !this.overlay.isVisible()) {
			this.minecraft.screen.renderComponentTooltip(p_238926_1_,
					this.hoveredButton.getTooltipText(this.minecraft.screen), p_238926_2_, p_238926_3_);
		}

	}

	public void setInvisible() {
		this.overlay.setVisible(false);
	}

	public boolean mouseClicked(double p_198955_1_, double p_198955_3_, int p_198955_5_, int p_198955_6_,
			int p_198955_7_, int p_198955_8_, int p_198955_9_) {
		if (this.overlay.isVisible()) {
			if (this.overlay.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
//				this.lastClickedRecipe = this.overlay.getLastRecipeClicked();
//				this.lastClickedRecipeCollection = this.overlay.getRecipeCollection();
			} else {
				this.overlay.setVisible(false);
			}

			return true;
		} else if (this.forwardButton.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
			++this.currentPage;
			this.updateButtonsForPage();
			return true;
		} else if (this.backButton.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
			--this.currentPage;
			this.updateButtonsForPage();
			return true;
		} else {
			for (PropertyWidget<?> propertyWidget : this.buttons) {
				if (propertyWidget.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
					return true;
				}
			}

			return false;
		}
	}

	public Minecraft getMinecraft() {
		return this.minecraft;
	}

	public RecipeBook getRecipeBook() {
		return this.recipeBook;
	}

	public SpellBindingContainer getMenu() {
		return menu;
	}
}