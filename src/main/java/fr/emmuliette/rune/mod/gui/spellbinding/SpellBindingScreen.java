package fr.emmuliette.rune.mod.gui.spellbinding;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.ComponentGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class SpellBindingScreen extends ContainerScreen<SpellBindingContainer> implements IContainerListener {
	public static final ResourceLocation SPELLBINDER_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/spellbinder.png");

	private static final ITextComponent SPELL_NAME_HINT = (new TranslationTextComponent(
			"gui.spellbindingscreen.spell_name")).withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.GRAY);
	/*
	 * private static final ResourceLocation RECIPE_BUTTON_LOCATION = new
	 * ResourceLocation( "textures/gui/recipe_button.png");
	 */
	private final ComponentGui componentGui = new ComponentGui();
	private boolean widthTooNarrow;
	private TextFieldWidget spellNameBox;
	private int textBoxX = 118, textBoxY = 7;

	public SpellBindingScreen(SpellBindingContainer container, PlayerInventory playerInventory,
			ITextComponent textComp) {
		super(container, playerInventory, textComp);
	}

	protected void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		this.componentGui.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
		this.leftPos = this.componentGui.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
		this.children.add(this.componentGui);
		this.menu.addSlotListener(this);
		this.setInitialFocus(this.componentGui);
		String s = this.spellNameBox != null ? this.spellNameBox.getValue() : "";
		this.spellNameBox = new TextFieldWidget(this.minecraft.font, this.leftPos + textBoxX, this.topPos + textBoxY,
				50, 14, new StringTextComponent(""));
		this.spellNameBox.setMaxLength(50);
		this.spellNameBox.setBordered(false);
		this.spellNameBox.setVisible(true);
		this.spellNameBox.setTextColor(16777215);
		this.spellNameBox.setValue(s);
		initComponentGui();
		/*
		 * this.addButton(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20,
		 * 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (p_214076_1_) -> {
		 * this.recipeBookComponent.initVisuals(this.widthTooNarrow);
		 * this.recipeBookComponent.toggleVisibility(); this.leftPos =
		 * this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow,
		 * this.width, this.imageWidth); ((ImageButton)
		 * p_214076_1_).setPosition(this.leftPos + 5, this.height / 2 - 49); }));
		 * this.titleLabelX = 29;
		 */
	}

	private void initComponentGui() {
		this.componentGui.initVisuals(this.widthTooNarrow);
		this.componentGui.setVisible();
		this.leftPos = this.componentGui.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
	}

	public void tick() {
		super.tick();
		this.componentGui.tick();
		this.spellNameBox.tick();
	}

	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(mStack);
		if (this.componentGui.isVisible() && this.widthTooNarrow) {
			this.renderBg(mStack, p_230430_4_, p_230430_2_, p_230430_3_);
			this.componentGui.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		} else {
			this.componentGui.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
			super.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
			// this.componentGui.renderGhostRecipe(mStack, this.leftPos, this.topPos, true,
			// p_230430_4_);
			if (!this.spellNameBox.isFocused() && this.spellNameBox.getValue().isEmpty()) {
				drawString(mStack, this.minecraft.font, SPELL_NAME_HINT, this.leftPos + textBoxX,
						this.topPos + textBoxY, -1);
			} else {
				this.spellNameBox.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
			}
		}

		this.renderTooltip(mStack, p_230430_2_, p_230430_3_);
		this.componentGui.renderTooltip(mStack, this.leftPos, this.topPos, p_230430_2_, p_230430_3_);
	}

	@SuppressWarnings("deprecation")
	protected void renderBg(MatrixStack mStack, float useless3, int useless2, int useless) {
		mStack.pushPose();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(SPELLBINDER_LOCATION);
		int i = this.leftPos;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(mStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		mStack.popPose();
		mStack.pushPose();
		for (Slot slot : this.menu.slots) {
			this.renderCase(mStack, i + slot.x, j + slot.y, slot == this.componentGui.getCurrentSelectedSlot());
		}
		mStack.popPose();
	}

	private void renderCase(MatrixStack mStack, int x, int y, boolean isSelected) {
		if (isSelected)
			this.blit(mStack, x - 1, y - 1, 18, this.imageHeight, 18, 18);
		else
			this.blit(mStack, x - 1, y - 1, 0, this.imageHeight, 18, 18);
	}

	@Override
	protected boolean isHovering(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_,
			double p_195359_7_) {
		return (!this.widthTooNarrow || !this.componentGui.isVisible())
				&& super.isHovering(p_195359_1_, p_195359_2_, p_195359_3_, p_195359_4_, p_195359_5_, p_195359_7_);
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (this.componentGui.mouseClicked(x, y, mouseButton)) {
			this.setFocused(this.componentGui);
			return true;
		} else if (this.spellNameBox.mouseClicked(x, y, mouseButton)) {
			this.setFocused(this.spellNameBox);
			return true;
		} else {
			return this.widthTooNarrow && this.componentGui.isVisible() ? true : super.mouseClicked(x, y, mouseButton);
		}
	}

	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		if (this.spellNameBox.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
			this.updateSpellName();
			return true;
		} else if (this.spellNameBox.isFocused() && this.spellNameBox.isVisible() && p_231046_1_ != 256) {
			this.updateSpellName();
			return true;
		} else if (this.minecraft.options.keyChat.matches(p_231046_1_, p_231046_2_) && !this.spellNameBox.isFocused()) {
			this.spellNameBox.setFocus(true);
			this.updateSpellName();
			return true;
		} else {
			return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
		}
	}

	@Override
	public boolean charTyped(char p_231042_1_, int p_231042_2_) {
		if (this.spellNameBox.charTyped(p_231042_1_, p_231042_2_)) {
			this.updateSpellName();
			return true;
		} else {
			return super.charTyped(p_231042_1_, p_231042_2_);
		}
	}

	private void updateSpellName() {
		this.menu.setSpellName(this.spellNameBox.getValue());
	}

	@Override
	protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_,
			int p_195361_7_) {
		boolean flag = p_195361_1_ < (double) p_195361_5_ || p_195361_3_ < (double) p_195361_6_
				|| p_195361_1_ >= (double) (p_195361_5_ + this.imageWidth)
				|| p_195361_3_ >= (double) (p_195361_6_ + this.imageHeight);
		return this.componentGui.hasClickedOutside(p_195361_1_, p_195361_3_, this.leftPos, this.topPos, this.imageWidth,
				this.imageHeight, p_195361_7_) && flag;
	}

	private Slot selectedSlot = null;

	@Override
	protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType clickType) {
//		this.minecraft.player.item
		if (slot != null && slot instanceof SpellBindingRuneSlot && mouseButton == 1 && this.minecraft.player.inventory.getCarried().isEmpty()) {
			if (!slot.equals(selectedSlot)) {
				selectSlot(slot);
			}
			this.componentGui.slotClicked(slot);
			return;
		}
		super.slotClicked(slot, slotId, mouseButton, clickType);
		if(selectedSlot != null && !selectedSlot.hasItem()) {
			selectedSlot = null;
		}
	}

	public void selectSlot(Slot sSlot) {
		selectedSlot = sSlot;
		this.componentGui.updateSlotComponent(sSlot);
	}

	public void removed() {
		this.componentGui.removed();
		super.removed();
	}

	public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {
		System.out.println("Refreshing");
	}

	public void slotChanged(Container container, int slot, ItemStack item) {
		if(selectedSlot != null && !selectedSlot.hasItem()) {
			selectedSlot = null;
		}
	}

	public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
		System.out.println("settingContainerData");
	}
}