package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingContainer;
import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingRuneSlot;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.client.CUpdateRecipeBookStatusPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ComponentGui extends AbstractGui implements IRenderable, IGuiEventListener {
	public static final ResourceLocation COMPONENT_PAGE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/component_page.png");
	private int xOffset;
	private int width;
	private int height;
	protected SpellBindingContainer menu;
	protected Minecraft minecraft;
//	private TextFieldWidget titleBox;
	private ClientRecipeBook book;
	private final ComponentPage componentPage = new ComponentPage();
	private final RecipeItemHelper stackedContents = new RecipeItemHelper();
	private int timesInventoryChanged;

	public void init(int width, int height, Minecraft minecraft, boolean initVisuals, SpellBindingContainer menu) {
		this.minecraft = minecraft;
		this.width = width;
		this.height = height;
		this.menu = menu;
		minecraft.player.containerMenu = menu;
		this.componentPage.menu = menu;
		this.book = minecraft.player.getRecipeBook();
		this.timesInventoryChanged = minecraft.player.inventory.getTimesChanged();
		if (this.isVisible()) {
			this.initVisuals(initVisuals);
		}

		minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	public void initVisuals(boolean p_201518_1_) {
		this.xOffset = p_201518_1_ ? 0 : 86;
		int i = (this.width - 147) / 2 - this.xOffset;
		int j = (this.height - 166) / 2;
		this.stackedContents.clear();
		this.minecraft.player.inventory.fillStackedContents(this.stackedContents);
		this.menu.fillCraftSlotsStackedContents(this.stackedContents);
//		String s = this.titleBox != null ? this.titleBox.getValue() : "";
//		this.titleBox = new TextFieldWidget(this.minecraft.font, i + 13, j + 14, 86, 9 + 5,
//				// TODO mettre la traduction ici ! new
//				// TranslationTextComponent("itemGroup.search"));
//				new StringTextComponent("test"));
//		this.titleBox.setMaxLength(50);
//		this.titleBox.setBordered(true);
//		this.titleBox.setVisible(true);
//		this.titleBox.setTextColor(16777215);
//		this.titleBox.setValue(s);
		this.componentPage.init(this.minecraft, i, j);
		this.updateCollections(false);
	}

	@Override
	public boolean changeFocus(boolean focus) {
		return false;
	}

	public void removed() {
//		this.titleBox = null;
		this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}

	public int updateScreenPosition(boolean p_193011_1_, int p_193011_2_, int p_193011_3_) {
		int i;
		if (this.isVisible() && !p_193011_1_) {
			i = 177 + (p_193011_2_ - p_193011_3_ - 200) / 2;
		} else {
			i = (p_193011_2_ - p_193011_3_) / 2;
		}

		return i;
	}

	public boolean isVisible() {
		return true;
	}

	public void setVisible() {
		boolean p_193006_1_ = true;
		this.book.setOpen(this.menu.getRecipeBookType(), p_193006_1_);
		if (!p_193006_1_) {
			this.componentPage.setInvisible();
		}

		this.sendUpdateSettings();
	}

	public void slotClicked(@Nullable Slot slot) {
		updateSlotComponent(slot);
	}

	private SpellBindingRuneSlot currentSelectedSlot = null;

	public void updateSlotComponent(Slot slot) {
		if (slot instanceof SpellBindingRuneSlot && slot != null && slot.index > 1 && slot.index < this.menu.getSize()
				&& this.isVisible() && slot.getItem().getItem() instanceof RuneItem) {
			currentSelectedSlot = (SpellBindingRuneSlot) slot;
		}
		updateComponent();
	}

	public Grade getGrade(ItemStack item) {
		return Grade.STONE;
	}

	private void updateComponent() {
		AbstractSpellComponent component = null;
		Grade grade = Grade.UNKNOWN;
//		String componentName = null;
		if (getCurrentSelectedSlot() != null && getCurrentSelectedSlot().getItem().getItem() instanceof RuneItem) {
			component = getCurrentSelectedSlot().getComponent();
			grade = getGrade(getCurrentSelectedSlot().getItem());
//			componentName = getCurrentSelectedSlot().getItem().getItem().getName(getCurrentSelectedSlot().getItem()).getContents();
		}

		componentPage.setComponent(grade, component);
//		if (componentName == null) {
//			this.titleBox.setValue("");
//			this.titleBox.visible = false;
//		} else {
//			this.titleBox.setValue(componentName);
//			this.titleBox.visible = true;
//		}
	}

	private void updateCollections(boolean p_193003_1_) {
		List<RecipeList> list = this.book.getCollection(this.menu.getRecipeBookCategories().get(0));
		list.forEach((p_193944_1_) -> {
			p_193944_1_.canCraft(this.stackedContents, this.menu.getGridWidth(), this.menu.getGridHeight(), this.book);
		});
		List<RecipeList> list1 = Lists.newArrayList(list);
		list1.removeIf((p_193952_0_) -> {
			return !p_193952_0_.hasKnownRecipes();
		});
		list1.removeIf((p_193953_0_) -> {
			return !p_193953_0_.hasFitting();
		});
	}

	public void tick() {
		if (this.isVisible()) {
			if (this.timesInventoryChanged != this.minecraft.player.inventory.getTimesChanged()) {
				// this.updateStackedContents();
				this.timesInventoryChanged = this.minecraft.player.inventory.getTimesChanged();
			}
			if (getCurrentSelectedSlot() == null || !getCurrentSelectedSlot().hasItem()) {
				updateComponent();
			}

//			this.titleBox.tick();
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (this.isVisible()) {
			RenderSystem.pushMatrix();
			RenderSystem.translatef(0.0F, 0.0F, 100.0F);
			this.minecraft.getTextureManager().bind(COMPONENT_PAGE_LOCATION);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			int i = (this.width - 147) / 2 - this.xOffset;
			int j = (this.height - 166) / 2;
			this.blit(mStack, i, j, 1, 1, 147, 166);

//			this.titleBox.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);

			this.componentPage.render(mStack, i, j, p_230430_2_, p_230430_3_, p_230430_4_);
			RenderSystem.popMatrix();
		}
	}

	public void renderTooltip(MatrixStack mStack, int p_238924_2_, int p_238924_3_, int p_238924_4_, int p_238924_5_) {
		if (this.isVisible()) {
			this.componentPage.renderTooltip(mStack, p_238924_4_, p_238924_5_);
		}
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		if (this.isVisible() && !this.minecraft.player.isSpectator()) {
			if (this.componentPage.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_,
					(this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166)) {
//				IRecipe<?> irecipe = this.componentPage.getLastClickedRecipe();
//				RecipeList recipelist = this.componentPage.getLastClickedRecipeCollection();
//				if (irecipe != null && recipelist != null) {
//					if (!recipelist.isCraftable(irecipe)) {// TODO !!! && this.ghostRecipe.getRecipe() == irecipe) {
//						return false;
//					}

				/*
				 * this.ghostRecipe.clear();
				 * this.minecraft.gameMode.handlePlaceRecipe(this.minecraft.player.containerMenu
				 * .containerId, irecipe, Screen.hasShiftDown()); if
				 * (!this.isOffsetNextToMainGUI()) { this.setVisible(false); }
				 */
//				}

				return true;
//			} else if (this.searchBox.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
//				return true;
//			} else if (this.filterButton.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
				/*
				 * boolean flag = this.toggleFiltering();
				 * this.filterButton.setStateTriggered(flag); this.sendUpdateSettings();
				 * this.updateCollections(false);
				 */
//				return true;
			} else {
				/*
				 * for (RecipeTabToggleWidget recipetabtogglewidget : this.tabButtons) { if
				 * (recipetabtogglewidget.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
				 * if (this.selectedTab != recipetabtogglewidget) {
				 * this.selectedTab.setStateTriggered(false); this.selectedTab =
				 * recipetabtogglewidget; this.selectedTab.setStateTriggered(true);
				 * this.updateCollections(true); }
				 * 
				 * return true; } }
				 */

				return false;
			}
		} else {
			return false;
		}
	}

	/*
	 * @Override private boolean toggleFiltering() { RecipeBookCategory
	 * recipebookcategory = this.menu.getRecipeBookType(); boolean flag =
	 * !this.book.isFiltering(recipebookcategory);
	 * this.book.setFiltering(recipebookcategory, flag); return flag; }
	 */

	public boolean hasClickedOutside(double p_195604_1_, double p_195604_3_, int p_195604_5_, int p_195604_6_,
			int p_195604_7_, int p_195604_8_, int p_195604_9_) {
		if (!this.isVisible()) {
			return true;
		} else {
			boolean flag = p_195604_1_ < (double) p_195604_5_ || p_195604_3_ < (double) p_195604_6_
					|| p_195604_1_ >= (double) (p_195604_5_ + p_195604_7_)
					|| p_195604_3_ >= (double) (p_195604_6_ + p_195604_8_);
			boolean flag1 = (double) (p_195604_5_ - 147) < p_195604_1_ && p_195604_1_ < (double) p_195604_5_
					&& (double) p_195604_6_ < p_195604_3_ && p_195604_3_ < (double) (p_195604_6_ + p_195604_8_);
			return flag && !flag1;
		}
	}

	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		return IGuiEventListener.super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
//		this.ignoreTextInput = false;
//		if (this.isVisible() && !this.minecraft.player.isSpectator()) {		
//			  if (p_231046_1_ == 256 && !this.isOffsetNextToMainGUI()) {
//			  this.setVisible(false); return true; } else
//			 
//			if (this.searchBox.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
//				// this.checkSearchStringUpdate();
//				return true;
//			} else if (this.searchBox.isFocused() && this.searchBox.isVisible() && p_231046_1_ != 256) {
//				return true;
//			} else if (this.minecraft.options.keyChat.matches(p_231046_1_, p_231046_2_)
//					&& !this.searchBox.isFocused()) {
//				this.ignoreTextInput = true;
//				this.searchBox.setFocus(true);
//				return true;
//			} else {
//				return false;
//			}
//		} else {
//			return false;
//		}
	}

	@Override
	public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
//		this.ignoreTextInput = false;
		return IGuiEventListener.super.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_);
	}

	@Override
	public boolean charTyped(char p_231042_1_, int p_231042_2_) {
		return IGuiEventListener.super.charTyped(p_231042_1_, p_231042_2_);
//		if (this.ignoreTextInput) {
//			return false;
//		} else if (this.isVisible() && !this.minecraft.player.isSpectator()) {
//			if (this.searchBox.charTyped(p_231042_1_, p_231042_2_)) {
//				// this.checkSearchStringUpdate();
//				return true;
//			} else {
//				return IGuiEventListener.super.charTyped(p_231042_1_, p_231042_2_);
//			}
//		} else {
//			return false;
//		}
	}

	@Override
	public boolean isMouseOver(double p_231047_1_, double p_231047_3_) {
		return IGuiEventListener.super.isMouseOver(p_231047_1_, p_231047_3_);
	}

	/*
	 * @Override private void checkSearchStringUpdate() { String s =
	 * this.searchBox.getValue().toLowerCase(Locale.ROOT);
	 * this.pirateSpeechForThePeople(s); if (!s.equals(this.lastSearch)) {
	 * this.updateCollections(false); this.lastSearch = s; }
	 * 
	 * }
	 */

	/*
	 * @Override private void pirateSpeechForThePeople(String p_193716_1_) { if
	 * ("excitedze".equals(p_193716_1_)) { LanguageManager languagemanager =
	 * this.minecraft.getLanguageManager(); Language language =
	 * languagemanager.getLanguage("en_pt"); if
	 * (languagemanager.getSelected().compareTo(language) == 0) { return; }
	 * 
	 * languagemanager.setSelected(language); this.minecraft.options.languageCode =
	 * language.getCode();
	 * net.minecraftforge.client.ForgeHooksClient.refreshResources(this.minecraft,
	 * net.minecraftforge.resource.VanillaResourceType.LANGUAGES);
	 * this.minecraft.options.save(); }
	 * 
	 * }
	 */

//   @Override
//   private boolean isOffsetNextToMainGUI() {
//      return this.xOffset == 86;
//   }

//   @Override
//   public void recipesUpdated() {
//      this.updateTabs();
//      if (this.isVisible()) {
//         this.updateCollections(false);
//      }
//
//   }

//   @Override
//   public void recipesShown(List<IRecipe<?>> p_193001_1_) {
//      for(IRecipe<?> irecipe : p_193001_1_) {
//         this.minecraft.player.removeRecipeHighlight(irecipe);
//      }
//
//   }

//   @Override
//   public void setupGhostRecipe(IRecipe<?> p_193951_1_, List<Slot> p_193951_2_) {
//      ItemStack itemstack = p_193951_1_.getResultItem();
//      this.ghostRecipe.setRecipe(p_193951_1_);
//      this.ghostRecipe.addIngredient(Ingredient.of(itemstack), (p_193951_2_.get(0)).x, (p_193951_2_.get(0)).y);
//      this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), p_193951_1_, p_193951_1_.getIngredients().iterator(), 0);
//   }

//   @Override
//   public void addItemToSlot(Iterator<Ingredient> p_201500_1_, int p_201500_2_, int p_201500_3_, int p_201500_4_, int p_201500_5_) {
//      Ingredient ingredient = p_201500_1_.next();
//      if (!ingredient.isEmpty()) {
//         Slot slot = this.menu.slots.get(p_201500_2_);
//         this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
//      }
//
//   }

	protected void sendUpdateSettings() {
		if (this.minecraft.getConnection() != null) {
			RecipeBookCategory recipebookcategory = this.menu.getRecipeBookType();
			boolean flag = this.book.getBookSettings().isOpen(recipebookcategory);
			boolean flag1 = this.book.getBookSettings().isFiltering(recipebookcategory);
			this.minecraft.getConnection().send(new CUpdateRecipeBookStatusPacket(recipebookcategory, flag, flag1));
		}

	}

	public SpellBindingRuneSlot getCurrentSelectedSlot() {
		return currentSelectedSlot;
	}
}
