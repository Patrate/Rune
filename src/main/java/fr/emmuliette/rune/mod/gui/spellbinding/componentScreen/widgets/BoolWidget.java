package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.gui.StarHelper;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.common.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;

public class BoolWidget extends PropertyWidget<BoolProperty> {

	protected BoolWidget(Grade grade, BoolProperty property, AbstractSpellComponent component, int x, int y) {
		super(grade, property, component, x, y, 1);
	}

	@Override
	protected void internalRender(MatrixStack mStack, int a, int b, float c) {
		int baseX = this.x + 5;
		int baseY = this.y + 15;

		mStack.pushPose();
		StarHelper.start();
		if (this.getProperty().getValue()) {
			StarHelper.blit(mStack, baseX, baseY, StarHelper.Coord.BORDER_BLACK_FULL);
			StarHelper.blit(mStack, baseX, baseY, StarHelper.Coord.LIGHT_FULL);
		} else {
			StarHelper.blit(mStack, baseX, baseY, StarHelper.Coord.BORDER_RED_FULL);
			StarHelper.blit(mStack, baseX, baseY, StarHelper.Coord.PINK_FULL);
		}
		StarHelper.stop();
		mStack.popPose();
	}

	@Override
	protected void internalClic(double x, double y) {
		System.out.println("CLIC ! " + this.getProperty());
		try {
			this.getProperty().setValueInternal(!this.getProperty().getValue());
		} catch (PropertyException e) {
			e.printStackTrace();
		}
	}
}
