package fr.emmuliette.rune.mod.events;

import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.packets.ISocket;
import fr.emmuliette.rune.mod.packets.SocketCapability;
import fr.emmuliette.rune.mod.specialRecipes.SocketableRecipe;
import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
		ISocket socketable = tool.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
		if (socketable != null) {
			if (SocketableRecipe.isToolSocketable(tool.getItem())) {
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
	public static void onProjectileLoose(final ProjectileImpactEvent.Arrow event) {
		if (event.isCanceled())
			return;
		AbstractArrowEntity arrow = event.getArrow();
		ISocket socketable = arrow.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
		if (socketable != null) {
			if (socketable.getSpell() != null) {
				socketable.getSpell().castSocketItem(0f, // power TODO
						null, // item TODO maybe add the bow ?
						(event.getEntity() instanceof LivingEntity) ? (LivingEntity) event.getEntity() : null, // target
						arrow.level, // world
						null, // caster get throwwer TODO
						null, // blockPos
						null, // context
						false, // channeling
						null); // SocketItem get bow TODO
			}
		}
	}

	@SubscribeEvent
	public static void onDamage(final LivingHurtEvent event) {
		if (event.isCanceled())
			return;
		List<ItemStack> armorTools = getEquipedItems(event.getEntityLiving());
		for (ItemStack tool : armorTools) {
			ISocket socketable = tool.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
			if (socketable != null) {
				if (SocketableRecipe.isArmorSocketable(tool.getItem())) {
					if (socketable.getSpell() != null) {
						LivingEntity target = null;
						if (event.getSource() instanceof EntityDamageSource
								&& event.getSource().getEntity() instanceof LivingEntity) {
							target = (LivingEntity) event.getSource().getEntity();
						}
						socketable.getSpell().castSocketItem(0f, // power TODO
								tool, // item
								target, // target
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
	}

	@SubscribeEvent
	public static void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
		if (event.isCanceled())
			return;
		ItemStack tool = event.getItemStack();
		ISocket socketable = tool.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
		if (socketable != null) {
			if (SocketableRecipe.isToolSocketable(tool.getItem())) {
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

	private static List<ItemStack> getEquipedItems(LivingEntity entity) {
		List<ItemStack> retour = new ArrayList<ItemStack>();
		if (entity instanceof PlayerEntity) {
			IBaublesItemHandler baub = BaublesAPI.getBaublesHandler((PlayerEntity) entity).orElse(null);
			if (baub != null) {
				for (int i = 0; i < baub.getSlots(); i++) {
					ItemStack item = baub.getStackInSlot(i);
					if (item == ItemStack.EMPTY)
						continue;
					retour.add(item);
				}
			}
		}
		for (ItemStack item : entity.getAllSlots()) {
			if (item == ItemStack.EMPTY)
				continue;
			retour.add(item);
		}
		return retour;
	}
}
