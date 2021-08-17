package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.ComponentGui;
import fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.ComponentPage;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.EnumProperty;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.GridProperty;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;

@OnlyIn(Dist.CLIENT)
public abstract class PropertyWidget<T extends Property<?>> extends Widget {
	private T property;
	private ComponentPage page;
	private int size;

	public static PropertyWidget<?> buildWidget(ComponentPage page, Grade grade, Property<?> property,
			AbstractSpellComponent component, int x, int y) {
		PropertyWidget<?> retour;
		if (property instanceof LevelProperty)
			retour = new LevelWidget(grade, (LevelProperty) property, component, x, y);
		else if (property instanceof BoolProperty)
			retour = new BoolWidget(grade, (BoolProperty) property, component, x, y);
		else if (property instanceof EnumProperty)
			retour = new EnumWidget(grade, (EnumProperty) property, component, x, y);
		else if (property instanceof GridProperty)
			retour = new GridWidget(grade, (GridProperty) property, component, x, y);
		else
			return null;
		retour.page = page;
		return retour;
	}

	protected PropertyWidget(Grade grade, T property, AbstractSpellComponent component, int x, int y, int size) {
		super(x, y, 150, 32 * size, StringTextComponent.EMPTY);
		this.property = property;
		this.width = 150;
		this.height = 32 * size;
		this.property.setCurrentGrade(grade);
		this.size = size;
	}

	public void init(ComponentPage page) {
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getSize() {
		return size;
	}

	public void renderButton(MatrixStack mStack, int a, int b, float c) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bind(ComponentGui.COMPONENT_PAGE_LOCATION);
		int iY = this.y;
		for (int i = 0; i < this.size; i++) {
			if (i == 0) {
				this.blit(mStack, this.x, iY, 1, 168, this.width, 16);
			} else {
				this.blit(mStack, this.x, iY, 1, 200, this.width, 16);
			}
			if (i + 1 == this.size) {
				this.blit(mStack, this.x, iY + 16, 1, 184, this.width, 16);
			} else {
				this.blit(mStack, this.x, iY + 16, 1, 200, this.width, 16);
			}
			iY += 32;
		}

		mStack.pushPose();
		ForgeIngameGui.drawString(mStack, minecraft.font, new StringTextComponent(this.getProperty().getName()),
				this.x + 5, this.y + 5, Color.WHITE.getRGB());
		mStack.popPose();

		internalRender(mStack, a, b, c);
	}

	protected abstract void internalRender(MatrixStack mStack, int a, int b, float c);

	public List<ITextComponent> getTooltipText(Screen screen) {
		List<ITextComponent> list = new ArrayList<ITextComponent>();
//		if (component != null)
//			list.addAll(component.getTooltips());
//		if (property != null)
//		list.addAll(Arrays
//		.asList(new StringTextComponent[] { new StringTextComponent(property.getClass().getSimpleName()),
//				new StringTextComponent(property.getDescription()) }));
		if (property != null)
			list.addAll(
					Arrays.asList(new StringTextComponent[] { new StringTextComponent(property.getDescription()) }));

		return list;
	}

	public int getWidth() {
		return 25;
	}

	protected boolean isValidClickButton(int p_230987_1_) {
		return p_230987_1_ == 0 || p_230987_1_ == 1;
	}

	public T getProperty() {
		return this.property;
	}

	protected abstract void internalClic(double x, double y);

	@Override
	protected boolean clicked(double x, double y) {
		if (super.clicked(x, y)) {
			internalClic(x, y);
			this.page.getMenu().propertyChanged();
			return true;
		}
		return false;
	}
}