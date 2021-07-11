package fr.emmuliette.rune.setup;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.spells.capability.spell.SpellCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityHandler {
	public static final ResourceLocation CURRENCY_CAP = new ResourceLocation(RuneMain.MOD_ID, "currency");

	@SubscribeEvent
	  public void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event)
	  {
	      if(event.getObject() instanceof PlayerEntity)
	      	event.addCapability(CURRENCY_CAP, new SpellCapability());
	  }

}
