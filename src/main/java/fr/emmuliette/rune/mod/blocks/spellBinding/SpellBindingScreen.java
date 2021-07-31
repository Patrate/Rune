package fr.emmuliette.rune.mod.blocks.spellBinding;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SpellBindingScreen extends ContainerScreen<SpellBindingContainer> implements IRecipeShownListener {
	protected static final ResourceLocation SPELLBINDER_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/spellbinder.png");
	/*private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation(
			"textures/gui/recipe_button.png");*/
	private final RecipeBookGui recipeBookComponent = new RecipeBookGui();
	private boolean widthTooNarrow;

	public SpellBindingScreen(SpellBindingContainer container, PlayerInventory playerInventory,
			ITextComponent textComp) {
		super(container, playerInventory, textComp);
	}

	protected void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
		this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
		this.children.add(this.recipeBookComponent);
		this.setInitialFocus(this.recipeBookComponent);
		/*this.addButton(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION,
				(p_214076_1_) -> {
					this.recipeBookComponent.initVisuals(this.widthTooNarrow);
					this.recipeBookComponent.toggleVisibility();
					this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width,
							this.imageWidth);
					((ImageButton) p_214076_1_).setPosition(this.leftPos + 5, this.height / 2 - 49);
				}));
		this.titleLabelX = 29;*/
	}

	public void tick() {
		super.tick();
		this.recipeBookComponent.tick();
	}

	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(mStack);
		if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
			this.renderBg(mStack, p_230430_4_, p_230430_2_, p_230430_3_);
			this.recipeBookComponent.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		} else {
			this.recipeBookComponent.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
			super.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
			this.recipeBookComponent.renderGhostRecipe(mStack, this.leftPos, this.topPos, true, p_230430_4_);
		}

		this.renderTooltip(mStack, p_230430_2_, p_230430_3_);
		this.recipeBookComponent.renderTooltip(mStack, this.leftPos, this.topPos, p_230430_2_, p_230430_3_);
	}

	@SuppressWarnings("deprecation")
	protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(SPELLBINDER_LOCATION);
		int i = this.leftPos;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}

	protected boolean isHovering(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_,
			double p_195359_7_) {
		return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible())
				&& super.isHovering(p_195359_1_, p_195359_2_, p_195359_3_, p_195359_4_, p_195359_5_, p_195359_7_);
	}

	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		if (this.recipeBookComponent.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
			this.setFocused(this.recipeBookComponent);
			return true;
		} else {
			return this.widthTooNarrow && this.recipeBookComponent.isVisible() ? true
					: super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
		}
	}

	protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_,
			int p_195361_7_) {
		boolean flag = p_195361_1_ < (double) p_195361_5_ || p_195361_3_ < (double) p_195361_6_
				|| p_195361_1_ >= (double) (p_195361_5_ + this.imageWidth)
				|| p_195361_3_ >= (double) (p_195361_6_ + this.imageHeight);
		return this.recipeBookComponent.hasClickedOutside(p_195361_1_, p_195361_3_, this.leftPos, this.topPos,
				this.imageWidth, this.imageHeight, p_195361_7_) && flag;
	}

	protected void slotClicked(Slot p_184098_1_, int p_184098_2_, int p_184098_3_, ClickType p_184098_4_) {
		super.slotClicked(p_184098_1_, p_184098_2_, p_184098_3_, p_184098_4_);
		this.recipeBookComponent.slotClicked(p_184098_1_);
	}

	public void recipesUpdated() {
		this.recipeBookComponent.recipesUpdated();
	}

	public void removed() {
		this.recipeBookComponent.removed();
		super.removed();
	}

	public RecipeBookGui getRecipeBookComponent() {
		return this.recipeBookComponent;
	}
}