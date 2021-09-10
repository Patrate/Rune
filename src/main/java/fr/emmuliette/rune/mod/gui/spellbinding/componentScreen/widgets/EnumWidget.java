package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.common.EnumProperty;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class EnumWidget extends PropertyWidget<EnumProperty> {
	private List<String> internalValues;
	private int internalId;

	protected EnumWidget(Grade grade, EnumProperty property, AbstractSpellComponent component, int x, int y) {
		super(grade, property, component, x, y, 1);
		this.internalValues = new ArrayList<String>(property.getValues(grade));
		this.internalId = internalValues.indexOf(this.getProperty().getValue());
	}

	@Override
	protected void internalRender(MatrixStack mStack, int a, int b, float c) {
		Minecraft minecraft = Minecraft.getInstance();
		mStack.pushPose();
		ForgeIngameGui.drawString(mStack, minecraft.font, new StringTextComponent(this.getProperty().getValue()),
				this.x + 5, this.y + 15, Color.WHITE.getRGB());
		mStack.popPose();
	}

	@Override
	protected void internalClic(double x, double y) {
		System.out.println("CLIC ! " + this.getProperty());
		if (internalValues.isEmpty())
			return;
		internalId = (internalId + 1) % internalValues.size();
		try {
			this.getProperty().setValue(internalValues.get(internalId));
		} catch (PropertyException e) {
			e.printStackTrace();
		}
	}
}
