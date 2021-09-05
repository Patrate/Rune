package fr.emmuliette.rune.mod.gui.playerGui;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.items.magicItems.WandItem;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.setup.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class GuiWandSpell {
	@SubscribeEvent
	public static void renderWandSpell(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.ARMOR) {
			return;
		}
		Minecraft minecraft = Minecraft.getInstance();
		PlayerEntity player = (PlayerEntity) minecraft.getCameraEntity();
		if (player.isAlive()) {
			Hand hand = null;
			if (player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof WandItem)
				hand = Hand.MAIN_HAND;
			else if (player.getItemInHand(Hand.OFF_HAND).getItem() instanceof WandItem)
				hand = Hand.OFF_HAND;
			if (hand != null) {
				ItemStack wand = player.getItemInHand(hand);
				Spell spell = WandItem.getCurrentSpell(wand, player);
				if (spell != null) {
					render(event, minecraft, player, spell);
				}
			}
		}
	}

	private static void render(RenderGameOverlayEvent.Post event, Minecraft minecraft, PlayerEntity player,
			Spell spell) {
		MatrixStack mStack = event.getMatrixStack();
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();

		String spellName = spell.getName();
		int left = screenWidth / 2 - 91;
		int top = screenHeight - ForgeIngameGui.left_height;
		List<? extends Integer> configPos = Configuration.Client.wandSpellPosition;
		if (configPos != null && configPos.size() == 2) {
			left = configPos.get(0);
			top = configPos.get(1);
		}
		
		mStack.pushPose();
		drawString(mStack, minecraft.font, new StringTextComponent(spellName), left + 1, top + 2, Color.BLUE.getRGB());
		drawString(mStack, minecraft.font, new StringTextComponent(spellName), left, top + 1, Color.WHITE.getRGB());
		mStack.popPose();
	}

	private static void drawString(MatrixStack mStack, FontRenderer font, ITextComponent textCmp, int x, int y,
			int rgbColor) {
		ForgeIngameGui.drawString(mStack, font, textCmp, x, y, rgbColor);
	}
}