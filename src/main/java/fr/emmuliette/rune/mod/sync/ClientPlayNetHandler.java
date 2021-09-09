package fr.emmuliette.rune.mod.sync;

import fr.emmuliette.rune.mod.gui.grimoireItem.GrimoireItemScreen;
import fr.emmuliette.rune.mod.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPlayNetHandler {
	private static Minecraft minecraft = Minecraft.getInstance();

	public static void handleOpenGrimoire(Hand hand) {
		ItemStack itemstack = minecraft.player.getItemInHand(hand);
		if (itemstack.getItem() == ModItems.GRIMOIRE.get()) {
			minecraft.setScreen(new GrimoireItemScreen(hand, itemstack, minecraft.player));
		}

	}
}
