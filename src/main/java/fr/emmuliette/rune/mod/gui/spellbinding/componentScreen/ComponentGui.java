package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingContainer;
import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingRuneSlot;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ComponentGui extends AbstractGui implements IRenderable, IGuiEventListener {
	public static final ResourceLocation COMPONENT_PAGE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/component_page.png");
	private int xOffset;
	private int width;
	private int height;
	protected SpellBindingContainer menu;
	protected Minecraft minecraft;
	private final ComponentPage componentPage = new ComponentPage();
	private final RecipeItemHelper stackedContents = new RecipeItemHelper();
	private int timesInventoryChanged;

	public void init(int width, int height, Minecraft minecraft, boolean initVisuals, SpellBindingContainer menu) {
		this.minecraft = minecraft;
		this.width = width;
		this.height = height;
		this.menu = menu;
		minecraft.player.containerMenu = menu;
		this.componentPage.menu = menu;
		this.timesInventoryChanged = minecraft.player.inventory.getTimesChanged();
		if (this.isVisible()) {
			this.initVisuals(initVisuals);
		}

		minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	public void initVisuals(boolean p_201518_1_) {
		this.xOffset = p_201518_1_ ? 0 : 86;
		int i = (this.width - 147) / 2 - this.xOffset;
		int j = (this.height - 166) / 2;
		this.stackedContents.clear();
		this.minecraft.player.inventory.fillStackedContents(this.stackedContents);
		this.menu.fillCraftSlotsStackedContents(this.stackedContents);
		this.componentPage.init(this.minecraft, i, j);
//		this.updateCollections(false);
	}

	@Override
	public boolean changeFocus(boolean focus) {
		return false;
	}

	public void removed() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}

	public int updateScreenPosition(boolean p_193011_1_, int p_193011_2_, int p_193011_3_) {
		int i;
		if (this.isVisible() && !p_193011_1_) {
			i = 177 + (p_193011_2_ - p_193011_3_ - 200) / 2;
		} else {
			i = (p_193011_2_ - p_193011_3_) / 2;
		}

		return i;
	}

	public boolean isVisible() {
		return true;
	}

//	public void setVisible() {
//		boolean p_193006_1_ = true;
//		this.book.setOpen(this.menu.getRecipeBookType(), p_193006_1_);
//		if (!p_193006_1_) {
//			this.componentPage.setInvisible();
//		}
//
//		this.sendUpdateSettings();
//	}

	public void slotClicked(@Nullable Slot slot) {
		updateSlotComponent(slot);
	}

	private SpellBindingRuneSlot currentSelectedSlot = null;

	public void updateSlotComponent(Slot slot) {
		if (slot instanceof SpellBindingRuneSlot && slot != null && slot.index > 1 && slot.index < this.menu.getSize()
				&& this.isVisible() && slot.getItem().getItem() instanceof RuneItem) {
			currentSelectedSlot = (SpellBindingRuneSlot) slot;
		}
		updateComponent();
	}

	private void updateComponent() {
		AbstractSpellComponent component = null;
		Grade grade = Grade.UNKNOWN;
		if (getCurrentSelectedSlot() != null && getCurrentSelectedSlot().getItem().getItem() instanceof RuneItem) {
			component = getCurrentSelectedSlot().getComponent();
			grade = RuneItem.getGrade(getCurrentSelectedSlot().getItem());
			System.out.println("Grade is " + grade.name());
		}

		componentPage.setComponent(grade, component);
	}

//	private void updateCollections(boolean p_193003_1_) {
//		List<RecipeList> list = this.book.getCollection(this.menu.getRecipeBookCategories().get(0));
//		list.forEach((p_193944_1_) -> {
//			p_193944_1_.canCraft(this.stackedContents, this.menu.getGridWidth(), this.menu.getGridHeight(), this.book);
//		});
//		List<RecipeList> list1 = Lists.newArrayList(list);
//		list1.removeIf((p_193952_0_) -> {
//			return !p_193952_0_.hasKnownRecipes();
//		});
//		list1.removeIf((p_193953_0_) -> {
//			return !p_193953_0_.hasFitting();
//		});
//	}

	public void tick() {
		if (this.isVisible()) {
			if (this.timesInventoryChanged != this.minecraft.player.inventory.getTimesChanged()) {
				this.timesInventoryChanged = this.minecraft.player.inventory.getTimesChanged();
			}
			if (getCurrentSelectedSlot() != null && !getCurrentSelectedSlot().hasItem()) {
				updateComponent();
				currentSelectedSlot = null;
			}
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (this.isVisible()) {
			RenderSystem.pushMatrix();
			RenderSystem.translatef(0.0F, 0.0F, 100.0F);
			this.minecraft.getTextureManager().bind(COMPONENT_PAGE_LOCATION);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			int i = (this.width - 147) / 2 - this.xOffset;
			int j = (this.height - 166) / 2;
			this.blit(mStack, i, j, 1, 1, 147, 166);
			this.componentPage.render(mStack, i, j, p_230430_2_, p_230430_3_, p_230430_4_);
			RenderSystem.popMatrix();
		}
	}

	public void renderTooltip(MatrixStack mStack, int p_238924_2_, int p_238924_3_, int p_238924_4_, int p_238924_5_) {
		if (this.isVisible()) {
			this.componentPage.renderTooltip(mStack, p_238924_4_, p_238924_5_);
		}
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		if (this.isVisible() && !this.minecraft.player.isSpectator()) {
			if (this.componentPage.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_,
					(this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
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

//	protected void sendUpdateSettings() {
//		if (this.minecraft.getConnection() != null) {
//			RecipeBookCategory recipebookcategory = this.menu.getRecipeBookType();
//			boolean flag = this.book.getBookSettings().isOpen(recipebookcategory);
//			boolean flag1 = this.book.getBookSettings().isFiltering(recipebookcategory);
//			this.minecraft.getConnection().send(new CUpdateRecipeBookStatusPacket(recipebookcategory, flag, flag1));
//		}
//
//	}

	public SpellBindingRuneSlot getCurrentSelectedSlot() {
		return currentSelectedSlot;
	}
}
