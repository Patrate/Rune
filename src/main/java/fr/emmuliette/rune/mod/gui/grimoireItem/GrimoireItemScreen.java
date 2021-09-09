package fr.emmuliette.rune.mod.gui.grimoireItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellCapability;
import fr.emmuliette.rune.mod.sync.SyncHandler;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireItemScreen extends Screen {
	public static final ResourceLocation BOOK_LOCATION = new ResourceLocation("textures/gui/book.png");

	public static final ITextComponent GUI_CANCEL = new TranslationTextComponent("gui.cancel");
	public static final ITextComponent GUI_LEARN = new TranslationTextComponent("gui.learn");
	public static final ITextComponent GUI_COST = new TranslationTextComponent("gui.cost");
//	private GrimoireItemScreen.IBookInfo bookAccess;
	private int currentPage;
	private List<IReorderingProcessor> cachedPageComponents = Collections.emptyList();
	private int cachedPage = -1;
	private ITextComponent pageMsg = StringTextComponent.EMPTY;
	private ChangePageButton forwardButton;
	private ChangePageButton backButton;
	private ItemStack grimoireItem;
	private PlayerEntity caster;
	private ICaster icaster;
	private ISpell spell;
	private Hand hand;

	private List<ITextProperties> pages;

	public GrimoireItemScreen(Hand hand, ItemStack grimoireItem, PlayerEntity caster) throws GrimoireItemScreenException {
		super(NarratorChatListener.NO_TITLE);
		this.hand = hand;
		this.grimoireItem = grimoireItem;
		this.caster = caster;
		this.spell = grimoireItem.getCapability(SpellCapability.SPELL_CAPABILITY).orElse((ISpell) null);
		this.icaster = caster.getCapability(CasterCapability.CASTER_CAPABILITY).orElse((ICaster) null);
		this.pages = new ArrayList<ITextProperties>();
		if (icaster == null || spell == null)
			throw new GrimoireItemScreenException();
		else {
			StringTextComponent current = new StringTextComponent(spell.getSpell().getName());
			current.append("\nCost " + (int) spell.getSpell().getCost().getManaCost() + " levels to learn");
			pages.add(current);
			for (ITextComponent a : spell.getSpell().getTooltips()) {
				pages.add(a);
			}
		}
	}

	private boolean learn() {
		if (caster.experienceLevel < getSpellCost())
			return false;
		SyncHandler.sendToServer(new CLearnSpellPacket(hand, getSpellCost()));
		if (icaster != null && spell != null) {
			icaster.getGrimoire().addSpell(spell);
			caster.experienceLevel -= getSpellCost();
			icaster.sync();
		}
		grimoireItem.shrink(1);
		return true;
	}

	private int getSpellCost() {
		return (int) Math.ceil(spell.getSpell().getCost().getManaCost());
	}

	public void setBookAccess() {
		this.currentPage = MathHelper.clamp(this.currentPage, 0, this.getPageCount());
		this.updateButtonVisibility();
		this.cachedPage = -1;
	}

	public boolean setPage(int p_214160_1_) {
		int i = MathHelper.clamp(p_214160_1_, 0, this.getPageCount() - 1);
		if (i != this.currentPage) {
			this.currentPage = i;
			this.updateButtonVisibility();
			this.cachedPage = -1;
			return true;
		} else {
			return false;
		}
	}

	protected int getPageCount() {
		return this.pages.size();
	}

	protected boolean forcePage(int p_214153_1_) {
		return this.setPage(p_214153_1_);
	}

	protected void init() {
		this.createMenuControls();
		this.createPageControlButtons();
	}

	private Button learnButton;

	protected void createMenuControls() {
		this.addButton(new Button(this.width / 2 - 100, 196, 98, 20, GUI_CANCEL, (p_214161_1_) -> {
			this.minecraft.setScreen((Screen) null);
		}));
		learnButton = this.addButton(new Button(this.width / 2 + 2, 196, 98, 20, GUI_LEARN, (p_214204_1_) -> {
			learn();
			this.minecraft.setScreen((Screen) null);
		}));
		this.learnButton.active = caster.experienceLevel >= getSpellCost();
	}

	protected void createPageControlButtons() {
		int i = (this.width - 192) / 2;
		this.forwardButton = this.addButton(new ChangePageButton(i + 116, 159, true, (p_214159_1_) -> {
			this.pageForward();
		}, true));
		this.backButton = this.addButton(new ChangePageButton(i + 43, 159, false, (p_214158_1_) -> {
			this.pageBack();
		}, true));
		this.updateButtonVisibility();
	}

	protected void pageBack() {
		if (this.currentPage > 0) {
			--this.currentPage;
		}

		this.updateButtonVisibility();
	}

	protected void pageForward() {
		if (this.currentPage < this.getPageCount() - 1) {
			++this.currentPage;
		}

		this.updateButtonVisibility();
	}

	private void updateButtonVisibility() {
		this.forwardButton.visible = this.currentPage < this.getPageCount() - 1;
		this.backButton.visible = this.currentPage > 0;
		this.learnButton.active = caster.experienceLevel >= getSpellCost();
	}

	public boolean keyPressed(int a, int b, int c) {
		if (super.keyPressed(a, b, c)) {
			return true;
		} else {
			switch (a) {
			case 266:
				this.backButton.onPress();
				return true;
			case 267:
				this.forwardButton.onPress();
				return true;
			default:
				return false;
			}
		}
	}

	private ITextProperties getPage(int page) {
		return pages.get(page);
	}

	@SuppressWarnings("deprecation")
	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(mStack);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BOOK_LOCATION);
		int i = (this.width - 192) / 2;
//		int j = 2;
		this.blit(mStack, i, 2, 0, 0, 192, 192);
		if (this.cachedPage != this.currentPage) {
			ITextProperties itextproperties = this.getPage(this.currentPage);
			this.cachedPageComponents = this.font.split(itextproperties, 114);
			this.pageMsg = new TranslationTextComponent("book.pageIndicator", this.currentPage + 1,
					Math.max(this.getPageCount(), 1));
		}

		this.cachedPage = this.currentPage;
		int i1 = this.font.width(this.pageMsg);
		this.font.draw(mStack, this.pageMsg, (float) (i - i1 + 192 - 44), 18.0F, 0);
		int k = Math.min(128 / 9, this.cachedPageComponents.size());

		for (int l = 0; l < k; ++l) {
			IReorderingProcessor ireorderingprocessor = this.cachedPageComponents.get(l);
			this.font.draw(mStack, ireorderingprocessor, (float) (i + 36), (float) (32 + l * 9), 0);
		}

		Style style = this.getClickedComponentStyleAt((double) p_230430_2_, (double) p_230430_3_);
		if (style != null) {
			this.renderComponentHoverEffect(mStack, style, p_230430_2_, p_230430_3_);
		}

		super.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		if (p_231044_5_ == 0) {
			Style style = this.getClickedComponentStyleAt(p_231044_1_, p_231044_3_);
			if (style != null && this.handleComponentClicked(style)) {
				return true;
			}
		}

		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}

	public boolean handleComponentClicked(Style p_230455_1_) {
		ClickEvent clickevent = p_230455_1_.getClickEvent();
		if (clickevent == null) {
			return false;
		} else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
			String s = clickevent.getValue();

			try {
				int i = Integer.parseInt(s) - 1;
				return this.forcePage(i);
			} catch (Exception exception) {
				return false;
			}
		} else {
			boolean flag = super.handleComponentClicked(p_230455_1_);
			if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
				this.minecraft.setScreen((Screen) null);
			}

			return flag;
		}
	}

	@Nullable
	public Style getClickedComponentStyleAt(double p_238805_1_, double p_238805_3_) {
		if (this.cachedPageComponents.isEmpty()) {
			return null;
		} else {
			int i = MathHelper.floor(p_238805_1_ - (double) ((this.width - 192) / 2) - 36.0D);
			int j = MathHelper.floor(p_238805_3_ - 2.0D - 30.0D);
			if (i >= 0 && j >= 0) {
				int k = Math.min(128 / 9, this.cachedPageComponents.size());
				if (i <= 114 && j < 9 * k + k) {
					int l = j / 9;
					if (l >= 0 && l < this.cachedPageComponents.size()) {
						IReorderingProcessor ireorderingprocessor = this.cachedPageComponents.get(l);
						return this.minecraft.font.getSplitter().componentStyleAtWidth(ireorderingprocessor, i);
					} else {
						return null;
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	public static List<String> convertPages(CompoundNBT p_214157_0_) {
		ListNBT listnbt = p_214157_0_.getList("pages", 8).copy();
		Builder<String> builder = ImmutableList.builder();

		for (int i = 0; i < listnbt.size(); ++i) {
			builder.add(listnbt.getString(i));
		}

		return builder.build();
	}
}