package fr.emmuliette.rune.mod.tileEntityRenderer;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.tileEntity.IllusionTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IllusionTileEntityRenderer extends TileEntityRenderer<IllusionTileEntity> {

	public IllusionTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(IllusionTileEntity te, float partialTicks, MatrixStack mStack, IRenderTypeBuffer buffer,
			int light, int overlay) {
		if(te.getBlockId() == null)
			return;
		BlockState blockstate = Block.stateById(te.getBlockId());
		if (blockstate.getRenderShape() == BlockRenderType.MODEL) {
			World world = te.getLevel();
			if (blockstate != world.getBlockState(te.getBlockPos())
					&& blockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
				mStack.pushPose();
				BlockPos blockpos = te.getBlockPos();
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
				for (net.minecraft.client.renderer.RenderType type : net.minecraft.client.renderer.RenderType
						.chunkBufferLayers()) {
					if (RenderTypeLookup.canRenderInLayer(blockstate, type)) {
						net.minecraftforge.client.ForgeHooksClient.setRenderLayer(type);
						blockrendererdispatcher.getModelRenderer().tesselateBlock(world,
								blockrendererdispatcher.getBlockModel(blockstate), blockstate, blockpos, mStack,
								buffer.getBuffer(type), false, new Random(), (long) partialTicks,
								OverlayTexture.NO_OVERLAY);
					}
				}
				net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
				mStack.popPose();
			}
		}
	}

}
