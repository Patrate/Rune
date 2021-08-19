package fr.emmuliette.rune.mod.tileEntityRenderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.tileEntity.PhasedTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class PhasedTileEntityRenderer extends TileEntityRenderer<PhasedTileEntity> {

	public PhasedTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(PhasedTileEntity te, float partialTicks, MatrixStack mStack, IRenderTypeBuffer buffer, int light,
			int overlay) {
		// TODO: Render something on the side of a phased tunnel ? So we can know the block inside are phased
	}

}
