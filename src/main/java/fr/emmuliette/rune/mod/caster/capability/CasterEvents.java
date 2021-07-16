package fr.emmuliette.rune.mod.caster.capability;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE)
public class CasterEvents {
	private static final int NATURAL_MANA_REGEN = 80;//, BOOSTED_MANA_REGEN = 10;
	@SubscribeEvent
	public static void regenMana(PlayerTickEvent event) {
		if(event.phase != Phase.START) {
			return;
		}
		if(!event.side.isClient()) {
			try {
				ICaster cap = event.player.getCapability(CasterCapability.CASTER_CAPABILITY).orElseThrow(new CasterCapabilityExceptionSupplier(event.player));
				cap.tickCooldown();
				cap.setManaCooldown(cap.getManaRegenTick() + 1);
				if(cap.getManaRegenTick() >= NATURAL_MANA_REGEN) {
					cap.addMana(1);
					cap.setManaCooldown(0);
				}
			} catch (CasterCapabilityException e) {
				e.printStackTrace();
			}
		}
	}
}
