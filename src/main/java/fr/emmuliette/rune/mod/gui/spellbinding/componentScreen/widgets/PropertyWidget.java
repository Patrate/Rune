package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.ComponentPage;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.EnumProperty;
import fr.emmuliette.rune.mod.spells.properties.LevelProperty;
import fr.emmuliette.rune.mod.spells.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class PropertyWidget<T extends Property<?>> extends Widget {
	private static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
//	private static final ITextComponent MORE_RECIPES_TOOLTIP = new TranslationTextComponent(
//			"gui.recipebook.moreRecipes");
	private AbstractSpellComponent component;
	private T property;
	private ComponentPage page;

	public static PropertyWidget<?> buildWidget(ComponentPage page, Property<?> property,
			AbstractSpellComponent component, int x, int y, int width, int height) {
		PropertyWidget<?> retour;
		if (property instanceof LevelProperty)
			retour = new LevelWidget((LevelProperty) property, component, x, y, width, height);
		else if (property instanceof BoolProperty)
			retour = new BoolWidget((BoolProperty) property, component, x, y, width, height);
		else if (property instanceof EnumProperty)
			retour = new EnumWidget((EnumProperty) property, component, x, y, width, height);
		else
			return null;
		retour.page = page;
		return retour;
	}

	protected PropertyWidget(T property, AbstractSpellComponent component, int x, int y, int width, int height) {
		super(x, y, width, height, StringTextComponent.EMPTY);
		this.component = component;
		this.property = property;
		this.width = width;
		this.height = height;
	}

	public void init(ComponentPage page) {
//		this.collection = recipeList;
//		this.book = page.getRecipeBook();
//		List<IRecipe<?>> list = recipeList.getRecipes(false);
//
//		for (IRecipe<?> irecipe : list) {
//			if (this.book.willHighlight(irecipe)) {
//				this.animationTime = 15.0F;
//				break;
//			}
//		}

	}

//	public RecipeList getCollection() {
//		return this.collection;
//	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void renderButton(MatrixStack mStack, int a, int b, float c) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bind(RECIPE_BOOK_LOCATION);
		int i = 29;

		int j = 206;

		this.blit(mStack, this.x, this.y, i, j, this.width, this.height);

		internalRender(mStack, a, b, c);
	}

	protected abstract void internalRender(MatrixStack mStack, int a, int b, float c);

	public List<ITextComponent> getTooltipText(Screen screen) {
		/*
		 * ItemStack itemstack =
		 * this.getOrderedRecipes().get(this.currentIndex).getResultItem();
		 * List<ITextComponent> list =
		 * Lists.newArrayList(screen.getTooltipFromItem(itemstack)); if
		 * (this.collection.getRecipes(this.book.isFiltering(this.menu)).size() > 1) {
		 * list.add(MORE_RECIPES_TOOLTIP); }
		 */
		List<ITextComponent> list = new ArrayList<ITextComponent>();
		if (component != null)
			list.addAll(component.getTooltips());
		if (property != null)
			list.addAll(Arrays
					.asList(new StringTextComponent[] { new StringTextComponent(property.getClass().getSimpleName()),
							new StringTextComponent(property.getDescription()) }));

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