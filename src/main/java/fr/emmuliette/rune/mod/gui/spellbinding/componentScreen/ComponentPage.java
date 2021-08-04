package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.recipebook.RecipeOverlayGui;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.item.crafting.RecipeBook;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ComponentPage {
	private final List<PropertyWidget> buttons = Lists.newArrayListWithCapacity(20);
	private PropertyWidget hoveredButton;
	private final RecipeOverlayGui overlay = new RecipeOverlayGui();
	private Minecraft minecraft;
	private List<RecipeList> recipeCollections;
	private ToggleWidget forwardButton;
	private ToggleWidget backButton;
	private int totalPages;
	private int currentPage;
	private RecipeBook recipeBook;
//	private IRecipe<?> lastClickedRecipe;
//	private RecipeList lastClickedRecipeCollection;
	private AbstractSpellComponent currentComponent;

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
		this.forwardButton.initTextureValues(1, 208, 13, 18, ComponentGui.RECIPE_BOOK_LOCATION);
		this.backButton = new ToggleWidget(x + 38, y + 137, 12, 17, true);
		this.backButton.initTextureValues(1, 208, 13, 18, ComponentGui.RECIPE_BOOK_LOCATION);
		this.propertyX = x;
		this.propertyY = y;
	}

	public void setComponent(AbstractSpellComponent comp) {
		this.buttons.clear();
		currentComponent = comp;
		if (comp == null)
			return;
		// TODO Grade modulable !!!
		// TODO move it dans le component somehow ?
		int i = this.propertyX + 10;
		int j = this.propertyY + 34;
		int height = 27;
		int k = 0;
		for (Property<?> property : comp.getProperties().getProperties(Grade.WOOD)) {
			this.buttons.add(new PropertyWidget(property, currentComponent, i, j + k * height, 88, height));
			++k;
		}

		/*
		 * for (int i = 0; i < 20; ++i) { this.buttons.add(new PropertyWidget(comp)); }
		 */
	}

	public void updateCollections(List<RecipeList> recipeList, boolean p_194192_2_) {
		this.recipeCollections = recipeList;
		this.totalPages = (int) Math.ceil((double) recipeList.size() / 20.0D);
		if (this.totalPages <= this.currentPage || p_194192_2_) {
			this.currentPage = 0;
		}

		this.updateButtonsForPage();
	}

	private void updateButtonsForPage() {
		int i = 20 * this.currentPage;

		for (int j = 0; j < this.buttons.size(); ++j) {
			PropertyWidget propertyWidget = this.buttons.get(j);
			if (i + j < this.recipeCollections.size()) {
				RecipeList recipelist = this.recipeCollections.get(i + j);
				propertyWidget.init(recipelist, this);
				propertyWidget.visible = true;
			} else {
				propertyWidget.visible = false;
			}
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

		for (PropertyWidget propertyWidget : this.buttons) {
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

//	@Nullable
//	public IRecipe<?> getLastClickedRecipe() {
//		return this.lastClickedRecipe;
//	}

//	@Nullable
//	public RecipeList getLastClickedRecipeCollection() {
//		return this.lastClickedRecipeCollection;
//	}

	public void setInvisible() {
		this.overlay.setVisible(false);
	}

	public boolean mouseClicked(double p_198955_1_, double p_198955_3_, int p_198955_5_, int p_198955_6_,
			int p_198955_7_, int p_198955_8_, int p_198955_9_) {
//		this.lastClickedRecipe = null;
//		this.lastClickedRecipeCollection = null;
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
			for (PropertyWidget recipewidget : this.buttons) {
				if (recipewidget.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
//					if (p_198955_5_ == 0) {
//						this.lastClickedRecipe = recipewidget.getRecipe();
//						this.lastClickedRecipeCollection = recipewidget.getCollection();
//					} /*
//						 * else if (p_198955_5_ == 1 && !this.overlay.isVisible() &&
//						 * !recipewidget.isOnlyOption()) { this.overlay.init(this.minecraft,
//						 * recipewidget.getCollection(), recipewidget.x, recipewidget.y, p_198955_6_ +
//						 * p_198955_8_ / 2, p_198955_7_ + 13 + p_198955_9_ / 2,
//						 * (float)recipewidget.getWidth()); }
//						 */

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
}