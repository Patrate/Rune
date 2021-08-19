package fr.emmuliette.rune.mod.items;

import java.util.function.Supplier;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.items.magicItems.WandItem;
import fr.emmuliette.rune.mod.items.spellItems.GrimoireSpellItem;
import fr.emmuliette.rune.mod.items.spellItems.SpellItem;
import fr.emmuliette.rune.mod.spells.component.castComponent.castEffect.ProjectileComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castEffect.SelfComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castEffect.TouchComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.ChannelingModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.ChargingModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.LoadingModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.ToggleModComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.DamageComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.FireComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.TeleportComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.blockEffects.AnchorBlockEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.blockEffects.MoveBlockEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.blockEffects.PhaseBlockEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.SilenceEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.BlindnessEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.ConfusionEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.DamageBoostEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.DamageResistanceEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.DigSlowEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.DigSpeedEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.FireResistanceEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.GlowEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.InvisibilityEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.JumpEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.LevitationEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.MoveDownEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.MoveSpeedEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.NightVisionEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.PoisonEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.RegenerationEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.SlowFallEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.WaterBreathingEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.WeaknessEffectComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.potionEffect.vanilla.WitherEffectComponent;
import fr.emmuliette.rune.mod.spells.component.structureComponent.MagicEntityComponent;
import net.minecraft.item.Item;

public enum ModItems {
	WANDA(new AdvancedModItem("wand_a", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new WandItem((WandItem.MagicItemProperties) new WandItem.MagicItemProperties().maxMana(5)
					.chargeSpeed(200).tab(getGroup()).stacksTo(1));
		}
	}), WANDB(new AdvancedModItem("wand_b", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new WandItem((WandItem.MagicItemProperties) new WandItem.MagicItemProperties().maxMana(40)
					.chargeSpeed(20).tab(getGroup()).stacksTo(1));
		}
	}),

	BLANK_RUNE(new ModItem("blank_rune", RuneMain.RUNE_GROUP)),
	// Cast
	PROJECTILE_RUNE(new RuneModItem("projectile_rune", RuneMain.RUNE_OTHER_GROUP, ProjectileComponent.class)),
	TOUCH_RUNE(new RuneModItem("touch_rune", RuneMain.RUNE_OTHER_GROUP, TouchComponent.class)),
	SELF_RUNE(new RuneModItem("self_rune", RuneMain.RUNE_OTHER_GROUP, SelfComponent.class)),

	// Cast mod
	LOAD_RUNE(new RuneModItem("load_rune", RuneMain.RUNE_OTHER_GROUP, LoadingModComponent.class)),
	CHARGE_RUNE(new RuneModItem("charge_rune", RuneMain.RUNE_OTHER_GROUP, ChargingModComponent.class)),
	CHANNEL_RUNE(new RuneModItem("channel_rune", RuneMain.RUNE_OTHER_GROUP, ChannelingModComponent.class)),
	TOGGLE_RUNE(new RuneModItem("toggle_rune", RuneMain.RUNE_OTHER_GROUP, ToggleModComponent.class)),

	// MAGIC ENTITY
	MAGIC_ENTITY_RUNE(new RuneModItem("magic_entity_rune", RuneMain.RUNE_OTHER_GROUP, MagicEntityComponent.class)),

	// Effect
	FIRE_RUNE(new RuneModItem("fire_rune", RuneMain.RUNE_EFFECT_GROUP, FireComponent.class)),
	DAMAGE_RUNE(new RuneModItem("damage_rune", RuneMain.RUNE_EFFECT_GROUP, DamageComponent.class)),
	TELEPORT_RUNE(new RuneModItem("teleport_rune", RuneMain.RUNE_EFFECT_GROUP, TeleportComponent.class)),
	SILENCE_RUNE(new RuneModItem("silence_rune", RuneMain.RUNE_EFFECT_GROUP, SilenceEffectComponent.class)),

	// Block effects
	MOVE_BLOCK_RUNE(new RuneModItem("move_block_rune", RuneMain.RUNE_EFFECT_GROUP, MoveBlockEffectComponent.class)),
	PHASE_BLOCK_RUNE(new RuneModItem("phase_block_rune", RuneMain.RUNE_EFFECT_GROUP, PhaseBlockEffectComponent.class)),
	ANCHOR_BLOCK_RUNE(new RuneModItem("anchor_block_rune", RuneMain.RUNE_EFFECT_GROUP, AnchorBlockEffectComponent.class)),

	// VANILLA EFFECTS
	BLINDNESS_RUNE(new RuneModItem("blindness_rune", RuneMain.RUNE_EFFECT_GROUP, BlindnessEffectComponent.class)),
	CONFUSION_RUNE(new RuneModItem("confusion_rune", RuneMain.RUNE_EFFECT_GROUP, ConfusionEffectComponent.class)),
	DAMAGEBOOST_RUNE(
			new RuneModItem("damage_boost_rune", RuneMain.RUNE_EFFECT_GROUP, DamageBoostEffectComponent.class)),
	DAMAGERESISTANCE_RUNE(new RuneModItem("damage_resistance_rune", RuneMain.RUNE_EFFECT_GROUP,
			DamageResistanceEffectComponent.class)),
	DIGSLOW_RUNE(new RuneModItem("dig_slow_rune", RuneMain.RUNE_EFFECT_GROUP, DigSlowEffectComponent.class)),
	DIGSPEED_RUNE(new RuneModItem("dig_speed_rune", RuneMain.RUNE_EFFECT_GROUP, DigSpeedEffectComponent.class)),
	FIRERESISTANCE_RUNE(
			new RuneModItem("fire_resistance_rune", RuneMain.RUNE_EFFECT_GROUP, FireResistanceEffectComponent.class)),
	GLOW_RUNE(new RuneModItem("glow_rune", RuneMain.RUNE_EFFECT_GROUP, GlowEffectComponent.class)),
	INVISIBILITY_RUNE(
			new RuneModItem("invisibility_rune", RuneMain.RUNE_EFFECT_GROUP, InvisibilityEffectComponent.class)),
	JUMP_RUNE(new RuneModItem("jump_rune", RuneMain.RUNE_EFFECT_GROUP, JumpEffectComponent.class)),
	LEVITATION_RUNE(new RuneModItem("levitation_rune", RuneMain.RUNE_EFFECT_GROUP, LevitationEffectComponent.class)),
	MOVESLOW_RUNE(new RuneModItem("move_slow_rune", RuneMain.RUNE_EFFECT_GROUP, MoveDownEffectComponent.class)),
	MOVESPEED_RUNE(new RuneModItem("move_speed_rune", RuneMain.RUNE_EFFECT_GROUP, MoveSpeedEffectComponent.class)),
	NIGHTVISION_RUNE(new RuneModItem("nightvision_rune", RuneMain.RUNE_EFFECT_GROUP, NightVisionEffectComponent.class)),
	POISON_RUNE(new RuneModItem("poison_rune", RuneMain.RUNE_EFFECT_GROUP, PoisonEffectComponent.class)),
	REGENERATION_RUNE(
			new RuneModItem("regeneration_rune", RuneMain.RUNE_EFFECT_GROUP, RegenerationEffectComponent.class)),
	SLOWFALL_RUNE(new RuneModItem("slow_fall_rune", RuneMain.RUNE_EFFECT_GROUP, SlowFallEffectComponent.class)),
	WATERBREATHING_RUNE(
			new RuneModItem("water_breathing_rune", RuneMain.RUNE_EFFECT_GROUP, WaterBreathingEffectComponent.class)),
	WEAKNESS_RUNE(new RuneModItem("weakness_rune", RuneMain.RUNE_EFFECT_GROUP, WeaknessEffectComponent.class)),
	WITHER_RUNE(new RuneModItem("wither_rune", RuneMain.RUNE_EFFECT_GROUP, WitherEffectComponent.class)),

	// Spells

	SPELL(new AdvancedModItem("spell", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new GrimoireSpellItem(new Item.Properties().tab(getGroup()).stacksTo(1));
		}
	}), PARCHMENT(new AdvancedModItem("parchment", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new SpellItem(new Item.Properties().tab(getGroup()).stacksTo(64));
		}
	}), GRIMOIRE(new AdvancedModItem("grimoire", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new SpellItem(new Item.Properties().tab(getGroup()).stacksTo(1));
		}
	}), SOCKET(new AdvancedModItem("socket", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new SpellItem(new Item.Properties().tab(getGroup()).stacksTo(64));
		}
	});

	private ModItem entity;

	private ModItems(ModItem entity) {
		this.entity = entity;
	}

	public Item getItem() {
		return entity.getModItem().get();
	}

	public static void register() {
	}
}
