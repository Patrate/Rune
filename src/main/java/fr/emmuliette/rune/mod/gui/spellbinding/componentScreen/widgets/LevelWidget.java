package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.gui.StarHelper;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.common.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class LevelWidget extends PropertyWidget<LevelProperty> {

	protected LevelWidget(Grade grade, LevelProperty property, AbstractSpellComponent component, int x, int y) {
		super(grade, property, component, x, y, 1);
	}

	@Override
	protected void internalRender(MatrixStack mStack, int a, int b, float c) {
		int maxLevel = this.getProperty().getMaxLevel();
		int level = this.getProperty().getValue();
		boolean boostable = this.getProperty().isBoostable();

		int baseX = this.x + 5;
		int baseY = this.y + 15;
		Minecraft minecraft = Minecraft.getInstance();

		mStack.pushPose();
		ForgeIngameGui.drawString(mStack, minecraft.font, new StringTextComponent(level + "/" + maxLevel),
				this.x + this.width - 50, this.y + 5, Color.WHITE.getRGB());
		mStack.popPose();

		mStack.pushPose();
		StarHelper.start();
		for (int i = 0; i < level; i++) {
			if (boostable) {
				StarHelper.blit(mStack, baseX + 9 * i, baseY, StarHelper.Coord.BORDER_YELLOW_FULL);
			}
			StarHelper.blit(mStack, baseX + 9 * i, baseY, StarHelper.Coord.LIGHT_FULL);
		}
		for (int i = level; i < maxLevel; i++) {
			if (boostable) {
				StarHelper.blit(mStack, baseX + 9 * i, baseY, StarHelper.Coord.BORDER_RED_FULL);
			}
			StarHelper.blit(mStack, baseX + 9 * i, baseY, StarHelper.Coord.DARK_FULL);
		}
		StarHelper.stop();
		mStack.popPose();
	}

	@Override
	protected void internalClic(double x, double y) {
		int X = (int) (x - this.x); // 88
		int Y = (int) (y - this.y); // 8

		System.out.println(X + "/" + Y + ", clicked " + x + "/" + y);

		int newLevel = this.getProperty().getValue();

		if (X <= 33) {
			newLevel--;
		} else if (X <= 66) {
			newLevel++;
		} else {
			this.getProperty().setBoostable(!this.getProperty().isBoostable());
		}
		if (newLevel > this.getProperty().getMaxLevel()) {
			newLevel = 1;
		}
		if (newLevel < 1)
			newLevel = this.getProperty().getMaxLevel();
		try {
			this.getProperty().setValueInternal(newLevel);
		} catch (PropertyException e) {
			e.printStackTrace();
		}
	}
}
