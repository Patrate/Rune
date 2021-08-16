package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.gui.StarHelper;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.BoolProperty;

public class BoolWidget extends PropertyWidget<BoolProperty> {

	protected BoolWidget(BoolProperty property, AbstractSpellComponent component, int x, int y, int width, int height) {
		super(property, component, x, y, width, height);
	}

	@Override
	protected void internalRender(MatrixStack mStack, int a, int b, float c) {
		int baseX = this.x + 18;
		int baseY = this.y + 9;

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
		this.getProperty().setValueInternal(!this.getProperty().getValue());
	}
}
