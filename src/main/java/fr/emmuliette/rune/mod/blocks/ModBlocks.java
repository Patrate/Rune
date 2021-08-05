package fr.emmuliette.rune.mod.blocks;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public enum ModBlocks {
	// Blocks
	CASTER_BLOCK(new ModBlock("caster_block", RuneMain.RUNE_GROUP,
			() -> new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL).strength(3.0f, 3.0f).harvestLevel(1)
					.harvestTool(ToolType.PICKAXE).sound(SoundType.NETHERRACK)))),

	SPELLBINDING_BLOCK(new ModBlock("spellbinding_block", RuneMain.RUNE_GROUP, () -> new SpellBindingBlock(
			AbstractBlock.Properties.of(Material.WOOD).strength(2.5f).sound(SoundType.WOOD))));

	private ModBlock entity;

	private ModBlocks(ModBlock entity) {
		this.entity = entity;
	}

	public Block getBlock() {
		return entity.getModBlock().get();
	}

	public static void register() {
	}
}
