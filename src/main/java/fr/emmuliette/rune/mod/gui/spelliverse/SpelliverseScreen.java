package fr.emmuliette.rune.mod.gui.spelliverse;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.gui.spelliverse.SpelliverseTabToggleWidget.SpelliverseTab;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SpelliverseScreen extends ContainerScreen<SpelliverseContainer> implements IContainerListener {
	public static final ResourceLocation SPELLIVERSE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/spelliverse.png");
	protected static final ResourceLocation ICON_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/spelliverse_icon.png");

	private final List<SpelliverseTabToggleWidget> tabButtons = Lists.newArrayList();
	private SpelliverseTabToggleWidget selectedTab;

	public SpelliverseScreen(SpelliverseContainer container, PlayerInventory playerInventory, ITextComponent textComp) {
		super(container, playerInventory, textComp);
	}

	protected void init() {
		super.init();
		this.imageWidth = 256;
		this.imageHeight = 204;// 181;
		this.leftPos -= 40;
		this.topPos -= 23;
		this.menu.addSlotListener(this);
		minecraft.player.containerMenu = menu;
		initVisuals();
	}

	private void initVisuals() {
//		      String s = this.searchBox != null ? this.searchBox.getValue() : "";
//		      this.searchBox = new TextFieldWidget(this.minecraft.font, i + 25, j + 14, 80, 9 + 5, new TranslationTextComponent("itemGroup.search"));
//		      this.searchBox.setMaxLength(50);
//		      this.searchBox.setBordered(false);
//		      this.searchBox.setVisible(true);
//		      this.searchBox.setTextColor(16777215);
//		      this.searchBox.setValue(s);
		this.tabButtons.clear();

		// TODO
		for (SpelliverseTab spelliverseTab : SpelliverseTabToggleWidget.SpelliverseTab.values()) {
			this.tabButtons.add(new SpelliverseTabToggleWidget(spelliverseTab));
		}

		if (this.selectedTab != null) {
			this.selectedTab = this.tabButtons.stream().filter((p_209505_1_) -> {
				return p_209505_1_.getTab().equals(this.selectedTab.getTab());
			}).findFirst().orElse((SpelliverseTabToggleWidget) null);
		}

		if (this.selectedTab == null) {
			this.selectedTab = this.tabButtons.get(0);
		}

		this.selectedTab.setStateTriggered(true);
		this.updateCollections(false);
		this.updateTabs();
	}

	public void tick() {
		super.tick();
	}

	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(mStack);
		super.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		this.renderTooltip(mStack, p_230430_2_, p_230430_3_);

		mStack.pushPose();
		for (SpelliverseTabToggleWidget spellTab : this.tabButtons) {
			spellTab.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		}
		mStack.popPose();

	}

	@SuppressWarnings("deprecation")
	protected void renderBg(MatrixStack mStack, float p_230430_4_, int mouseX, int mouseY) {
		int i = this.leftPos;
		int j = this.topPos;
		this.minecraft.getTextureManager().bind(SPELLIVERSE_LOCATION);
		mStack.pushPose();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.blit(mStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		mStack.popPose();
	}

	public FontRenderer getFont() {
		return font;
	}

	@Override
	protected void renderLabels(MatrixStack mStack, int x, int y) {
		this.font.draw(mStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
	}

	private void updateCollections(boolean p_193003_1_) {
//		List<RecipeList> list = this.book.getCollection(this.selectedTab.getCategory());
//		list.forEach((p_193944_1_) -> {
//			p_193944_1_.canCraft(this.stackedContents, this.menu.getGridWidth(), this.menu.getGridHeight(), this.book);
//		});
//		List<RecipeList> list1 = Lists.newArrayList(list);
//		list1.removeIf((p_193952_0_) -> {
//			return !p_193952_0_.hasKnownRecipes();
//		});
//		list1.removeIf((p_193953_0_) -> {
//			return !p_193953_0_.hasFitting();
//		});
//		String s = this.searchBox.getValue();
//		if (!s.isEmpty()) {
//			ObjectSet<RecipeList> objectset = new ObjectLinkedOpenHashSet<>(this.minecraft
//					.getSearchTree(SearchTreeManager.RECIPE_COLLECTIONS).search(s.toLowerCase(Locale.ROOT)));
//			list1.removeIf((p_193947_1_) -> {
//				return !objectset.contains(p_193947_1_);
//			});
//		}
//
//		if (this.book.isFiltering(this.menu)) {
//			list1.removeIf((p_193958_0_) -> {
//				return !p_193958_0_.hasCraftable();
//			});
//		}
//
//		this.recipeBookPage.updateCollections(list1, p_193003_1_);
	}

	private void updateTabs() {
		int i = this.leftPos - 28;
		int j = (this.height - this.imageHeight) / 2 + 3;
//		int k = 27;
		int l = 0;

		for (SpelliverseTabToggleWidget spellTab : this.tabButtons) {
//			SpelliverseTab recipebookcategories = spellTab.getTab();
			spellTab.visible = true;
			spellTab.setPosition(i, j + 27 * l++);
		}
	}

	@Override
	protected boolean isHovering(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_,
			double p_195359_7_) {
		return super.isHovering(p_195359_1_, p_195359_2_, p_195359_3_, p_195359_4_, p_195359_5_, p_195359_7_);
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		for (SpelliverseTabToggleWidget spellTab : this.tabButtons) {
			if (spellTab.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
				if (this.selectedTab != spellTab) {
					this.selectedTab.setStateTriggered(false);
					this.selectedTab = spellTab;
					this.selectedTab.setStateTriggered(true);
					this.updateCollections(true);
				}
				return true;
			}
		}
		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
//		}
	}

	@Override
	protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_,
			int p_195361_7_) {
		return super.hasClickedOutside(p_195361_1_, p_195361_3_, p_195361_5_, p_195361_6_, p_195361_7_);
	}

	@Override
	protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
		super.slotClicked(slot, mouseX, mouseY, clickType);
	}

	public void removed() {
		if (this.minecraft.player != null && this.minecraft.player.inventory != null) {
			this.minecraft.player.inventoryMenu.removeSlotListener(this);
		}
		this.selectedTab = null;
		super.removed();
	}

	public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {
	}

	public void slotChanged(Container container, int slot, ItemStack item) {
	}

	public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
	}
}