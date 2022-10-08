package fr.emmuliette.rune.mod.gui.scripting;

import java.awt.Point;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScriptingScreen extends ContainerScreen<ScriptingContainer> implements IContainerListener {
	public static final ResourceLocation SCRIPTING_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/scripting.png");
	private static final int WIDTH = 165, HEIGHT = 75, PIX_SIZE = 6;

	private int drawLeft, drawTop, drawRight, drawBottom;

	public ScriptingScreen(ScriptingContainer container, PlayerInventory playerInventory, ITextComponent textComp) {
		super(container, playerInventory, textComp);
	}

	protected void init() {
		super.init();
		drawLeft = this.leftPos + 5;
		drawTop = this.topPos + 5;
		drawRight = drawLeft + WIDTH;
		drawBottom = drawTop + HEIGHT;
		// this.menu.addSlotListener(this);
	}

	public void tick() {
		super.tick();
	}

	@Override
	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(mStack);
		super.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		this.renderDrawing(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		this.renderTooltip(mStack, p_230430_2_, p_230430_3_);
	}

	@SuppressWarnings("deprecation")
	private void renderDrawing(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (this.menu.getPoints().size() < 2)
			return;
		mStack.pushPose();
		RenderSystem.color4f(.5F,1.0F,1.0F,1.0F);
		this.minecraft.getTextureManager().bind(SCRIPTING_LOCATION);
		for (Point p : this.menu.getPoints()) {
			this.blit(mStack, drawLeft + p.x, drawTop + p.y, 36, 166, PIX_SIZE, PIX_SIZE);
		}
		mStack.popPose();
	}

	@SuppressWarnings("deprecation")
	protected void renderBg(MatrixStack mStack, float useless3, int useless2, int useless) {
		mStack.pushPose();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(SCRIPTING_LOCATION);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(mStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		mStack.popPose();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
		if (mouseX >= drawLeft && mouseX <= drawRight && mouseY >= drawTop && mouseY <= drawBottom) {
			System.out.println("clic: " + mouseX + "/" + mouseY + "/" + clickType);
			menu.addPoint(mouseX - drawLeft, mouseY - drawTop, PIX_SIZE);
		}
		/*
		 * if(mouseX >= this.leftPos && mouseX <= this.leftPos + this.imageWidth) {
		 * if(mouseY >= this.topPos && mouseY <= this.topPos + this.imageHeight) { } }
		 */
		return super.mouseClicked(mouseX, mouseY, clickType);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int clicType, double deltaX, double deltaY) {
		double mX = mouseX + deltaX;
		double mY = mouseY + deltaY;
		if (mX >= drawLeft && mX <= drawRight && mY >= drawTop && mY <= drawBottom) {
			menu.addPoint(mouseX - drawLeft, mouseY - drawTop, PIX_SIZE);
		}
		// System.out.println("drag: " + mouseX + "/" + mouseY + " to " + deltaX + "/" +
		// deltaY + " " + clicType);
		return super.mouseDragged(mouseX, mouseY, clicType, deltaX, deltaY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int clickType) {
		// System.out.println("release: " + mouseX + "/" + mouseY + "/" + clickType);
		return super.mouseReleased(mouseX, mouseY, clickType);
	}

	public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
		System.out.println("settingContainerData");
	}

	@Override
	public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {
	}

	@Override
	public void slotChanged(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {

	}
}