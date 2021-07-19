package fr.emmuliette.rune.mod.circle;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorldReader;

public class Circle {
	public static final ResourceLocation IMAGE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/magic_circle.png");
	private static final RenderType CIRCLE_RENDER_TYPE = RenderType.entityCutout(IMAGE_LOCATION);

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
			renderer.getDispatcher().textureManager.bind(IMAGE_LOCATION);
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
			float partialTick, IWorldReader reader, float circleRadius) {
		if (entity instanceof MobEntity) {
			MobEntity mobentity = (MobEntity) entity;
			if (mobentity.isBaby()) {
				circleRadius = circleRadius * 0.5F;
			}
		}

		double x = MathHelper.lerp((double) partialTick, entity.xOld, entity.getX());
		double y = MathHelper.lerp((double) partialTick, entity.yOld, entity.getY());
		double z = MathHelper.lerp((double) partialTick, entity.zOld, entity.getZ());
		MatrixStack.Entry matrixstack$entry = mStack.last();
		IVertexBuilder ivertexbuilder = buffer.getBuffer(CIRCLE_RENDER_TYPE);

		BlockPos a = caster.blockPosition();
		for (BlockPos b : new BlockPos[] { a, a.north(), a.south() }) {
			for (BlockPos c : new BlockPos[] { b, b.east(), b.west() }) {
				renderBlockCircle(caster, c, matrixstack$entry, ivertexbuilder, reader, x, y, z, circleRadius, strength,
						partialTick);
			}
		}
	}

	private static void renderBlockCircle(Entity entity, BlockPos blockpos, MatrixStack.Entry mStack,
			IVertexBuilder builder, IWorldReader reader, double x, double y, double z, float radius, float strength,
			float partialTick) {
		VoxelShape voxelshape = VoxelShapes.block();
		strength = (float) Math.min(Math.max(0, strength), 1);
		AxisAlignedBB axisalignedbb = voxelshape.bounds();
		double xMin = (double) blockpos.getX() + axisalignedbb.minX;
		double xMax = (double) blockpos.getX() + axisalignedbb.maxX;
		double yMin = (double) entity.getY() + axisalignedbb.minY + 0.01;
		double zMin = (double) blockpos.getZ() + axisalignedbb.minZ;
		double zMax = (double) blockpos.getZ() + axisalignedbb.maxZ;
		float f1 = (float) (xMin - x) * 2f;
		float f2 = (float) (xMax - x);
		float f3 = (float) (yMin - y);
		float f4 = (float) (zMin - z) * 2f;
		float f5 = (float) (zMax - z);
		float f6 = -f1 / 2.0F / radius + 0.5F;
		float f7 = -f2 / 2.0F / radius + 0.5F;
		float f8 = -f4 / 2.0F / radius + 0.5F;
		float f9 = -f5 / 2.0F / radius + 0.5F;
		//circleVertex(mStack, builder, strength, f1, f3, f4, f6, f8);
		circleVertex(mStack, builder, strength, f1, f3, f4, f6, f8);
		// circleVertex(mStack, builder, strength, f1, f3, f5, f6, f9);
		// circleVertex(mStack, builder, strength, f2, f3, f5, f7, f9);
		// circleVertex(mStack, builder, strength, f2, f3, f4, f7, f8);
	}

	private static void circleVertex(MatrixStack.Entry mStack, IVertexBuilder builder, float alpha, float x, float y,
			float z, float w, float v) {
		builder.vertex(mStack.pose(), x, y, z).color(0F, 1F, 1F, alpha).uv(w, v).overlayCoords(1).uv2(15728880)
				.normal(mStack.normal(), 1.0F, 1.0F, 1.0F).endVertex();
	}

	// Same as isNameplateInRenderDistance in ForgeHooksClient
	private boolean isCircleInRenderDistance(LivingEntity entity, double squareDistance) {
		return (squareDistance <= 4096.0f);
	}
}