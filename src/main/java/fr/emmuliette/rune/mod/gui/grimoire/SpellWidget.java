package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class SpellWidget extends Widget {
	private static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
	//private RecipeBook book;
	//private RecipeList collection;
	private float time;
	private float animationTime;
	private int currentIndex;
	private List<Spell> spellList;

	public SpellWidget(Spell spell) {
		this(Lists.newArrayList(spell));
	}
	
	public SpellWidget(List<Spell> spellList) {
	      super(0, 0, 25, 25, StringTextComponent.EMPTY);
	      this.spellList = new ArrayList<Spell>();
	      for(Spell spell:spellList) {
	    	  this.spellList.add(spell);
	      }
	   }

	public void init() {
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@SuppressWarnings("deprecation")
	public void renderButton(MatrixStack matrixStack, int mouse_X, int mouse_Y, float partial_tick) {
		if (!Screen.hasControlDown()) {
			this.time += partial_tick;
		}

		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bind(RECIPE_BOOK_LOCATION);
		int i = 29;

		int j = 206;
		if (this.spellList.size() > 1) {
			j += 25;
		}

		boolean flag = this.animationTime > 0.0F;
		if (flag) {
			float f = 1.0F + 0.1F * (float) Math.sin((double) (this.animationTime / 15.0F * (float) Math.PI));
			RenderSystem.pushMatrix();
			RenderSystem.translatef((float) (this.x + 8), (float) (this.y + 12), 0.0F);
			RenderSystem.scalef(f, f, 1.0F);
			RenderSystem.translatef((float) (-(this.x + 8)), (float) (-(this.y + 12)), 0.0F);
			this.animationTime -= partial_tick;
		}

		this.blit(matrixStack, this.x, this.y, i, j, this.width, this.height);
		List<Spell> list = this.getSpellList();
		this.currentIndex = MathHelper.floor(this.time / 30.0F) % list.size();
		ItemStack itemstack = list.get(this.currentIndex).getResultItem();
		int k = 4;
		if (this.getSpellList().size() > 1) {
			minecraft.getItemRenderer().renderAndDecorateItem(itemstack, this.x + k + 1, this.y + k + 1);
			--k;
		}

		minecraft.getItemRenderer().renderAndDecorateFakeItem(itemstack, this.x + k, this.y + k);
		if (flag) {
			RenderSystem.popMatrix();
		}

	}
	
	private List<Spell> getSpellList() {
		return spellList;
	}

	public boolean isOnlyOption() {
		return this.getSpellList().size() == 1;
	}

	public Spell getCurrentSpell() {
		List<Spell> list = this.getSpellList();
		return list.get(this.currentIndex);
	}

	public List<ITextComponent> getTooltipText(Screen p_191772_1_) {
		ItemStack itemstack = this.getSpellList().get(this.currentIndex).getResultItem();
		List<ITextComponent> list = Lists.newArrayList(p_191772_1_.getTooltipFromItem(itemstack));
		/*if (this.collection.getRecipes(this.book.isFiltering(this.menu)).size() > 1) {
			list.add(MORE_RECIPES_TOOLTIP);
		}*/

		return list;
	}

	public int getWidth() {
		return 25;
	}

	protected boolean isValidClickButton(int p_230987_1_) {
		return p_230987_1_ == 0 || p_230987_1_ == 1;
	}
}