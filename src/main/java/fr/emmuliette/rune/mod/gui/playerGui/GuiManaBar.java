package fr.emmuliette.rune.mod.gui.playerGui;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellCapability;
import fr.emmuliette.rune.mod.effects.ModEffects;
import fr.emmuliette.rune.mod.items.magicItems.WandItem;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.setup.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class GuiManaBar {
	private static int lastMana = 0;
	private static long lastManaTime, manaBlinkTime;
	public static final ResourceLocation MOD_ICONS_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/icons.png");

	@SubscribeEvent
	public static void renderManaBar(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.ARMOR) {
			return;
		}
		Minecraft minecraft = Minecraft.getInstance();
		PlayerEntity player = (PlayerEntity) minecraft.getCameraEntity();
		if (player.isAlive())
			player.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(new NonNullConsumer<ICaster>() {

				@Override
				public void accept(ICaster cap) {
					render(event, minecraft, player, cap);
					bind(ForgeIngameGui.GUI_ICONS_LOCATION);
				}

			});

	}

	private static void render(RenderGameOverlayEvent.Post event, Minecraft minecraft, PlayerEntity player,
			ICaster iplayer) {
		MatrixStack mStack = event.getMatrixStack();
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		int tickCount = minecraft.gui.getGuiTicks();

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
			lastManaTime = Util.getMillis();
		}

		lastMana = mana;

		float manaMax = (float) iplayer.getMaxMana();
		float bonusMana = 0;
		int manaCost = 0;

		Spell spell = null;
		if (player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof WandItem) {
			spell = WandItem.getCurrentSpell(player.getItemInHand(Hand.MAIN_HAND), player);
		} else if (player.getItemInHand(Hand.OFF_HAND).getItem() instanceof WandItem) {
			spell = WandItem.getCurrentSpell(player.getItemInHand(Hand.OFF_HAND), player);
		} else {
			ISpell spellCap = player.getItemInHand(Hand.MAIN_HAND).getCapability(SpellCapability.SPELL_CAPABILITY)
					.orElse(null);
			if (spellCap == null)
				spellCap = player.getItemInHand(Hand.OFF_HAND).getCapability(SpellCapability.SPELL_CAPABILITY)
						.orElse(null);
			if (spellCap != null)
				spell = spellCap.getSpell();
		}

		if (spell != null)
			manaCost = MathHelper.ceil(spell.getCost().getManaCost());

		boolean overpriced = (manaCost > (bonusMana + mana));
		float bonusRemaining = bonusMana;
		int costRemaining = manaCost;

		int rowHeight = 11;

		Random random = new Random((long) (tickCount * 312871));

		int left = screenWidth / 2 - 91;
		int top = screenHeight - ForgeIngameGui.left_height;
		List<? extends Integer> configPos = Configuration.Client.manaBarPosition;
		if (configPos != null && configPos.size() == 2) {
			left = configPos.get(0);
			top = configPos.get(1);
		}

		ForgeIngameGui.left_height += rowHeight;
		if (rowHeight != 10)
			ForgeIngameGui.left_height += 10 - rowHeight;

		// TODO mana bar gui effects
		boolean silenced = player.hasEffect(ModEffects.SILENCED.get());
//		boolean mana_regen = player.hasEffect(ModEffect.MANA_REGENERATION.get());
//		boolean mana_poison = player.hasEffect(ModEffect.MANA_POISON.get());
//		boolean mana_boost = player.hasEffect(ModEffect.MANA_BOOST.get());
//		boolean free_spell = player.hasEffect(ModEffect.FREE_SPELL.get());

		final int STEP = 9;
		final int Y_BORDER = 0, Y_FULL = 9;

		// Change when effect silenced is implemented
		final int BACKGROUND = (silenced) ? 9 * STEP : 0;

		int x = left - 8 * (int) Math.max(1, (Math.floor(Math.log10(mana))));
		int y = top;
		int manaGold = MathHelper.ceil(((mana + bonusMana) - 20f) / 20f);

		minecraft.getProfiler().push("manaValue");
		x = left - ((int) Math.log10(mana) + 1) * 8;
		y = top;
		drawString(mStack, minecraft.font, new StringTextComponent("" + mana), x + 1, y + 2, Color.BLUE.getRGB());
		drawString(mStack, minecraft.font, new StringTextComponent("" + mana), x, y + 1, Color.WHITE.getRGB());
		minecraft.getProfiler().pop();

		switch (Configuration.Client.renderManaMode) {
		case BAR:
			minecraft.getProfiler().push("manaBar");
			RenderSystem.enableBlend();
			bind(MOD_ICONS_LOCATION);
			float X = 79f;

			float a = (mana / manaMax) * X;
			float b = (mana - costRemaining) / manaMax * X;
			for (int i = 0; i < X; i++) {
				x = left + 1 + i;
				if (i <= a) {
					if (overpriced)
						blit(mStack, x, y, STEP * 10 + 2, Y_FULL * 2, 1, STEP);
					else if (i >= b)
						blit(mStack, x, y, STEP * 10 + 1, Y_FULL * 2, 1, STEP);
					else
						blit(mStack, x, y, STEP * 10, Y_FULL * 2, 1, STEP);
				} else {
					blit(mStack, x, y, STEP * 10 + 3, Y_FULL * 2, 1, STEP);
				}
			}

			x = left;
			y = top;
			blit(mStack, x, y, 0, Y_FULL * 2, STEP * 9, STEP);

			minecraft.getProfiler().pop();

			RenderSystem.disableBlend();
			break;
		case STARS:
		default:
			// =================================================================================
			RenderSystem.enableBlend();
			bind(MOD_ICONS_LOCATION);

			// minecraft.getProfiler().push("mana");

			for (int i = Math.min(9, MathHelper.ceil((manaMax + bonusMana) / 2.0F) - 1); i >= 0; --i) {
				int manaTmp = (mana - 1) % 20 + 1;
				x = left + i % 10 * 8;
				y = top;

				if (mana <= 4 || overpriced)
					y += random.nextInt(2);

				// drawing background
				// black if silenced
				// normal otherwise
				if (overpriced && i * 2 + 1 <= manaTmp) {
					// red if overprice
					if (i * 2 + 1 < manaTmp) // full
						blit(mStack, x, y, STEP * 9, Y_FULL * 1, STEP, STEP);
					else if (i * 2 + 1 == manaTmp) { // half
						blit(mStack, x, y, STEP * 10, Y_FULL * 1, STEP, STEP);
						blit(mStack, x, y, BACKGROUND + 1, Y_FULL * 1, STEP, STEP);
					}
				} else {
					blit(mStack, x, y, BACKGROUND, Y_FULL * 1, STEP, STEP);
				}

				// drawing border
				if (manaGold > i) {
					// gold if manaGold
					blit(mStack, x, y, STEP * 12, Y_BORDER, STEP, STEP);
				} else if (overpriced && i * 2 + 1 <= manaTmp) {
					// red if overprice
					if (i * 2 + 1 < manaTmp) // full
						blit(mStack, x, y, STEP * 6, Y_BORDER, STEP, STEP);
					else if (i * 2 + 1 == manaTmp) { // half
						blit(mStack, x, y, STEP * 7, Y_BORDER, STEP, STEP);
						if (highlight)
							blit(mStack, x, y, STEP * 11, Y_BORDER, STEP, STEP);
						else
							blit(mStack, x, y, STEP * 2, Y_BORDER, STEP, STEP);
					}
				} else if (highlight) {
					// white if highlight
					blit(mStack, x, y, STEP * 9, Y_BORDER, STEP, STEP);
				} else {
					// black if normal
					blit(mStack, x, y, 0, Y_BORDER, STEP, STEP);
				}

				if (overpriced) {
					// Overpriced: red stars
					if (i * 2 + 1 < manaTmp) {
						// full
						blit(mStack, x, y, STEP * 9, Y_FULL, STEP, STEP);
					} else if (i * 2 + 1 == manaTmp) {
						// half
						blit(mStack, x, y, STEP * 10, Y_FULL, STEP, STEP);
					}
				} else if (bonusRemaining > 0.0F) {
					// Bonus, gold star
					if (bonusRemaining == bonusMana && bonusMana % 2.0F == 1.0F) {
						blit(mStack, x, y, STEP * 6, Y_FULL, STEP, STEP);
						bonusRemaining -= 1.0F;
					} else {
						blit(mStack, x, y, STEP * 7, Y_FULL, STEP, STEP);
						bonusRemaining -= 2.0F;
					}
				} else if (costRemaining > 0) {
					if (i * 2 + 1 < manaTmp) {
						if (costRemaining == 1) {
							// half right gold, half left blue
							blit(mStack, x, y, STEP * 8, Y_FULL, STEP, STEP);
							blit(mStack, x, y, STEP * 4, Y_FULL, STEP, STEP);
							costRemaining -= 1.0F;
						} else {
							// full gold
							blit(mStack, x, y, STEP * 6, Y_FULL, STEP, STEP);
							costRemaining -= 2.0F;
						}
					} else if (i * 2 + 1 == manaTmp) {
						// half left gold
						blit(mStack, x, y, STEP * 7, Y_FULL, STEP, STEP);
						costRemaining -= 1.0F;
					}
				} else {
					// Normal, blue stars
					if (i * 2 + 1 < manaTmp) {
						// full blue
						blit(mStack, x, y, STEP * 3, Y_FULL, STEP, STEP);
					} else if (i * 2 + 1 == manaTmp) {
						// left side blue
						blit(mStack, x, y, STEP * 4, Y_FULL, STEP, STEP);
					}
				}
			}
			RenderSystem.disableBlend();
			// =================================================================================
		}

		minecraft.getProfiler().pop();
	}

	private static void blit(MatrixStack mStack, int x, int y, int i, int top, int j, int k) {
		ForgeIngameGui.blit(mStack, x, y, 1, (float) i, (float) top, j, k, 256, 256);
	}

	private static void drawString(MatrixStack mStack, FontRenderer font, ITextComponent textCmp, int x, int y,
			int rgbColor) {
		ForgeIngameGui.drawString(mStack, font, textCmp, x, y, rgbColor);
	}

	private static void bind(ResourceLocation res) {
		Minecraft.getInstance().getTextureManager().bind(res);
	}
}