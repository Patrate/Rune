package fr.emmuliette.rune.mod.gui.grimoire;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GrimoireScreen extends ContainerScreen<GrimoireContainer> implements IContainerListener {
	public static final ResourceLocation GRIMOIRE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/grimoire.png");

//	private final GrimoireGui grimoireGui = new GrimoireGui();
//	private GrimoireContainer container;

	public GrimoireScreen(GrimoireContainer container, PlayerInventory playerInventory, ITextComponent textComp) {
		super(container, playerInventory, textComp);
	}

	protected void init() {
		super.init();
		this.imageWidth = 146 * 2;
		this.imageHeight = 192;
//		this.grimoireGui.init(this.width, this.height, this.minecraft, this.menu);
//		this.leftPos = this.grimoireGui.updateScreenPosition(this.width, this.imageWidth);
//		this.children.add(this.grimoireGui);
		this.menu.addSlotListener(this);
//		this.setInitialFocus(this.grimoireGui);
		minecraft.player.containerMenu = menu;
		initGrimoireGui();
	}

	private void initGrimoireGui() {
		this.leftPos = (this.width - this.imageWidth) / 2;
//		this.grimoireGui.initVisuals();
//		this.leftPos = this.grimoireGui.updateScreenPosition(this.width, this.imageWidth);
	}

	public void tick() {
		super.tick();
		// this.grimoireGui.tick();
	}

	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(mStack);
		super.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
//		this.grimoireGui.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);

		this.renderTooltip(mStack, p_230430_2_, p_230430_3_);
//		this.grimoireGui.renderTooltip(mStack, this.leftPos, this.topPos, p_230430_2_, p_230430_3_);

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
		this.minecraft.getTextureManager().bind(GRIMOIRE_LOCATION);
		for (int slotId = 0; slotId < this.menu.slots.size(); slotId++) {
			Slot slot = this.menu.slots.get(slotId);
			if (slotId < this.menu.getSpellCount()) {
				this.blit(mStack, i + slot.x - 1, j + slot.y - 1, 0, 222, 18, 18);
			} else {
				this.blit(mStack, i + slot.x - 1, j + slot.y - 1, 0, 222, 18, 18);
			}
		}
		mStack.popPose();
		mStack.pushPose();
		for (int slotId = 0; slotId < this.menu.getSpellCount(); slotId++) {
			Slot slot = this.menu.slots.get(slotId);
			this.font.draw(mStack, slot.getItem().getItem().getName(slot.getItem()), i + slot.x + 20, j + slot.y + 2,
					4210752);
		}

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
//		if (this.grimoireGui.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
//			this.setFocused(this.grimoireGui);
//			return true;
//		} else {
		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
//		}
	}

	@Override
	protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_,
			int p_195361_7_) {
		return super.hasClickedOutside(p_195361_1_, p_195361_3_, p_195361_5_, p_195361_6_, p_195361_7_);
//		boolean flag = p_195361_1_ < (double) p_195361_5_ || p_195361_3_ < (double) p_195361_6_
//				|| p_195361_1_ >= (double) (p_195361_5_ + this.imageWidth)
//				|| p_195361_3_ >= (double) (p_195361_6_ + this.imageHeight);
//		return this.grimoireGui.hasClickedOutside(p_195361_1_, p_195361_3_, this.leftPos, this.topPos, this.imageWidth,
//				this.imageHeight, p_195361_7_) && flag;
	}

	@Override
	protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
		super.slotClicked(slot, mouseX, mouseY, clickType);
	}

	public void removed() {
		if (this.minecraft.player != null && this.minecraft.player.inventory != null) {
			this.minecraft.player.inventoryMenu.removeSlotListener(this);
		}
//		this.grimoireGui.removed();
		super.removed();
	}

	public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {
		try {
			throw new Exception("Refreshing container GrimoireScreen: " + p_71110_2_);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void slotChanged(Container container, int slot, ItemStack item) {
		try {
			throw new Exception("SlotChanged container GrimoireScreen: " + slot + ", " + item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
	}
}