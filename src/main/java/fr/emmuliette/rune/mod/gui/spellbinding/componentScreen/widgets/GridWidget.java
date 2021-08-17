package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.ComponentGui;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.BlockGrid;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.GridProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class GridWidget extends PropertyWidget<GridProperty> {
	private int currentFloor;
	private static final int BASE_X = 25, BASE_Y = 15, GRID_WIDTH = 7, GRID_HEIGHT = 7;;

	private ToggleWidget upButton;
	private ToggleWidget downButton;

	protected GridWidget(Grade grade, GridProperty property, AbstractSpellComponent component, int x, int y) {
		super(grade, property, component, x, y, 4);
		currentFloor = 0;
		this.upButton = new ToggleWidget(x + 5, y + 24, 17, 12, false);
		this.upButton.initTextureValues(149, 65, 18, 13, ComponentGui.COMPONENT_PAGE_LOCATION);
		this.downButton = new ToggleWidget(x + 5, y + 56, 17, 12, true);
		this.downButton.initTextureValues(149, 65, 18, 13, ComponentGui.COMPONENT_PAGE_LOCATION);
		updateArrowButtons();
	}

	private void updateArrowButtons() {
		this.upButton.visible = currentFloor < 3;
		this.downButton.visible = currentFloor > -3;
	}

	@Override
	protected void internalRender(MatrixStack mStack, int a, int b, float c) {
		int maxSize = this.getProperty().getMaxLevel();
		int currentSize = this.getProperty().getValue().getSize();

		int baseX = this.x + BASE_X;
		int baseY = this.y + BASE_Y;
		Minecraft minecraft = Minecraft.getInstance();

		mStack.pushPose();
		ForgeIngameGui.drawString(mStack, minecraft.font, new StringTextComponent(currentSize + "/" + maxSize),
				this.x + this.width - 50, this.y + 5, Color.WHITE.getRGB());
		ForgeIngameGui.drawString(mStack, minecraft.font, new StringTextComponent("" + currentFloor), this.x + 9,
				this.y + 42, Color.WHITE.getRGB());
		mStack.popPose();

		mStack.pushPose();
		GridHelper.start();

		BlockGrid grid = this.getProperty().getValue();
		int centerX = (GRID_WIDTH / 2), centerY = (GRID_HEIGHT / 2);

		for (int i = 0; i < GRID_WIDTH; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				if (grid.hasPos(i - centerX, currentFloor, j - centerY)) {
					if ((currentFloor == 0 || currentFloor == 1) && i == centerX && j == centerY) {
						GridHelper.blit(mStack, baseX + 9 * i, baseY + 9 * j, GridHelper.RED);
					} else {
						GridHelper.blit(mStack, baseX + 9 * i, baseY + 9 * j, GridHelper.BLUE);
					}
				} else {
					if ((currentFloor == 0 || currentFloor == 1) && i == centerX && j == centerY) {
						GridHelper.blit(mStack, baseX + 9 * i, baseY + 9 * j, GridHelper.CROSS);
					} else {
						GridHelper.blit(mStack, baseX + 9 * i, baseY + 9 * j, GridHelper.GREY);
					}
				}
			}
		}

		GridHelper.stop();
		mStack.popPose();

		this.upButton.render(mStack, a, b, c);
		this.downButton.render(mStack, a, b, c);
	}

	@Override
	public boolean mouseClicked(double x, double y, int mButton) {
		if (this.upButton.mouseClicked(x, y, mButton)) {
			currentFloor++;
			updateArrowButtons();
			return true;
		} else if (this.downButton.mouseClicked(x, y, mButton)) {
			currentFloor--;
			updateArrowButtons();
			return true;
		} else {
			return super.mouseClicked(x, y, mButton);
		}
	}

	@Override
	protected void internalClic(double x, double y) {
		int X = (int) (x - this.x); // 88
		int Y = (int) (y - this.y); // 8

		int centerX = (GRID_WIDTH / 2), centerY = (GRID_HEIGHT / 2);
		int minX = BASE_X, minY = BASE_Y, maxX = BASE_X + 7 * 9, maxY = BASE_Y + 7 * 9;

		if (X >= minX && X <= maxX && Y >= minY && Y <= maxY) {
			int xPos = (X - minX) / 9 - centerX, yPos = (Y - minY) / 9 - centerY;
			this.getProperty().getValue().togglePos(xPos, currentFloor, yPos);
		}
	}

	private static class GridHelper {
		static final int GREY = 0, BLUE = 1, RED = 2, CROSS = 3;

		static void blit(MatrixStack mStack, int x, int y, int coord) {
			ForgeIngameGui.blit(mStack, x, y, 1, 240, 30 + (float) coord * 10, 9, 9, 256, 256);
		}

		static void start() {
			Minecraft.getInstance().getTextureManager().bind(ComponentGui.COMPONENT_PAGE_LOCATION);
			RenderSystem.enableBlend();
		}

		static void stop() {
			RenderSystem.disableBlend();
		}
	}
}
