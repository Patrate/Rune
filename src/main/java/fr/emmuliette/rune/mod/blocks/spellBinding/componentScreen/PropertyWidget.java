package fr.emmuliette.rune.mod.blocks.spellBinding.componentScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PropertyWidget extends Widget {
	private static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
//	private static final ITextComponent MORE_RECIPES_TOOLTIP = new TranslationTextComponent(
//			"gui.recipebook.moreRecipes");
	private RecipeBook book;
	private RecipeList collection;
	private float animationTime;
	private int currentIndex;
	private AbstractSpellComponent component;
	private Property<?> property;

	public PropertyWidget(Property<?> property, AbstractSpellComponent component, int x, int y, int width, int height) {
		super(x, y, width, height, StringTextComponent.EMPTY);
		this.component = component;
		this.property = property;
		this.width = width;
		this.height = height;
	}

	public void init(RecipeList recipeList, ComponentPage page) {
		this.collection = recipeList;
		this.book = page.getRecipeBook();
		List<IRecipe<?>> list = recipeList.getRecipes(false);

		for (IRecipe<?> irecipe : list) {
			if (this.book.willHighlight(irecipe)) {
				this.animationTime = 15.0F;
				break;
			}
		}

	}

	public RecipeList getCollection() {
		return this.collection;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@SuppressWarnings("deprecation")
	public void renderButton(MatrixStack mStack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bind(RECIPE_BOOK_LOCATION);
		int i = 29;
//		if (!this.collection.hasCraftable()) {
//			i += 25;
//		}

		int j = 206;
//		if (this.collection.getRecipes(false).size() > 1) {
//			j += 25;
//		}

		boolean flag = this.animationTime > 0.0F;
		if (flag) {
			float f = 1.0F + 0.1F * (float) Math.sin((double) (this.animationTime / 15.0F * (float) Math.PI));
			RenderSystem.pushMatrix();
			RenderSystem.translatef((float) (this.x + 8), (float) (this.y + 12), 0.0F);
			RenderSystem.scalef(f, f, 1.0F);
			RenderSystem.translatef((float) (-(this.x + 8)), (float) (-(this.y + 12)), 0.0F);
			this.animationTime -= p_230431_4_;
		}

		this.blit(mStack, this.x, this.y, i, j, this.width, this.height);
		// List<IRecipe<?>> list = this.getOrderedRecipes();
		// this.currentIndex = 0;// MathHelper.floor(this.time / 30.0F) % list.size();
		ItemStack itemstack = new ItemStack(Items.APPLE);// list.get(this.currentIndex).getResultItem();
		minecraft.getItemRenderer().renderAndDecorateFakeItem(itemstack, this.x + 4, this.y + 4);
		if (flag) {
			RenderSystem.popMatrix();
		}

	}

	private List<IRecipe<?>> getOrderedRecipes() {
		return new ArrayList<IRecipe<?>>();
//		List<IRecipe<?>> list = this.collection.getDisplayRecipes(true);
//		list.addAll(this.collection.getDisplayRecipes(false));
//		return list;
	}

	public IRecipe<?> getRecipe() {
		List<IRecipe<?>> list = this.getOrderedRecipes();
		return list.get(this.currentIndex);
	}

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
							new StringTextComponent(property.toString()) }));

		return list;
	}

	public int getWidth() {
		return 25;
	}

	protected boolean isValidClickButton(int p_230987_1_) {
		return p_230987_1_ == 0 || p_230987_1_ == 1;
	}
}