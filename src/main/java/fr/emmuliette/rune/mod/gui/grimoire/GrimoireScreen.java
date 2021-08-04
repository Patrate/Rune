package fr.emmuliette.rune.mod.gui.grimoire;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GrimoireScreen extends ContainerScreen<GrimoireContainer> {// implements IRecipeShownListener {
	static final ResourceLocation GRIMOIRE_LOCATION = new ResourceLocation("textures/gui/book.png");

	private final GrimoireGui grimoireGui = new GrimoireGui();
	private GrimoireContainer container;
	private GrimoireListener listener;

	public GrimoireScreen(GrimoireContainer container, PlayerInventory playerInventory, ITextComponent textComp) {
		super(container, playerInventory, textComp);
		this.container = container;
	}

	protected void init() {
		super.init();
		this.imageWidth = 146 * 2;
		this.imageHeight = 192;
		this.grimoireGui.init(this.width, this.height, this.minecraft, this.menu);
		this.leftPos = this.grimoireGui.updateScreenPosition(this.width, this.imageWidth);
		this.children.add(this.grimoireGui);
		this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
		this.listener = new GrimoireListener(this.minecraft);
		this.minecraft.player.inventoryMenu.addSlotListener(this.listener);
		this.setInitialFocus(this.grimoireGui);
		initGrimoireGui();
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

	private void initGrimoireGui() {
		this.grimoireGui.initVisuals();
		this.leftPos = this.grimoireGui.updateScreenPosition(this.width, this.imageWidth);
	}

	public void tick() {
		super.tick();
		// this.grimoireGui.tick();
	}

	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(mStack);
		super.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		this.grimoireGui.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		// this.componentGui.renderGhostRecipe(mStack, this.leftPos, this.topPos, true,
		// p_230430_4_);

		this.renderTooltip(mStack, p_230430_2_, p_230430_3_);
		this.grimoireGui.renderTooltip(mStack, this.leftPos, this.topPos, p_230430_2_, p_230430_3_);

	}

	@SuppressWarnings("deprecation")
	protected void renderBg(MatrixStack mStack, float p_230430_4_, int mouseX, int mouseY) {
		int i = this.leftPos;
//		int j = (this.height - this.imageHeight) / 2;
		int j = this.topPos;
		int k = this.imageWidth / 2;
		this.minecraft.getTextureManager().bind(GRIMOIRE_LOCATION);
		mStack.pushPose();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.blit(mStack, i, j, 20, 0, k, this.imageHeight);
		mStack.popPose();
		mStack.pushPose();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.blit(mStack, i + k, j, 20, 0, k, this.imageHeight);
		mStack.popPose();
		mStack.pushPose();
		this.minecraft.getTextureManager().bind(SpellBindingScreen.SPELLBINDER_LOCATION);
		for (Slot slot : container.slots) {
			if (slot instanceof GrimoireSpellSlot) {
				this.blit(mStack, i + slot.x - 1, j + slot.y - 1, 0, 166, 18, 18);
//				this.font.draw(mStack, slot.getItem().getDisplayName(), i + slot.x, j + slot.y, 4210752);
			} else {
				this.blit(mStack, i + slot.x - 1, j + slot.y - 1, 0, 166, 18, 18);
			}

		}
		mStack.popPose();
		this.renderSpellPage(mStack, mouseX, mouseY, p_230430_4_);
	}

	protected void renderSpellPage(MatrixStack mStack, int mouseX, int mouseY, float useless) {
		if (this.menu.getSelectedSpell() == null)
			return;
		this.font.draw(mStack, this.menu.getSelectedSpell().getSpell().getName(), this.width / 2, 8, 4210752);

	}

	@Override
	protected void renderLabels(MatrixStack mStack, int x, int y) {
		this.font.draw(mStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
	}

	@Override
	protected boolean isHovering(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_,
			double p_195359_7_) {
		return super.isHovering(p_195359_1_, p_195359_2_, p_195359_3_, p_195359_4_, p_195359_5_, p_195359_7_);
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		if (this.grimoireGui.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
			this.setFocused(this.grimoireGui);
			return true;
		} else {
			return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
		}
	}

	@Override
	protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_,
			int p_195361_7_) {
		boolean flag = p_195361_1_ < (double) p_195361_5_ || p_195361_3_ < (double) p_195361_6_
				|| p_195361_1_ >= (double) (p_195361_5_ + this.imageWidth)
				|| p_195361_3_ >= (double) (p_195361_6_ + this.imageHeight);
		return this.grimoireGui.hasClickedOutside(p_195361_1_, p_195361_3_, this.leftPos, this.topPos, this.imageWidth,
				this.imageHeight, p_195361_7_) && flag;
	}

	@Override
	protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
		super.slotClicked(slot, mouseX, mouseY, clickType);
//		if (slot == null || !(slot instanceof SpellBindingRuneSlot)) {
//			return;
//		}

		// Quand je clique sur un spellSlot:
		// S'il en résulte qu'il reste un spellcomponent dans le slot, alors
		// sélectionner ça
		// S'il en résulte que dans le selectedComponent actuel il n'y en a plus, alors
		// kill it.

		// QUand le slot change :

//		if (!slot.equals(selectedSlot)) {
//			selectSlot(slot);
//		}
//		this.componentGui.slotClicked(slot);

	}

	public void removed() {
		if (this.minecraft.player != null && this.minecraft.player.inventory != null) {
			this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
		}
		this.grimoireGui.removed();
		super.removed();
	}
}