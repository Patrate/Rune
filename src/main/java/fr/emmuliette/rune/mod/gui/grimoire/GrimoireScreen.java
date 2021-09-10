package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.gui.grimoire.spellPage.GrimoireSpellPage;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireScreen extends ContainerScreen<GrimoireContainer> implements IContainerListener {
	public static final ResourceLocation GRIMOIRE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/grimoire.png");

	private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation(
			"textures/gui/container/creative_inventory/tabs.png");

	private boolean widthTooNarrow;
	private Grimoire grimoire;
	ISpell selectedSpell;
	private List<SpellWidget> spellWidgets;
	GrimoireSpellPage spellPage = new GrimoireSpellPage();

	private float scrollOffs;
	private boolean scrolling;

	public GrimoireScreen(GrimoireContainer container, PlayerInventory playerInventory, ITextComponent textComp) {
		super(container, playerInventory, textComp);
		spellWidgets = new ArrayList<SpellWidget>(VIEW_SIZE);
	}

	public int getSpellCount() {
		return grimoire.grimoireSize();
	}

	public ISpell getSelectedSpell() {
		return selectedSpell;
	}

	public Grimoire getGrimoire() {
		return grimoire;
	}

	void selectSpell(int spellId) {
		selectedSpell = grimoire.getSpell(spellId);
		this.leftPos = this.spellPage.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
	}

	protected void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		this.imageWidth = 176;
		this.imageHeight = 181;
		initSpellPage();
		this.menu.addSlotListener(this);
		this.setInitialFocus(this.spellPage);
		this.scrollOffs = 0.0F;
		this.scrollTo(0.0F);
		minecraft.player.containerMenu = menu;
	}

	private void initSpellPage() {
		this.spellPage.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this);
		this.leftPos = this.spellPage.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
		this.children.add(this.spellPage);
		this.spellPage.initVisuals(this.widthTooNarrow);
	}

	public void tick() {
		super.tick();
	}

	@Override
	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(mStack);
		super.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		if (selectedSpell != null) {
			spellPage.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		}
		this.renderTooltip(mStack, p_230430_2_, p_230430_3_);

	}

	@SuppressWarnings("deprecation")
	protected void renderBg(MatrixStack mStack, float p_230430_4_, int mouseX, int mouseY) {
		int i = this.leftPos;
		int j = this.topPos;
		this.minecraft.getTextureManager().bind(GRIMOIRE_LOCATION);
		mStack.pushPose();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.blit(mStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

		if (canScroll()) {
			this.minecraft.getTextureManager().bind(CREATIVE_INVENTORY_TABS);
			i = this.leftPos + 175 - 18;
			j = this.topPos + 18 - 11;
			int k = j + 112 - 29;
			this.blit(mStack, i, j + (int) ((float) (k - j - 17) * this.scrollOffs), 232 + (this.canScroll() ? 0 : 12),
					0, 12, 15);
		}
		mStack.popPose();

	}

	protected void renderSpellPage(MatrixStack mStack, int mouseX, int mouseY, float useless) {
		if (getSelectedSpell() == null)
			return;
		this.font.draw(mStack, getSelectedSpell().getSpell().getName(), this.width / 2, 8, 4210752);
	}

	public FontRenderer getFont() {
		return font;
	}

	@Override
	protected void renderLabels(MatrixStack mStack, int x, int y) {
		this.font.draw(mStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
	}

	@Override
	protected boolean isHovering(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_,
			double p_195359_7_) {
		return super.isHovering(p_195359_1_, p_195359_2_, p_195359_3_, p_195359_4_, p_195359_5_, p_195359_7_);
	}

	@Override
	public boolean mouseClicked(double x, double y, int p_231044_5_) {
		if (this.insideScrollbar(x, y)) {
			this.scrolling = this.canScroll();
			return true;
		}
//		if (this.grimoireGui.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
//			this.setFocused(this.grimoireGui);
//			return true;
//		} else {
		return super.mouseClicked(x, y, p_231044_5_);
//		}
	}

	public boolean mouseReleased(double x, double y, int mouseButton) {
		if (mouseButton == 0) {
			this.scrolling = false;
		}
		return super.mouseReleased(x, y, mouseButton);
	}

	public boolean mouseScrolled(double x, double y, double z) {
		if (!this.canScroll()) {
			return false;
		} else {
			int i = (this.getSpellCount() + 9 - 1) / 9 - 5;
			this.scrollOffs = (float) ((double) this.scrollOffs + z / (double) i);
			this.scrollOffs = MathHelper.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.scrollTo(this.scrollOffs);
			return true;
		}
	}

	public boolean mouseDragged(double a, double b, int c, double d, double e) {
		if (this.scrolling) {
			int i = this.topPos + 18;
			int j = i + 112;
			this.scrollOffs = ((float) b - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
			this.scrollOffs = MathHelper.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.scrollTo(this.scrollOffs);
			return true;
		} else {
			return super.mouseDragged(a, b, c, d, e);
		}
	}

	protected boolean insideScrollbar(double x, double y) {
		int i = this.leftPos;
		int j = this.topPos;
		int k = i + 175;
		int l = j + 18;
		int i1 = k + 14;
		int j1 = l + 112;
		return x >= (double) k && y >= (double) l && x < (double) i1 && y < (double) j1;
	}

	public void scrollTo(float dest) {
		int i = this.getSpellCount() - VIEW_SIZE;
		int j = (int) ((double) (dest * (float) i) + 0.5D);
		if (j < 0) {
			j = 0;
		}

		for (int k = 0; k < VIEW_SIZE; k++) {
			this.spellWidgets.get(k).setSpellId(j + k);
		}
	}

	private boolean canScroll() {
		return this.getSpellCount() > VIEW_SIZE;
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
		super.removed();
	}

	public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {
		this.buttons.clear();
		spellWidgets.clear();
		try {
			ICaster cap = this.inventory.player.getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(this.inventory.player));
			grimoire = cap.getGrimoire();

			for (int i = 0; i < VIEW_SIZE; i++) {
				SpellWidget w = new SpellWidget(this, grimoire, (i < getSpellCount()) ? i : -1, this.leftPos + 16,
						this.topPos + 16 + 16 * i);
				this.addButton(w);
				spellWidgets.add(w);
			}

		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}
	}

	private static final int VIEW_SIZE = 4;

	public void slotChanged(Container container, int slot, ItemStack item) {
	}

	public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
	}
}