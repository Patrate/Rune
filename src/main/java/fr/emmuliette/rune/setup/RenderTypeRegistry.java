package fr.emmuliette.rune.setup;

import fr.emmuliette.rune.mod.blocks.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderTypeRegistry {
	@SubscribeEvent
	public static void onRenderTypeSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			RenderTypeLookup.setRenderLayer(ModBlocks.PHASED_BLOCK.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.ANCHORED_BLOCK.get(), RenderType.cutout());
		});
	}
	
	public static void register() {}
}