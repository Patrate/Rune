package fr.emmuliette.rune.mod.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.mod.gui.scripting.RuneUtils;
import fr.emmuliette.rune.mod.gui.scripting.ScriptingScreen;
import fr.emmuliette.rune.mod.items.ModItems;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GrimoireBookScreen extends ReadBookScreen {
	public static final int MATRIX_SIZE = 32;
	private Double[][] matrix;
	private GrimoireInfo access;
	private int currentPage;
	private int cachedpage;

	public GrimoireBookScreen(GrimoireInfo p_i51098_1_) {
		super(p_i51098_1_);
		access = p_i51098_1_;
		matrix = null;
		currentPage = 0;
		cachedpage = -1;
	}

	@Override
	protected void pageBack() {
		if (this.currentPage > 0) {
			--this.currentPage;
		}
		super.pageBack();
	}

	@Override
	protected void pageForward() {
		if (this.currentPage < this.getNumPages() - 1) {
			++this.currentPage;
		}
		super.pageForward();
	}

	@Override
	public boolean setPage(int p_214160_1_) {
		int i = MathHelper.clamp(p_214160_1_, 0, this.access.getPageCount() - 1);
		if (i != this.currentPage) {
			this.currentPage = i;
			this.cachedpage = -1;
		}
		return super.setPage(p_214160_1_);
	}

	private int getNumPages() {
		return this.access.getPageCount();
	}

	public void render(MatrixStack mStack, int a, int b, float partialTick) {
		if (cachedpage != currentPage) {
			matrix = access.getMatrix(currentPage);
			cachedpage = currentPage;
		}
		super.render(mStack, a, b, partialTick);
		renderDrawing(mStack, a, b, partialTick);
	}

	@SuppressWarnings("deprecation")
	private void renderDrawing(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (matrix == null)
			return;
		int x = (this.width - 64 - MATRIX_SIZE) / 2;
		int y = 86;
		mStack.pushPose();
		RenderSystem.color4f(.5F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(ScriptingScreen.SCRIPTING_LOCATION);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 0)
					continue;

				this.blit(mStack, x + i * ScriptingScreen.PIX_SIZE, y + j * ScriptingScreen.PIX_SIZE, 36, 166,
						ScriptingScreen.PIX_SIZE, ScriptingScreen.PIX_SIZE);
			}
		}

		mStack.popPose();
	}

	@OnlyIn(Dist.CLIENT)
	public static class GrimoireInfo implements ReadBookScreen.IBookInfo {
		private final List<String> pages;
		private final List<ModItems> runes;

		public GrimoireInfo(ItemStack p_i50616_1_) {
			this.pages = readPages(p_i50616_1_);
			this.runes = new ArrayList<ModItems>();
			ListNBT listnbt = p_i50616_1_.getTag().getList("rune", 8).copy();

			for (int i = 0; i < listnbt.size(); ++i) {
				runes.add(ModItems.valueOf(listnbt.getString(i)));
			}
		}

		public Double[][] getMatrix(int i) {
			return RuneUtils.getRune(MATRIX_SIZE, runes.get(i).get());
		}

		private static List<String> readPages(ItemStack p_216921_0_) {
			CompoundNBT compoundnbt = p_216921_0_.getTag();
			return (List<String>) (compoundnbt != null && WrittenBookItem.makeSureTagIsValid(compoundnbt)
					? ReadBookScreen.convertPages(compoundnbt)
					: ImmutableList.of(ITextComponent.Serializer.toJson(
							(new TranslationTextComponent("book.invalid.tag")).withStyle(TextFormatting.DARK_RED))));
		}

		public int getPageCount() {
			return this.pages.size();
		}

		public ITextProperties getPageRaw(int p_230456_1_) {
			String s = this.pages.get(p_230456_1_);

			try {
				ITextProperties itextproperties = ITextComponent.Serializer.fromJson(s);
				if (itextproperties != null) {
					return itextproperties;
				}
			} catch (Exception exception) {
			}

			return ITextProperties.of(s);
		}
	}
}
