package fr.emmuliette.rune.mod.circle;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapeCube;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorldReader;

public class Circle {
	private static final RenderType SHADOW_RENDER_TYPE = RenderType
			.entityShadow(new ResourceLocation("textures/misc/shadow.png"));
	public static final ResourceLocation IMAGE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/icons.png");

	private LivingEntity caster;
	private float rotation;

	public Circle(LivingEntity caster) {
		this.caster = caster;
		rotation = 0f;
	}

	@SuppressWarnings("resource")
	public void renderTag(LivingRenderer<?, ?> renderer, MatrixStack mStack, IRenderTypeBuffer buffer) {
		rotation = (rotation + 1) % 360;
		ITextComponent text = new StringTextComponent("WHOLOLOLOLO");
		int colorIntSomehow = 0;

		double d0 = renderer.getDispatcher().distanceToSqr(caster);
		if (isCircleInRenderDistance(caster, d0)) {
			float f = 0.01f;
			mStack.pushPose();
			mStack.translate(0.0D, (double) f, 0.0D);
			mStack.mulPose(new Quaternion(90, 0, rotation, true));
			// mStack.mulPose(new Quaternion(0, 0, rotation, true));
			mStack.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = mStack.last().pose();

			float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
			int j = (int) (f1 * 255.0F) << 24;

			// TODO wip
			FontRenderer fontrenderer = renderer.getFont();
			float f2 = (float) (-fontrenderer.width(text) / 2); // Centering I guess ?
			fontrenderer.drawInBatch(text, f2, 0f, 553648127, false, matrix4f, buffer, false, j, colorIntSomehow);

			mStack.popPose();
		}
	}

	@SuppressWarnings("resource")
	public void renderCape(LivingRenderer<?, ?> renderer, MatrixStack mStack, IRenderTypeBuffer buffer) {

		double d0 = renderer.getDispatcher().distanceToSqr(caster);
		if (isCircleInRenderDistance(caster, d0)) {
			float f = (float) caster.getY(); // Height I guess, position of the tag
			int i = 0; // deadmaus wtf jsp ?
			mStack.pushPose();
			// mStack.mulPose(Vector3f.ZP.rotationDegrees(180));
			mStack.translate(0.0D, (double) f, 0.0D);
			float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
			int j = (int) (f1 * 255.0F) << 24;

			IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.entitySolid(IMAGE_LOCATION));

			mStack.popPose();
		}
	}

	public void renderShadow(MatrixStack mStack, IRenderTypeBuffer buffer, Entity entity, float strength,
			float partialTick, IWorldReader reader, float shadowRadius) {
		if (entity instanceof MobEntity) {
			MobEntity mobentity = (MobEntity) entity;
			if (mobentity.isBaby()) {
				shadowRadius = shadowRadius * 0.5F;
			}
		}

		double d2 = MathHelper.lerp((double) partialTick, entity.xOld, entity.getX());
		double d0 = MathHelper.lerp((double) partialTick, entity.yOld, entity.getY());
		double d1 = MathHelper.lerp((double) partialTick, entity.zOld, entity.getZ());
		int i = MathHelper.floor(d2 - (double) shadowRadius);
		int j = MathHelper.floor(d2 + (double) shadowRadius);
		int k = MathHelper.floor(d0 - (double) shadowRadius);
		int l = MathHelper.floor(d0);
		int i1 = MathHelper.floor(d1 - (double) shadowRadius);
		int j1 = MathHelper.floor(d1 + (double) shadowRadius);
		MatrixStack.Entry matrixstack$entry = mStack.last();
		IVertexBuilder ivertexbuilder = buffer.getBuffer(SHADOW_RENDER_TYPE);

		// for (BlockPos blockpos : BlockPos.betweenClosed(new BlockPos(i, k, i1), new
		// BlockPos(j, l, j1))) {
		renderBlockShadow(caster, matrixstack$entry, ivertexbuilder, reader, d2, d0, d1, shadowRadius, strength,
				partialTick);
		// }

		// renderBlockShadow(caster, matrixstack$entry, ivertexbuilder, reader, d2, d0,
		// d1, f, strength);

	}

	private static void renderBlockShadow(Entity entity, MatrixStack.Entry mStack, IVertexBuilder builder,
			IWorldReader reader, double x, double y, double z, float radius, float strength, float partialTick) {
		// if (blockstate.getRenderShape() != BlockRenderType.INVISIBLE &&
		// reader.getMaxLocalRawBrightness(blockPos) > 3) {
		// if (blockstate.isCollisionShapeFullBlock(reader, blockpos)) {
		// VoxelShape voxelshape = VoxelShapes.create(entity.getBoundingBox());
		VoxelShape voxelshape = VoxelShapes.block();
		// float f = (float) (((double) strength - (y - (double) blockPos.getY()) /
		// 2.0D) * 0.5D * (double) reader.getBrightness(blockPos));
		strength = (float) Math.min(Math.max(0, strength), 1);
		AxisAlignedBB axisalignedbb = voxelshape.bounds();
		double xMin = (double) entity.getX() - axisalignedbb.maxX;
		double xMax = (double) entity.getX() + axisalignedbb.maxX;
		double yMin = (double) entity.getY() + axisalignedbb.minY;
		double zMin = (double) entity.getZ() - axisalignedbb.maxZ;
		double zMax = (double) entity.getZ() + axisalignedbb.maxZ;
		float f1 = (float) (xMin - x);
		float f2 = (float) (xMax - x);
		float f3 = (float) (yMin - y);
		float f4 = (float) (zMin - z);
		float f5 = (float) (zMax - z);
		float f6 = -f1 / 2.0F / radius + 0.5F;
		float f7 = -f2 / 2.0F / radius + 0.5F;
		float f8 = -f4 / 2.0F / radius + 0.5F;
		float f9 = -f5 / 2.0F / radius + 0.5F;
		shadowVertex(mStack, builder, strength, f1, f3, f4, f6, f8);
		shadowVertex(mStack, builder, strength, f1, f3, f5, f6, f9);
		shadowVertex(mStack, builder, strength, f2, f3, f5, f7, f9);
		shadowVertex(mStack, builder, strength, f2, f3, f4, f7, f8);
	}

	private static void shadowVertex(MatrixStack.Entry mStack, IVertexBuilder builder, float alpha, float x, float y,
			float z, float w, float v) {
		builder.vertex(mStack.pose(), x, y, z).color(.0F, .0F, .0F, alpha).uv(w, v)
				.overlayCoords(OverlayTexture.pack(1, 10)).uv2(15728880).normal(mStack.normal(), 1.0F, 1.0F, 1.0F)
				.endVertex();
	}

	// Same as isNameplateInRenderDistance in ForgeHooksClient
	private boolean isCircleInRenderDistance(LivingEntity entity, double squareDistance) {
		return (squareDistance <= 4096.0f);
	}
}