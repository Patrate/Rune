package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.EnumProperty;

public class EnumWidget extends PropertyWidget<EnumProperty> {

	protected EnumWidget(EnumProperty property, AbstractSpellComponent component, int x, int y, int width, int height) {
		super(property, component, x, y, width, height);
	}

	@Override
	protected void internalRender(MatrixStack mStack, int a, int b, float c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void internalClic(double x, double y) {
		System.out.println("CLIC ! " + this.getProperty());
	}
}
