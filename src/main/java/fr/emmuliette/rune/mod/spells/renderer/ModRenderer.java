package fr.emmuliette.rune.mod.spells.renderer;

import fr.emmuliette.rune.mod.spells.entities.ModEntities;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ModRenderer {

	public static void register() {
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.MAGIC_ENTITY.get(), MagicEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.CIRCLE_ENTITY.get(), CircleEntityRenderer::new);
	}
}
