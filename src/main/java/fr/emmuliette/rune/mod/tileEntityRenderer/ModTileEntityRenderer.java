package fr.emmuliette.rune.mod.tileEntityRenderer;

import fr.emmuliette.rune.mod.tileEntity.ModTileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModTileEntityRenderer {

	public static void register() {
		ClientRegistry.bindTileEntityRenderer(ModTileEntity.ILLUSION_TE.get(), IllusionTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntity.PHASED_TE.get(), PhasedTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntity.ANCHORED_TE.get(), AnchoredTileEntityRenderer::new);
	}
}
