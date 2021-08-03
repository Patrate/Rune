package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireTabToggleWidget extends ToggleWidget {
	private final GrimoireCategories category;
	private float animationTime;

	public GrimoireTabToggleWidget(GrimoireCategories p_i51075_1_) {
		super(0, 0, 35, 27, false);
		this.category = p_i51075_1_;
		this.initTextureValues(153, 2, 35, 0, GrimoireGui.RECIPE_BOOK_LOCATION);
	}

	public void startAnimation(Minecraft minecraft) {
//		Grimoire clientGrimoire = minecraft.player.getGrimoire();
//		List<RecipeList> list = clientGrimoire.getCollection(this.category);
//		if (minecraft.player.containerMenu instanceof GrimoireContainer) {
//			for (RecipeList recipelist : list) {
//				for (IRecipe<?> irecipe : recipelist.getRecipes(
//						clientGrimoire.isFiltering((GrimoireContainer) minecraft.player.containerMenu))) {
//					if (clientGrimoire.willHighlight(irecipe)) {
//						this.animationTime = 15.0F;
//						return;
//					}
//				}
//			}
//
//		}
	}

	@SuppressWarnings("deprecation")
	public void renderButton(MatrixStack mStack, int mouseX, int mouseY, float p_230431_4_) {
		if (this.animationTime > 0.0F) {
			float f = 1.0F + 0.1F * (float) Math.sin((double) (this.animationTime / 15.0F * (float) Math.PI));
			RenderSystem.pushMatrix();
			RenderSystem.translatef((float) (this.x + 8), (float) (this.y + 12), 0.0F);
			RenderSystem.scalef(1.0F, f, 1.0F);
			RenderSystem.translatef((float) (-(this.x + 8)), (float) (-(this.y + 12)), 0.0F);
		}

		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bind(this.resourceLocation);
		RenderSystem.disableDepthTest();
		int i = this.xTexStart;
		int j = this.yTexStart;
		if (this.isStateTriggered) {
			i += this.xDiffTex;
		}

		if (this.isHovered()) {
			j += this.yDiffTex;
		}

		int k = this.x;
		if (this.isStateTriggered) {
			k -= 2;
		}

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.blit(mStack, k, this.y, i, j, this.width, this.height);
		RenderSystem.enableDepthTest();
		this.renderIcon(minecraft.getItemRenderer());
		if (this.animationTime > 0.0F) {
			RenderSystem.popMatrix();
			this.animationTime -= p_230431_4_;
		}

	}

	private void renderIcon(ItemRenderer p_193920_1_) {
		List<ItemStack> list = this.category.getIconItems();
		int i = this.isStateTriggered ? -2 : 0;
		if (list.size() == 1) {
			p_193920_1_.renderAndDecorateFakeItem(list.get(0), this.x + 9 + i, this.y + 5);
		} else if (list.size() == 2) {
			p_193920_1_.renderAndDecorateFakeItem(list.get(0), this.x + 3 + i, this.y + 5);
			p_193920_1_.renderAndDecorateFakeItem(list.get(1), this.x + 14 + i, this.y + 5);
		}

	}

	public GrimoireCategories getCategory() {
		return this.category;
	}
}
