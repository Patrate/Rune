package fr.emmuliette.rune.mod;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.NotABlockException;
import fr.emmuliette.rune.exception.NotAnItemException;
import fr.emmuliette.rune.mod.blocks.ModBlock;
import fr.emmuliette.rune.mod.items.ModItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

public enum ModObjects {
	BLANK_RUNE(new ModItem("blank_rune", RuneMain.RUNE_GROUP)),
	PROJECTILE_RUNE(new ModItem("projectile_rune", RuneMain.RUNE_GROUP)),
	FIRE_RUNE(new ModItem("fire_rune", RuneMain.RUNE_GROUP)),
	CASTER_BLOCK(new ModBlock("caster_block", RuneMain.RUNE_GROUP,
			() -> new Block(net.minecraft.block.AbstractBlock.Properties.of(Material.HEAVY_METAL).strength(3.0f, 3.0f)
					.harvestLevel(1).harvestTool(ToolType.PICKAXE).sound(SoundType.NETHERRACK)))),
	SPELLBINDING_BLOCK(new ModBlock("spellbinding_block", RuneMain.RUNE_GROUP,
		() -> new Block(net.minecraft.block.AbstractBlock.Properties.of(Material.HEAVY_METAL).strength(3.0f, 3.0f).harvestLevel(1).harvestTool(ToolType.PICKAXE).sound(SoundType.NETHERRACK)))),
	;
	private AbstractModObject entity;

	private ModObjects(AbstractModObject entity) {

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
