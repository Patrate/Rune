package fr.emmuliette.rune.mod.gui.handItem;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class Grimoire {
	@SuppressWarnings("deprecation")
	public static final RenderMaterial BOOK_LOCATION = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS,
			new ResourceLocation("entity/enchanting_table_book"));
	private static final BookModel bookModel = new BookModel();

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void renderHandParchment(RenderHandEvent event) {
		ItemStack is = event.getItemStack();
		if (is.getItem() != ModItems.GRIMOIRE.getItem()) {
			return;
		}
		renderArmWithItem(Minecraft.getInstance().player, event.getHand(), event.getItemStack(), event.getMatrixStack(),
				event.getBuffers(), 0, 0, event.getLight());
		event.setCanceled(true);
	}

	private static void renderArmWithItem(AbstractClientPlayerEntity player, Hand hand, ItemStack itemStack,
			MatrixStack mStack, IRenderTypeBuffer renderBuffer, float prems, float dems, int light) {
		boolean flag = hand == Hand.MAIN_HAND;
		HandSide handside = flag ? player.getMainArm() : player.getMainArm().getOpposite();
		mStack.pushPose();
		if (!itemStack.isEmpty()) {
			if (itemStack.getItem() == ModItems.GRIMOIRE.getItem()) {
				if (flag && !player.isInvisible()) {
					renderPlayerArm(mStack, renderBuffer, light, dems, prems, handside);
				}
				renderGrimoire(mStack, renderBuffer, light, dems, handside, dems, itemStack);
			}
		}
		mStack.popPose();
	}

	private static void renderGrimoire(MatrixStack mStack, IRenderTypeBuffer rBuffer, int light, float partialTick,
			HandSide handSide, float c, ItemStack iStack) {
		float handsideFloat = handSide == HandSide.RIGHT ? 1.0F : -1.0F;
		Minecraft minecraft = Minecraft.getInstance();
		mStack.translate((double) (handsideFloat * 0.125F), -0.125D, 0.0D);
		if (!minecraft.player.isInvisible()) {
			mStack.pushPose();
			mStack.mulPose(Vector3f.ZP.rotationDegrees(handsideFloat * 10.0F));
			renderPlayerArm(mStack, rBuffer, light, partialTick, c, handSide);
			mStack.popPose();
		}

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
