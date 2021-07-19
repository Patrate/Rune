package fr.emmuliette.rune.mod;

import java.util.function.Supplier;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.NotABlockException;
import fr.emmuliette.rune.exception.NotAnItemException;
import fr.emmuliette.rune.mod.blocks.ModBlock;
import fr.emmuliette.rune.mod.items.AdvancedModItem;
import fr.emmuliette.rune.mod.items.GrimoireSpellItem;
import fr.emmuliette.rune.mod.items.ModItem;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.items.RuneModItem;
import fr.emmuliette.rune.mod.items.SpellItem;
import fr.emmuliette.rune.mod.spells.component.castComponent.castEffect.ProjectileComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castEffect.SelfComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castEffect.TouchComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.ChargingModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.castMod.ManaTankModComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.DamageComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.FireComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.TeleportComponent;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;

public enum ModObjects {
	BLANK_RUNE(new ModItem("blank_rune", RuneMain.RUNE_GROUP)),
	PROJECTILE_RUNE(new RuneModItem("projectile_rune", RuneMain.RUNE_GROUP, ProjectileComponent.class)),
	TOUCH_RUNE(new RuneModItem("touch_rune", RuneMain.RUNE_GROUP, TouchComponent.class)),
	SELF_RUNE(new RuneModItem("self_rune", RuneMain.RUNE_GROUP, SelfComponent.class)),

	CHARGE_RUNE(new RuneModItem("charge_rune", RuneMain.RUNE_GROUP, ChargingModComponent.class)),
	MANA_TANK_RUNE(new RuneModItem("mana_tank_rune", RuneMain.RUNE_GROUP, ManaTankModComponent.class)),
	
	FIRE_RUNE(new RuneModItem("fire_rune", RuneMain.RUNE_GROUP, FireComponent.class)),
	DAMAGE_RUNE(new RuneModItem("damage_rune", RuneMain.RUNE_GROUP, DamageComponent.class)),
	TELEPORT_RUNE(new RuneModItem("teleport_rune", RuneMain.RUNE_GROUP, TeleportComponent.class)),
	
	SPELL(new AdvancedModItem("spell", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new GrimoireSpellItem(new Item.Properties().tab(getGroup()).stacksTo(1));
		}
	}),
	PARCHMENT(new AdvancedModItem("parchment", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new SpellItem(new Item.Properties().tab(getGroup()).stacksTo(64));
		}
	}),
	GRIMOIRE(new AdvancedModItem("grimoire", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new SpellItem(new Item.Properties().tab(getGroup()).stacksTo(1));
		}
	}),
	SOCKET(new AdvancedModItem("socket", RuneMain.RUNE_GROUP) {
		@Override
		protected Supplier<? extends Item> getItemSupplier() {
			return () -> new SpellItem(new Item.Properties().tab(getGroup()).stacksTo(64));
		}
	}),
	
	CASTER_BLOCK(new ModBlock("caster_block", RuneMain.RUNE_GROUP,
			() -> new Block(net.minecraft.block.AbstractBlock.Properties.of(Material.HEAVY_METAL).strength(3.0f, 3.0f)
					.harvestLevel(1).harvestTool(ToolType.PICKAXE).sound(SoundType.NETHERRACK))));

	private AbstractModObject entity;

	private ModObjects(AbstractModObject entity) {
		new RuneItem(ProjectileComponent.class, (new Item.Properties()).tab(ItemGroup.TAB_MATERIALS));

		this.entity = entity;
	}

	public static void register() {
	}

	public AbstractModObject getEntity() {
		return entity;
	}

	public ModItem getItem() throws NotAnItemException {
		if (entity instanceof ModItem) {
			return (ModItem) entity;
		}
		throw new NotAnItemException(entity);
	}

	public ModBlock getBlock() throws NotABlockException {
		if (entity instanceof ModBlock) {
			return (ModBlock) entity;
		}
		throw new NotABlockException(entity);
	}

	public Item getModItem() throws NotAnItemException {
		if (entity instanceof ModItem) {
			return ((ModItem) entity).getModItem().get();
		}
		throw new NotAnItemException(entity);
	}

	public Block getModBlock() throws NotABlockException {
		if (entity instanceof ModBlock) {
			return ((ModBlock) entity).getModBlock().get();
		}
		throw new NotABlockException(entity);
	}
}
