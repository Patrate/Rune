package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.gui.StarHelper;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class LevelWidget extends PropertyWidget<LevelProperty> {

	protected LevelWidget(LevelProperty property, AbstractSpellComponent component, int x, int y, int width,
			int height) {
		super(property, component, x, y, width, height);
	}

	@Override
	protected void internalRender(MatrixStack mStack, int a, int b, float c) {
		int maxLevel = this.getProperty().getMaxLevel();
		int level = this.getProperty().getValue();
		boolean boostable = this.getProperty().isBoostable();

		int baseX = this.x + 18;
		int baseY = this.y + 9;
		Minecraft minecraft = Minecraft.getInstance();

		mStack.pushPose();
		ForgeIngameGui.drawString(mStack, minecraft.font,
				new StringTextComponent("niveau " + level + " sur " + maxLevel), this.x + 2, this.y + 2,
				Color.WHITE.getRGB());
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
		System.out.println("CLIC ! " + this.getProperty());
		int newLevel = (this.getProperty().getValue()) % this.getProperty().getMaxLevel() + 1;
		this.getProperty().setValueInternal(newLevel);
	}
}
