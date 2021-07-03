package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GrimoireScreen extends Screen {
	protected static final ResourceLocation GRIMOIRE_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
	private final List<SpellWidget> buttons = Lists.newArrayListWithCapacity(20);
	private final List<Spell> spellList;

	protected GrimoireScreen(ITextComponent title) {
		super(title);
		initialized = false;
		spellList = Lists.newArrayList(new Spell(new ItemStack(Blocks.DIRT, 1)),
				new Spell(new ItemStack(Blocks.DARK_OAK_WOOD, 1)), new Spell(new ItemStack(Blocks.IRON_BLOCK, 1)),
				new Spell(new ItemStack(Blocks.GOLD_BLOCK, 1)), new Spell(new ItemStack(Blocks.DIAMOND_BLOCK, 1)));
	}

	@Override
	protected void init() {
		for (Spell spell : spellList) {
			buttons.add(new SpellWidget(spell));
		}
		initialized = true;
		super.init();
	}

	@Override
	public void tick() {
		super.tick();
		// Appelé à chaque tick
	}

	@Override
	public void removed() {
	}

	@Override
	protected void insertText(String p_231155_1_, boolean p_231155_2_) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack matrixStack, int mouse_X, int mouse_Y, float partial_tick) {
		this.renderBackground(matrixStack);

		RenderSystem.pushMatrix();
		RenderSystem.translatef(0.0F, 0.0F, 100.0F);
		this.minecraft.getTextureManager().bind(GRIMOIRE_LOCATION);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		int a = 147, b = 166;
		int i = (this.width - a) / 2;
		int j = (this.height - b) / 2;
		this.blit(matrixStack, i, j, 1, 1, a, b);
		//drawString(matrixStack, this.minecraft.font, "CECI EST UN STRING DE TEST", i + 25, j + 14, -1);
		this.renderTooltip(matrixStack, mouse_X, mouse_Y);

		int k = 0,
			x0 = i + 12,
			y0 = j + 12;
		for (SpellWidget child : this.buttons) {
			drawString(matrixStack, this.minecraft.font, child.getCurrentSpell().getResultItem().getDisplayName(), x0 + 27, y0 + 8 + 28 * k, -1);
			
			child.setPosition(x0, y0 + 28 * k);
			child.render(matrixStack, mouse_X, mouse_Y, partial_tick);
			k++;
		}

		RenderSystem.popMatrix();
		super.render(matrixStack, mouse_X, mouse_Y, partial_tick);
	}

	@Override
	public void renderBackground(MatrixStack matrixStack) {
		super.renderBackground(matrixStack);
	}

	protected void renderTooltip(MatrixStack matrixStack, int mouse_X, int mouse_Y) {
		// TODO render tooltip
		/*
		 * if (this.minecraft.player.inventory.getCarried().isEmpty() &&
		 * this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
		 * this.renderTooltip(p_230459_1_, this.hoveredSlot.getItem(), p_230459_2_,
		 * p_230459_3_); }
		 */
	}

	// AJOUT DU TRUC A L'ECRAN
	// AJOUT DU TRUC A L'ECRAN
	// AJOUT DU TRUC A L'ECRAN

	private static final GrimoireScreen grimoireScreen = new GrimoireScreen(new StringTextComponent("TEST"));
	private static InventoryScreen gui = null;
	private boolean initialized;
	// private static final GrimoireScreen grimoiregui = new GrimoireScreen();
	private static final ResourceLocation GRIMOIRE_BUTTON_LOCATION = new ResourceLocation(
			"textures/gui/recipe_button.png");

	static public void internalAddGrimoireButtonToInventory(GuiScreenEvent event) {
		if (event.getGui() instanceof InventoryScreen) {
			System.err.println("A");
			gui = ((InventoryScreen) event.getGui());
			if (gui.getMinecraft().gameMode.hasInfiniteItems()) {
				return;
			}
			System.err.println("B");
			// if (!grimoiregui.initialized) {
			System.err.println("C");
			// grimoiregui.init(gui.width, gui.height, gui.getMinecraft(), gui.width < 379,
			// gui.getMenu());
			// grimoiregui.initialized = true;
			// }

			System.err.println("D");
			ImageButton grimoireButton = new ImageButton(gui.getGuiLeft() + 136, gui.height / 2 - 22, 20, 18, 0, 0, 19,
					GRIMOIRE_BUTTON_LOCATION, button -> {
						System.err.println("1");
						Minecraft.getInstance().setScreen(grimoireScreen);
						System.err.println("2");
						if (!grimoireScreen.initialized) {
							grimoireScreen.init();
						}
						System.err.println("3");
						// grimoiregui.tick();
					});
			if (event instanceof GuiScreenEvent.InitGuiEvent) {
				((GuiScreenEvent.InitGuiEvent) event).addWidget(grimoireButton);
			} else if (event instanceof GuiScreenEvent.InitGuiEvent.Pre) {
				((GuiScreenEvent.InitGuiEvent.Pre) event).addWidget(grimoireButton);
			}
		}
	}

	@SubscribeEvent
	static public void addGrimoireButtonToInventory(GuiScreenEvent.InitGuiEvent.Pre event) {
		internalAddGrimoireButtonToInventory(event);
	}

	@SubscribeEvent
	static public void addGrimoireButtonToInventory(GuiScreenEvent.InitGuiEvent event) {
		internalAddGrimoireButtonToInventory(event);
	}

}
