package fr.emmuliette.rune.mod.gui.spelliverse;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class SpelliverseTabToggleWidget extends ToggleWidget {
	public enum SpelliverseTab {
		MAIN(new ItemStack(Items.CRAFTING_TABLE)), PERSO(new ItemStack(Items.CHAINMAIL_CHESTPLATE)),
		SEARCH(new ItemStack(Items.COMPASS));

		private final ItemStack itemIcon;

		private SpelliverseTab(ItemStack icon) {
			this.itemIcon = icon.copy();
		}

		public ItemStack getIconItem() {
			return this.itemIcon;
		}

	}

	private final SpelliverseTab tab;

	public SpelliverseTabToggleWidget(SpelliverseTab tab) {
		super(0, 0, 35, 27, false);
		this.tab = tab;
		this.initTextureValues(153, 2, 35, 0, SpelliverseScreen.ICON_LOCATION);
	}

	@SuppressWarnings("deprecation")
	public void renderButton(MatrixStack mStack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
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
	}

	private void renderIcon(ItemRenderer itemRenderer) {
		ItemStack iconItem = this.tab.getIconItem();
		int i = this.isStateTriggered ? -2 : 0;
		itemRenderer.renderAndDecorateFakeItem(iconItem, this.x + 9 + i, this.y + 5);
	}

	public SpelliverseTab getTab() {
		return this.tab;
	}
}
