package fr.emmuliette.rune.mod.gui.handItem;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.NotAnItemException;
import fr.emmuliette.rune.mod.ModObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class Parchment {
	private static final RenderType MAP_BACKGROUND = RenderType
			.text(new ResourceLocation("textures/map/map_background.png"));
//	private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType
//			.text(new ResourceLocation("textures/map/map_background_checkerboard.png"));

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void renderHandParchment(RenderHandEvent event) {
		try {
			ItemStack is = event.getItemStack();
			if (is.getItem() != ModObjects.PARCHMENT.getModItem() && is.getItem() != ModObjects.SPELL.getModItem()) {
				return;
			}
			renderArmWithItem(Minecraft.getInstance().player, event.getHand(), event.getItemStack(),
					event.getMatrixStack(), event.getBuffers(), 0, 0, event.getLight());
			event.setCanceled(true);
		} catch (NotAnItemException e) {
			e.printStackTrace();
		}
	}

	private static void renderArmWithItem(AbstractClientPlayerEntity player, Hand hand, ItemStack itemStack,
			MatrixStack mStack, IRenderTypeBuffer renderBuffer, float prems, float dems, int light)
			throws NotAnItemException {
		boolean flag = hand == Hand.MAIN_HAND;
		HandSide handside = flag ? player.getMainArm() : player.getMainArm().getOpposite();
		mStack.pushPose();
		if (!itemStack.isEmpty()) {
			if (itemStack.getItem() == ModObjects.PARCHMENT.getModItem()) {
				if (flag && !player.isInvisible()) {
					renderPlayerArm(mStack, renderBuffer, light, dems, prems, handside);
				}
				renderParchment(mStack, renderBuffer, light, dems, handside, dems, itemStack);
			} else if (itemStack.getItem() == ModObjects.SPELL.getModItem()) {
				renderSpell();
			}
		}
		mStack.popPose();
	}

	@SuppressWarnings("resource")
	private static void renderParchment(MatrixStack mStack, IRenderTypeBuffer rBuffer, int light, float b,
			HandSide handSide, float c, ItemStack iStack) {
		float f = handSide == HandSide.RIGHT ? 1.0F : -1.0F;
		mStack.translate((double) (f * 0.125F), -0.125D, 0.0D);
		if (!Minecraft.getInstance().player.isInvisible()) {
			mStack.pushPose();
			mStack.mulPose(Vector3f.ZP.rotationDegrees(f * 10.0F));
			renderPlayerArm(mStack, rBuffer, light, b, c, handSide);
			mStack.popPose();
		}

		mStack.pushPose();
		mStack.translate((double) (f * 0.51F), (double) (-0.08F + b * -1.2F), -0.75D);
		float f1 = MathHelper.sqrt(c);
		float f2 = MathHelper.sin(f1 * (float) Math.PI);
		float f3 = -0.5F * f2;
		float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
		float f5 = -0.3F * MathHelper.sin(c * (float) Math.PI);
		mStack.translate((double) (f * f3), (double) (f4 - 0.3F * f2), (double) f5);
		mStack.mulPose(Vector3f.XP.rotationDegrees(f2 * -45.0F));
		mStack.mulPose(Vector3f.YP.rotationDegrees(f * f2 * -30.0F));
		renderMap(mStack, rBuffer, light, iStack);
		mStack.popPose();
	}

	private static void renderMap(MatrixStack mStack, IRenderTypeBuffer rBuffer, int light, ItemStack iStack) {
		mStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		mStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
		mStack.scale(0.38F, 0.38F, 0.38F);
		mStack.translate(-0.5D, -0.5D, 0.0D);
		mStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
		// MapData mapdata = FilledMapItem.getOrCreateSavedData(iStack,
		// Minecraft.getInstance().level);
		IVertexBuilder ivertexbuilder = rBuffer
				// .getBuffer(mapdata == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
				.getBuffer(MAP_BACKGROUND);

		Matrix4f matrix4f = mStack.last().pose();
		ivertexbuilder.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light)
				.endVertex();
		ivertexbuilder.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light)
				.endVertex();
		ivertexbuilder.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light)
				.endVertex();
		ivertexbuilder.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light)
				.endVertex();
		/*
		 * if (mapdata != null) {
		 * Minecraft.getInstance().gameRenderer.getMapRenderer().render(mStack, rBuffer,
		 * mapdata, false, light); }
		 */

	}

	private static void renderSpell() {

	}

	@SuppressWarnings("resource")
	private static void renderPlayerArm(MatrixStack mStack, IRenderTypeBuffer rBuffer, int light, float prems,
			float deuz, HandSide handSide) {
		boolean flag = handSide != HandSide.LEFT;
		float f = flag ? 1.0F : -1.0F;
		float f1 = MathHelper.sqrt(deuz);
		float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
		float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
		float f4 = -0.4F * MathHelper.sin(deuz * (float) Math.PI);
		mStack.translate((double) (f * (f2 + 0.64000005F)), (double) (f3 + -0.6F + prems * -0.6F),
				(double) (f4 + -0.71999997F));
		mStack.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
		float f5 = MathHelper.sin(deuz * deuz * (float) Math.PI);
		float f6 = MathHelper.sin(f1 * (float) Math.PI);
		mStack.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
		mStack.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
		AbstractClientPlayerEntity abstractclientplayerentity = Minecraft.getInstance().player;
		Minecraft.getInstance().getTextureManager().bind(abstractclientplayerentity.getSkinTextureLocation());
		mStack.translate((double) (f * -1.0F), (double) 3.6F, 3.5D);
		mStack.mulPose(Vector3f.ZP.rotationDegrees(f * 120.0F));
		mStack.mulPose(Vector3f.XP.rotationDegrees(200.0F));
		mStack.mulPose(Vector3f.YP.rotationDegrees(f * -135.0F));
		mStack.translate((double) (f * 5.6F), 0.0D, 0.0D);
		PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher()
				.<AbstractClientPlayerEntity>getRenderer(abstractclientplayerentity);
		if (flag) {
			playerrenderer.renderRightHand(mStack, rBuffer, light, abstractclientplayerentity);
		} else {
			playerrenderer.renderLeftHand(mStack, rBuffer, light, abstractclientplayerentity);
		}

	}
}
