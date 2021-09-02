package fr.emmuliette.rune.mod.items;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.capabilities.socket.ISocket;
import fr.emmuliette.rune.mod.capabilities.socket.SocketCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE)
public class SocketItemEvent {

	@SubscribeEvent
	public static void onAttackEntityEvent(final AttackEntityEvent event) {
		if (event.isCanceled() || !(event.getTarget() instanceof LivingEntity))
			return;
		ItemStack tool = event.getPlayer().getMainHandItem();
		;
		if (tool.getItem() == Items.WOODEN_SWORD || tool.getItem() == Items.STONE_SWORD
				|| tool.getItem() == Items.IRON_SWORD || tool.getItem() == Items.GOLDEN_SWORD
				|| tool.getItem() == Items.DIAMOND_SWORD || tool.getItem() == Items.NETHERITE_SWORD) {
//		if (tool.getItem().is(ModTags.Items.SOCKETABLE)) {
			ISocket socketable = tool.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
			if (socketable != null) {
				if (socketable.getSpell() != null) {
					socketable.getSpell().castSocketItem(0f, // power TODO
							tool, // item
							(LivingEntity) event.getTarget(), // target
							event.getEntityLiving().level, // world
							event.getEntityLiving(), // caster
							null, // blockPos
							null, // context
							false, // channeling
							tool); // SocketItem
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
		if (event.isCanceled())
			return;
		ItemStack tool = event.getItemStack();
		if (tool.getItem() == Items.WOODEN_SWORD || tool.getItem() == Items.STONE_SWORD
				|| tool.getItem() == Items.IRON_SWORD || tool.getItem() == Items.GOLDEN_SWORD
				|| tool.getItem() == Items.DIAMOND_SWORD || tool.getItem() == Items.NETHERITE_SWORD) {
//		if (tool.getItem().is(ModTags.Items.SOCKETABLE)) {
			ISocket socketable = tool.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
			if (socketable != null) {
				if (socketable.getSpell() != null) {
					socketable.getSpell().castSocketItem(0f, // power TODO
							tool, // item
							null, // target
							event.getEntityLiving().level, // world
							event.getEntityLiving(), // caster
							event.getPos(), // blockPos
							null, // context
							false, // channeling
							tool); // SocketItem
				}
			}
		}
	}
}
