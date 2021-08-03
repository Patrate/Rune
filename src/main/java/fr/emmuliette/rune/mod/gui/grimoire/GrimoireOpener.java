package fr.emmuliette.rune.mod.gui.grimoire;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class GrimoireOpener {
	private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.grimoire");
	private static final ResourceLocation GRIMOIRE_BUTTON_LOCATION = new ResourceLocation(
			"textures/gui/recipe_button.png");
	private static final Widget button = new ImageButton(0, 0, 20, 18, 0, 0, 19, GRIMOIRE_BUTTON_LOCATION, (button) -> {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.setScreen(new GrimoireScreen(new GrimoireContainer(0, minecraft.player.inventory),
				minecraft.player.inventory, CONTAINER_TITLE));
	});

	@SubscribeEvent
	public static void openInventoryEvent(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof InventoryScreen) {
			InventoryScreen gui = (InventoryScreen) event.getGui();
			button.x = gui.getGuiLeft() + 128;
			button.y = gui.height / 2 - 22;
			button.visible = true;
			button.active = true;
			event.addWidget(button);
		} else {
			button.visible = false;
			button.active = false;
		}
	}

	@SubscribeEvent
	public static void clicButtonEvent(GuiScreenEvent.MouseClickedEvent.Pre event) {
		if (button.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void clicButtonEvent(GuiScreenEvent.MouseClickedEvent.Post event) {
		if (event.getGui() instanceof InventoryScreen) {
			if (button.visible)
				button.x = ((InventoryScreen) event.getGui()).getGuiLeft();
		}
	}
}
