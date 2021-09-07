package fr.emmuliette.rune.mod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellCapability;
import fr.emmuliette.rune.mod.items.spellItems.AbstractSpellItem;
import fr.emmuliette.rune.mod.packets.ISocket;
import fr.emmuliette.rune.mod.packets.SocketCapability;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class SpellItemDescription {

	@SubscribeEvent
	public static void customTooltip(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() instanceof AbstractSpellItem) {
			ISpell spell = event.getItemStack().getCapability(SpellCapability.SPELL_CAPABILITY).orElse(null);
			if (spell == null || spell.getSpell() == null)
				return;
			event.getToolTip().addAll(spell.getSpell().getTooltips());
		} else {
			ISocket socket = event.getItemStack().getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
			if (socket != null && socket.getSpell() != null) {
				event.getToolTip().addAll(socket.getSpell().getTooltips());
			}
		}
	}

	@SubscribeEvent
	public static void customTooltip(RenderTooltipEvent.PostText event) {
		if (event.isCanceled() || !(event.getStack().getItem() instanceof AbstractSpellItem))
			return;
		ISpell spell = event.getStack().getCapability(SpellCapability.SPELL_CAPABILITY).orElse(null);
		if (spell == null || spell.getSpell() == null)
			return;
		drawCost((int) spell.getSpell().getCost().getManaCost(), event.getMatrixStack(), event.getX(),
				event.getY() + 11);
	}

	private static void drawCost(int cost, MatrixStack mStack, int x, int y) {
		x += 6 * Math.ceil(Math.log10(cost + 1));
		mStack.pushPose();
		StarHelper.start();
		int i;
		for (i = 0; i < cost / 2; i++) {
			StarHelper.blit(mStack, x + 9 * i, y, StarHelper.Coord.BORDER_GREEN_FULL);
			StarHelper.blit(mStack, x + 9 * i, y, StarHelper.Coord.LIGHT_FULL);
		}
		if ((cost % 2) == 1) {
			StarHelper.blit(mStack, x + 9 * i, y, StarHelper.Coord.BORDER_GREEN_HLEFT);
			StarHelper.blit(mStack, x + 9 * i, y, StarHelper.Coord.LIGHT_HLEFT);
		}
		StarHelper.stop();
		mStack.popPose();
	}
}
