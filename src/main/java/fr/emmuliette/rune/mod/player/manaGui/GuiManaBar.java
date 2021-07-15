package fr.emmuliette.rune.mod.player.manaGui;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.player.capability.ICaster;
import fr.emmuliette.rune.mod.player.capability.CasterCapability;
import fr.emmuliette.rune.mod.spells.capability.ISpell;
import fr.emmuliette.rune.mod.spells.capability.SpellCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE) // , value = Dist.CLIENT)
public class GuiManaBar {
	private static int lastMana = 0;
	private static int displayMana;
	private static long lastManaTime, manaBlinkTime;
	public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/icons.png");

	@SubscribeEvent
	public static void renderManaBar(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.HEALTH) {
			return;
		}
		Minecraft minecraft = Minecraft.getInstance();
		PlayerEntity player = (PlayerEntity) minecraft.getCameraEntity();

		try {
			ICaster cap = player.getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(player));
			render(event, minecraft, player, cap);
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}

	}

	private static void render(RenderGameOverlayEvent.Post event, Minecraft minecraft, PlayerEntity player,
			ICaster iplayer) {
		MatrixStack mStack = event.getMatrixStack();
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		int tickCount = minecraft.gui.getGuiTicks();
		bind(GUI_ICONS_LOCATION);

		// minecraft.getProfiler().push("mana");
		RenderSystem.enableBlend();

		int mana = MathHelper.ceil(iplayer.getMana());
		boolean highlight = manaBlinkTime > (long) tickCount && (manaBlinkTime - (long) tickCount) / 3L % 2L == 1L;

		if (mana < lastMana) {
			lastManaTime = Util.getMillis();
			manaBlinkTime = (long) (tickCount + 20);
		} else if (mana > lastMana) {
			lastManaTime = Util.getMillis();
			manaBlinkTime = (long) (tickCount + 10);
		}

		if (Util.getMillis() - lastManaTime > 1000L) {
			lastMana = mana;
			displayMana = mana;
			lastManaTime = Util.getMillis();
		}

		lastMana = mana;
		int manaLast = displayMana;

		float manaMax = (float) iplayer.getMaxMana();
		float bonusMana = 0;
		int manaCost = 0;
		ISpell cap = player.getItemInHand(Hand.MAIN_HAND).getCapability(SpellCapability.SPELL_CAPABILITY).orElse(null);
		if (cap == null)
			cap = player.getItemInHand(Hand.OFF_HAND).getCapability(SpellCapability.SPELL_CAPABILITY).orElse(null);
		if (cap != null)
			manaCost = MathHelper.ceil(cap.getSpell().getManaCost());

		int manaRows = MathHelper.ceil((manaMax + bonusMana) / 2.0F / 10.0F);
		int rowHeight = Math.max(10 - (manaRows - 2), 3);

		Random random = new Random((long) (tickCount * 312871));

		int left = screenWidth / 2 - 91;
		int top = screenHeight - ForgeIngameGui.left_height;
		ForgeIngameGui.left_height += (manaRows * rowHeight);
		if (rowHeight != 10)
			ForgeIngameGui.left_height += 10 - rowHeight;

		final int TOP = 0;// 9 * (minecraft.level.getLevelData().isHardcore() ? 5 : 0);
		final int BACKGROUND = (highlight ? 27 : 0);
		int MARGIN = 0;
		// if (player.hasEffect(Effects.SILENCED)) MARGIN += 36; TODO faire la mécanique
		// de silenced (pas de sort)
		// else if (player.hasEffect(Effects.FREEMAGIC)) MARGIN += 72; TODO faire la
		// mécanique de free spell
		boolean overpriced = (manaCost > (bonusMana + mana));
		float bonusRemaining = bonusMana;
		int costRemaining = manaCost;

		for (int i = MathHelper.ceil((manaMax + bonusMana) / 2.0F) - 1; i >= 0; --i) {
			// int b0 = (highlight ? 1 : 0);
			int row = MathHelper.ceil((float) (i + 1) / 10.0F) - 1;
			int x = left + i % 10 * 8;
			int y = top - row * rowHeight;

			if (mana <= 4 || overpriced)
				y += random.nextInt(2);

			blit(mStack, x, y, BACKGROUND, TOP, 9, 9);

			/*
			 * if(overpriced) { blit(mStack, x, y, BACKGROUND - 8, TOP, 9, 9); }
			 */

			if (highlight) {
				if (i * 2 + 1 < manaLast) // pair
					blit(mStack, x, y, MARGIN + 27, TOP, 9, 9);
				else if (i * 2 + 1 == manaLast) // impair
					blit(mStack, x, y, MARGIN + 36, TOP, 9, 9);
			}

			if (overpriced) {
				// Overpriced: red stars
				if (i * 2 + 1 < mana) {
					blit(mStack, x, y, MARGIN + 99, TOP, 9, 9);
				} else if (i * 2 + 1 == mana) {
					blit(mStack, x, y, MARGIN + 108, TOP, 9, 9);
				}
			} else if (bonusRemaining > 0.0F) {
				// Bonus, gold star
				if (bonusRemaining == bonusMana && bonusMana % 2.0F == 1.0F) {
					blit(mStack, x, y, MARGIN + 153, TOP, 9, 9); // 17 TODO
					bonusRemaining -= 1.0F;
				} else {
					blit(mStack, x, y, MARGIN + 144, TOP, 9, 9); // 16 TODO
					bonusRemaining -= 2.0F;
				}
			} else if (costRemaining > 0) {
				// Gost, yellow star
				if (i * 2 + 1 < mana) {
					if (costRemaining == 1) {
						// half right side
						blit(mStack, x, y, MARGIN + 90, TOP, 9, 9);
						blit(mStack, x, y, MARGIN + 54, TOP, 9, 9);
						costRemaining -= 1.0F;
					} else {
						// full
						blit(mStack, x, y, MARGIN + 72, TOP, 9, 9);
						costRemaining -= 2.0F;
					}
				} else if (i * 2 + 1 == mana) {
					// half left side
					blit(mStack, x, y, MARGIN + 81, TOP, 9, 9);
					costRemaining -= 1.0F;
				}
			} else {
				// Normal, blue stars
				if (i * 2 + 1 < mana) {
					blit(mStack, x, y, MARGIN + 45, TOP, 9, 9);
				} else if (i * 2 + 1 == mana) {
					blit(mStack, x, y, MARGIN + 54, TOP, 9, 9);
				}
			}
		}

		RenderSystem.disableBlend();
		minecraft.getProfiler().pop();

	}

	private static void blit(MatrixStack mStack, int x, int y, int i, int top, int j, int k) {
		ForgeIngameGui.blit(mStack, x, y, 1, (float) i, (float) top, j, k, 256, 256);
	}

	private static void bind(ResourceLocation res) {
		Minecraft.getInstance().getTextureManager().bind(res);
	}
}