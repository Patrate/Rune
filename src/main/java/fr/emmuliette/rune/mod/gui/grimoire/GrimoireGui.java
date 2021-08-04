package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireGui extends AbstractGui implements IRenderable, IGuiEventListener {
	protected static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
	private int width;
	private int height;
	protected GrimoireContainer menu;
	protected Minecraft minecraft;
	private final List<GrimoireTabToggleWidget> tabButtons = Lists.newArrayList();
	private GrimoireTabToggleWidget selectedTab;

	public void init(int width, int height, Minecraft minecraft, GrimoireContainer menu) {
		this.minecraft = minecraft;
		this.width = width;
		this.height = height;
		this.menu = menu;
		minecraft.player.containerMenu = menu;
		if (this.isVisible()) {
			this.initVisuals();
		}

		minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	public void initVisuals() {
		this.tabButtons.clear();
		this.tabButtons.clear();

		for (GrimoireCategories grimoireCategories : this.menu.getGrimoireCategories()) {
			this.tabButtons.add(new GrimoireTabToggleWidget(grimoireCategories));
		}

		if (this.selectedTab != null) {
			this.selectedTab = this.tabButtons.stream().filter((p_209505_1_) -> {
				return p_209505_1_.getCategory().equals(this.selectedTab.getCategory());
			}).findFirst().orElse((GrimoireTabToggleWidget) null);
		}

		if (this.selectedTab == null) {
			this.selectedTab = this.tabButtons.get(0);
		}

		this.selectedTab.setStateTriggered(true);
		this.updateCollections(false);
		this.updateTabs();
	}

	public void removed() {
		this.selectedTab = null;
		this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}

	public int updateScreenPosition(int p_193011_2_, int p_193011_3_) {
		return (p_193011_2_ - p_193011_3_) / 2;
	}

	public boolean isVisible() {
		return true;
	}

	public void slotClicked(@Nullable Slot slot) {
	}

	private void updateCollections(boolean p_193003_1_) {
		/*
		 * List<RecipeList> list =
		 * this.book.getCollection(this.menu.getGrimoireCategories().get(0));
		 * list.forEach((p_193944_1_) -> { p_193944_1_.canCraft(this.stackedContents,
		 * this.menu.getGridWidth(), this.menu.getGridHeight(), this.book); });
		 * List<RecipeList> list1 = Lists.newArrayList(list);
		 * list1.removeIf((p_193952_0_) -> { return !p_193952_0_.hasKnownRecipes(); });
		 * list1.removeIf((p_193953_0_) -> { return !p_193953_0_.hasFitting(); });
		 */
	}

	void updateTabs() {
		int i = (this.width - 147) / 2 - 102;
		int j = (this.height - 166) / 2 + 3;
		int l = 0;

		for (GrimoireTabToggleWidget grimoireTabToggleWidget : this.tabButtons) {
			GrimoireCategories recipebookcategories = grimoireTabToggleWidget.getCategory();
			if (recipebookcategories != GrimoireCategories.CRAFTING_SEARCH
					&& recipebookcategories != GrimoireCategories.FURNACE_SEARCH) {
				grimoireTabToggleWidget.setPosition(i, j + 27 * l++);
				grimoireTabToggleWidget.startAnimation(this.minecraft);
			} else {
				grimoireTabToggleWidget.visible = true;
				grimoireTabToggleWidget.setPosition(i, j + 27 * l++);
			}
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float p_230430_4_) {
		if (this.isVisible()) {
//			RenderSystem.translatef(0.0F, 0.0F, 100.0F);
//			this.minecraft.getTextureManager().bind(GrimoireScreen.GRIMOIRE_LOCATION);
//			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//			int i = (this.width - 147) / 2 - this.xOffset;
//			int j = (this.height - 166) / 2;
//			this.blit(mStack, i, j, 1, 1, 147, 166);

			RenderSystem.pushMatrix();
			for (GrimoireTabToggleWidget grimoireTabToggleWidget : this.tabButtons) {
				grimoireTabToggleWidget.render(mStack, mouseX, mouseY, p_230430_4_);
			}
			RenderSystem.popMatrix();
		}
	}

	public void renderTooltip(MatrixStack mStack, int p_238924_2_, int p_238924_3_, int p_238924_4_, int p_238924_5_) {
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (this.isVisible() && !this.minecraft.player.isSpectator()) {
			for (GrimoireTabToggleWidget grimoireTabToggleWidget : this.tabButtons) {
				if (grimoireTabToggleWidget.mouseClicked(mouseX, mouseY, mouseButton)) {
					if (this.selectedTab != grimoireTabToggleWidget) {
						this.selectedTab.setStateTriggered(false);
						this.selectedTab = grimoireTabToggleWidget;
						this.selectedTab.setStateTriggered(true);
						this.updateCollections(true);
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasClickedOutside(double p_195604_1_, double p_195604_3_, int p_195604_5_, int p_195604_6_,
			int p_195604_7_, int p_195604_8_, int p_195604_9_) {
		if (!this.isVisible()) {
			return true;
		} else {
			boolean flag = p_195604_1_ < (double) p_195604_5_ || p_195604_3_ < (double) p_195604_6_
					|| p_195604_1_ >= (double) (p_195604_5_ + p_195604_7_)
					|| p_195604_3_ >= (double) (p_195604_6_ + p_195604_8_);
			boolean flag1 = (double) (p_195604_5_ - 147) < p_195604_1_ && p_195604_1_ < (double) p_195604_5_
					&& (double) p_195604_6_ < p_195604_3_ && p_195604_3_ < (double) (p_195604_6_ + p_195604_8_);
			return flag && !flag1;
		}
	}

	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		return IGuiEventListener.super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
	}

	@Override
	public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
		return IGuiEventListener.super.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_);
	}

	@Override
	public boolean charTyped(char p_231042_1_, int p_231042_2_) {
		return IGuiEventListener.super.charTyped(p_231042_1_, p_231042_2_);
	}

	@Override
	public boolean isMouseOver(double p_231047_1_, double p_231047_3_) {
		return IGuiEventListener.super.isMouseOver(p_231047_1_, p_231047_3_);
	}
}
