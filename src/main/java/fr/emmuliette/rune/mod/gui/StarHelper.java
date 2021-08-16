package fr.emmuliette.rune.mod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.gui.ForgeIngameGui;

public abstract class StarHelper {
	public enum Coord {
		BORDER_BLACK_FULL(0, 0), BORDER_BLACK_HLEFT(1, 0), BORDER_BLACK_HRIGHT(2, 0), BORDER_GREEN_FULL(3, 0),
		BORDER_GREEN_HLEFT(4, 0), BORDER_GREEN_HRIGHT(5, 0), BORDER_RED_FULL(6, 0), BORDER_RED_HLEFT(7, 0),
		BORDER_RED_HRIGHT(8, 0), BORDER_YELLOW_FULL(12, 0), BORDER_YELLOW_HLEFT(13, 0), BORDER_YELLOW_HRIGHT(14, 0),

		DARK_FULL(0, 1), DARK_HLEFT(1, 1), DARK_HRIGHT(2, 1), LIGHT_FULL(3, 1), LIGHT_HLEFT(4, 1), LIGHT_HRIGHT(5, 1),
		GREEN_FULL(6, 1), GREEN_HLEFT(7, 1), GREEN_HRIGHT(8, 1), PINK_FULL(9, 1), PINK_HLEFT(10, 1), PINK_HRIGHT(11, 1),

		;

		public int x;
		public int y;

		private Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/icons.png");

	public static void blit(MatrixStack mStack, int x, int y, Coord coord) {
		blit(mStack, x, y, coord.x, coord.y);
	}

	public static void blit(MatrixStack mStack, int x, int y, int i, int j) {
		ForgeIngameGui.blit(mStack, x, y, 1, (float) i * 9, (float) j * 9, 9, 9, 256, 256);
	}

	public static void drawString(MatrixStack mStack, FontRenderer font, ITextComponent textCmp, int x, int y,
			int rgbColor) {
		ForgeIngameGui.drawString(mStack, font, textCmp, x, y, rgbColor);
	}

	public static void start() {
		Minecraft.getInstance().getTextureManager().bind(GUI_ICONS_LOCATION);
		RenderSystem.enableBlend();
	}

	public static void stop() {
		RenderSystem.disableBlend();
	}
}
