package fr.emmuliette.rune.mod.spells.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.spells.entities.MagicEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MagicEntityRenderer<T extends MagicEntity> extends EntityRenderer<T> {
	private static final ResourceLocation BOAT_TEXTURE_LOCATIONS = new ResourceLocation("textures/entity/boat/oak.png");
	protected static final ResourceLocation IMAGE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/magic_circle.png");
	// private static final ModelResourceLocation FRAME_LOCATION = new
	// ModelResourceLocation("item_frame", "map=false");

	protected final EntityModel<T> model = new CreeperModel<>();

	public MagicEntityRenderer(EntityRendererManager erm) {
		super(erm);
		this.shadowRadius = 0.7f;
	}

	public ResourceLocation getTextureLocation(MagicEntity entity) {
		return BOAT_TEXTURE_LOCATIONS;
		// return IMAGE_LOCATION;
	}

	@Override
	public void render(T entity, float p_225623_2_, float p_225623_3_, MatrixStack mStack, IRenderTypeBuffer buffer,
			int partialTick) {
		super.render(entity, p_225623_2_, p_225623_3_, mStack, buffer, partialTick);
		entity.render();
		// boat renderer !

		mStack.pushPose();
		mStack.translate(0.0D, 0.375D, 0.0D);
		mStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - p_225623_2_));

		mStack.scale(-1.0F, -1.0F, 1.0F);
		mStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
		IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		this.model.renderToBuffer(mStack, ivertexbuilder, partialTick, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
				1.0F);

		mStack.popPose();

		// Frame renderer !
		/*
		 * BlockRendererDispatcher blockrendererdispatcher =
		 * Minecraft.getInstance().getBlockRenderer(); ModelManager modelmanager =
		 * blockrendererdispatcher.getBlockModelShaper().getModelManager();
		 * ModelResourceLocation modelresourcelocation = FRAME_LOCATION;
		 * mStack.pushPose(); mStack.translate(0, -0.5D, 0); mStack.mulPose(new
		 * Quaternion(0.7071f, 0, 0, 0.7071f));
		 * blockrendererdispatcher.getModelRenderer().renderModel(mStack.last(),
		 * buffer.getBuffer(Atlases.solidBlockSheet()), (BlockState) null,
		 * modelmanager.getModel(modelresourcelocation), 1.0F, 1.0F, 1.0F, partialTick,
		 * OverlayTexture.NO_OVERLAY); mStack.popPose(); super.render(entity,
		 * p_225623_2_, p_225623_3_, mStack, buffer, partialTick);
		 */
	}
}