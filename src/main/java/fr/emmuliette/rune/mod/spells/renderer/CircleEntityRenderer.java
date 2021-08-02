package fr.emmuliette.rune.mod.spells.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.circle.CircleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BoatModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CircleEntityRenderer extends EntityRenderer<CircleEntity> {
	private static final ResourceLocation BOAT_TEXTURE_LOCATIONS = new ResourceLocation("textures/entity/boat/oak.png");
	protected static final ResourceLocation IMAGE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/magic_circle.png");
	private static final ModelResourceLocation FRAME_LOCATION = new ModelResourceLocation("item_frame", "map=false");
	protected final BoatModel model = new BoatModel();

	public CircleEntityRenderer(EntityRendererManager erm) {
		super(erm);
	}

	public ResourceLocation getTextureLocation(CircleEntity entity) {
		return BOAT_TEXTURE_LOCATIONS;
		// return IMAGE_LOCATION;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(CircleEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack mStack,
			IRenderTypeBuffer buffer, int partialTick) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
		ModelManager modelmanager = blockrendererdispatcher.getBlockModelShaper().getModelManager();
		ModelResourceLocation modelresourcelocation = FRAME_LOCATION;
		mStack.pushPose();
		//mStack.translate(0, -0.5D, 0);
		mStack.mulPose(new Quaternion(0.7071f, 0, 0, 0.7071f));
		blockrendererdispatcher.getModelRenderer().renderModel(mStack.last(),
				buffer.getBuffer(Atlases.solidBlockSheet()), (BlockState) null,
				modelmanager.getModel(modelresourcelocation), 1.0F, 1.0F, 1.0F, partialTick, OverlayTexture.NO_OVERLAY);
		mStack.popPose();
		super.render(entity, p_225623_2_, p_225623_3_, mStack, buffer, partialTick);

	}
}