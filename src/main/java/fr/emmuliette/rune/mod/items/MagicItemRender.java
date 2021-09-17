package fr.emmuliette.rune.mod.items;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.items.magicItems.WandItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
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
public class MagicItemRender {
	private static final RenderType PARCHMENT_TEXTURE = RenderType
			.text(new ResourceLocation(RuneMain.MOD_ID, "textures/handitem/parchment_texture.png"));

	@SuppressWarnings("deprecation")
	public static final RenderMaterial BOOK_LOCATION = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS,
			new ResourceLocation("entity/enchanting_table_book"));
	private static final BookModel bookModel = new BookModel();
	private static final Color[] colorList = new Color[] { Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA,
			Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW };

	public static void drawEffect(LivingEntity entity) {
//		entity.getDirection()
		for (int i = 0; i < 10; ++i) {
			float f1 = entity.getRandom().nextFloat() * ((float) Math.PI * 2F);
			float f2 = MathHelper.sqrt(entity.getRandom().nextFloat()) * 0.2F;
			float f3 = MathHelper.cos(f1) * f2;
			float f4 = MathHelper.sin(f1) * f2;
			Color c = colorList[entity.getRandom().nextInt(colorList.length)];
			int r = c.getRed();
			int g = c.getGreen();
			int b = c.getBlue();
			entity.level.addAlwaysVisibleParticle(ParticleTypes.ENTITY_EFFECT, entity.getX() + (double) f3,
					entity.getY() + 1, entity.getZ() + (double) f4, (double) ((float) r / 255.0F),
					(double) ((float) g / 255.0F), (double) ((float) b / 255.0F));
		}
	}

	@SubscribeEvent
	public static void renderHandMagicItem(RenderHandEvent event) {
		ItemStack is = event.getItemStack();
		Minecraft minecraft = Minecraft.getInstance();
		if (event.isCanceled() || minecraft.player.isInvisible() || (is.getItem() != ModItems.PARCHMENT.get()
				&& is.getItem() != ModItems.GRIMOIRE.get() && !(is.getItem() instanceof WandItem)))
			return;
//		renderArmWithItem(Minecraft.getInstance().player, event.getHand(), event.getItemStack(), event.getMatrixStack(),
//				event.getBuffers(), 0, 0, event.getLight());
		event.setCanceled(true);

		if (!minecraft.player.isInvisible()) {
			renderArm(minecraft, event.getMatrixStack(), event.getBuffers(), event.getLight(), 0f, event.getHand(), 0f,
					is);

			if (is.getItem() == ModItems.PARCHMENT.get())
				renderParchment(minecraft, event.getMatrixStack(), event.getBuffers(), event.getLight(), 0f,
						event.getHand(), 0f, is);
			if (is.getItem() == ModItems.GRIMOIRE.get())
				renderGrimoire(minecraft, event.getMatrixStack(), event.getBuffers(), event.getLight(), 0f,
						event.getHand(), 0f, is);
			if (is.getItem() instanceof WandItem)
				renderWand(minecraft, event.getMatrixStack(), event.getBuffers(), event.getLight(), 0f, event.getHand(),
						0f, is);
		}

		if (minecraft.player.isUsingItem() && minecraft.player.getUseItemRemainingTicks() > 0) {
			drawEffect(minecraft.player);
		}
	}

	private static void renderArm(Minecraft minecraft, MatrixStack mStack, IRenderTypeBuffer rBuffer, int light,
			float partialTick, Hand hand, float c, ItemStack iStack) {
		boolean flag = hand == Hand.MAIN_HAND;
		HandSide handside = flag ? minecraft.player.getMainArm() : minecraft.player.getMainArm().getOpposite();
		float f = handside == HandSide.RIGHT ? 1.0F : -1.0F;

		mStack.pushPose();
		mStack.translate((double) (f * 0.125F), -0.125D, 0.0D);
		mStack.mulPose(Vector3f.ZP.rotationDegrees(f * 10.0F));
		renderPlayerArm(mStack, rBuffer, light, partialTick, c, handside);
		mStack.popPose();
	}

	private static void renderParchment(Minecraft minecraft, MatrixStack mStack, IRenderTypeBuffer rBuffer, int light,
			float partialTick, Hand hand, float c, ItemStack iStack) {
		boolean flag = hand == Hand.MAIN_HAND;
		HandSide handside = flag ? minecraft.player.getMainArm() : minecraft.player.getMainArm().getOpposite();
		float f = handside == HandSide.RIGHT ? 1.0F : -1.0F;

		mStack.pushPose();
		mStack.translate((double) (f * 0.51F), (double) (-0.08F + partialTick * -1.2F), -0.75D);
		float f1 = MathHelper.sqrt(c);
		float f2 = MathHelper.sin(f1 * (float) Math.PI);
		float f3 = -0.5F * f2;
		float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
		float f5 = -0.3F * MathHelper.sin(c * (float) Math.PI);
		mStack.translate((double) (f * f3), (double) (f4 - 0.3F * f2), (double) f5);
		mStack.mulPose(Vector3f.XP.rotationDegrees(f2 * -45.0F));
		mStack.mulPose(Vector3f.YP.rotationDegrees(f * f2 * -30.0F));

		mStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		mStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
		mStack.scale(0.38F, 0.38F, 0.38F);
		mStack.translate(-0.5D, -0.5D, 0.0D);
		mStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
		IVertexBuilder ivertexbuilder = rBuffer.getBuffer(PARCHMENT_TEXTURE);

		Matrix4f matrix4f = mStack.last().pose();
		ivertexbuilder.vertex(matrix4f, -7.0F, 155.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light)
				.endVertex();
		ivertexbuilder.vertex(matrix4f, 135.0F, 155.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light)
				.endVertex();
		ivertexbuilder.vertex(matrix4f, 135.0F, -27.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light)
				.endVertex();
		ivertexbuilder.vertex(matrix4f, -7.0F, -27.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light)
				.endVertex();

		mStack.popPose();
	}

	private static void renderGrimoire(Minecraft minecraft, MatrixStack mStack, IRenderTypeBuffer rBuffer, int light,
			float partialTick, Hand hand, float c, ItemStack iStack) {
		mStack.pushPose();
		mStack.translate(0.5D, 0.75D, 0.5D);
		float f = partialTick;
		mStack.translate(0.2D, (double) (0.9F + MathHelper.sin(f * 0.1F) * 0.01F), 0.0D);

		float f1 = 0f;

		while (f1 < -(float) Math.PI) {
			f1 += ((float) Math.PI * 2F);
		}

		float f2 = f1;
		mStack.mulPose(Vector3f.YP.rotation(-f2));
		mStack.mulPose(Vector3f.ZP.rotationDegrees(80.0F));
		float f3 = MathHelper.lerp(0, 0, 0);
		float f4 = MathHelper.frac(f3 + 0.25F) * 1.6F - 0.3F;
		float f5 = MathHelper.frac(f3 + 0.75F) * 1.6F - 0.3F;
		float f6 = MathHelper.lerp(0, 0, 0);
		bookModel.setupAnim(f, MathHelper.clamp(f4, 0.0F, 1.0F), MathHelper.clamp(f5, 0.0F, 1.0F), f6);
		IVertexBuilder ivertexbuilder = BOOK_LOCATION.buffer(rBuffer, RenderType::entitySolid);
		bookModel.render(mStack, ivertexbuilder, light, 1, 1.0F, 1.0F, 1.0F, 1.0F);
		mStack.popPose();
	}

	private static void renderWand(Minecraft minecraft, MatrixStack mStack, IRenderTypeBuffer rBuffer, int light,
			float partialTick, Hand hand, float c, ItemStack iStack) {
		mStack.pushPose();
		mStack.translate(0, 1, 0);
		mStack.scale(0.5F, 0.5F, 0.5F);
		minecraft.getItemRenderer().renderAndDecorateItem(iStack, light, 0);
		mStack.popPose();
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
