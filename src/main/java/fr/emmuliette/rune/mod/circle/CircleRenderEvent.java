package fr.emmuliette.rune.mod.circle;

import java.util.HashMap;
import java.util.Map;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class CircleRenderEvent {
	private static final Map<LivingEntity, Circle> circleMap = new HashMap<LivingEntity, Circle>();
	private static final float shadowradius = 0.5f;

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void renderCircle(RenderLivingEvent<?, ?> event) {
		if (event.getEntity() != Minecraft.getInstance().player) {
			return;
		}
		if (!circleMap.containsKey(event.getEntity())) {
			// return;
			circleMap.put(event.getEntity(), new Circle(event.getEntity()));
		}
		if (!event.getEntity().isAlive()) {
			circleMap.remove(event.getEntity());
			return;
		}
		Circle circle = circleMap.get(event.getEntity());
		//circle.renderTag(event.getRenderer(), event.getMatrixStack(), event.getBuffers());
		// circle.renderCape(event.getRenderer(), event.getMatrixStack(),
		// event.getBuffers());
		circle.renderShadow(event.getMatrixStack(), event.getBuffers(), event.getEntity(), 1f, event.getPartialRenderTick(),
				event.getEntity().level, shadowradius);
	}
}
