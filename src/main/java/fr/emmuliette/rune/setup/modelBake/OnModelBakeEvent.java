package fr.emmuliette.rune.setup.modelBake;

import org.apache.logging.log4j.Logger;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class OnModelBakeEvent {
	private static final Logger LOGGER = RuneMain.LOGGER;

	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		LOGGER.info("ModelBakeEvent");
		ModelResourceLocation itemModelResourceLocation = RuneModel.modelResourceLocation;
		IBakedModel existingModel = event.getModelRegistry().get(itemModelResourceLocation);
		if (existingModel == null) {
			LOGGER.warn("Did not find the expected vanilla baked model for RuneModel in registry");
		} else if (existingModel instanceof RuneModel) {
			LOGGER.warn("Tried to replace RuneModel twice");
		} else {
			LOGGER.info("Custom model loaded");
			RuneModel customModel = new RuneModel(existingModel);
			event.getModelRegistry().put(itemModelResourceLocation, customModel);
		}
	}
}
