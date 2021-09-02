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

	MANA_ORE_BLOCK(new ModBlock("mana_ore_block", RuneMain.RUNE_GROUP,
			() -> new ManaOreBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops()
					.strength(3.0F, 3.0F).harvestLevel(1).harvestTool(ToolType.PICKAXE)
//			.randomTicks().lightLevel(litBlockEmission(9))
			))),

	SPELLBINDING_BLOCK(new ModBlock("spellbinding_block", RuneMain.RUNE_GROUP,
			() -> new SpellBindingBlock(
					AbstractBlock.Properties.of(Material.WOOD).strength(2.5f).sound(SoundType.WOOD)))),

	ILLUSION_BLOCK(new ModBlock("illusion_block", RuneMain.RUNE_GROUP,
			() -> new IllusionBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL).noCollission().noDrops()
					.noOcclusion().strength(-1.0f, 3600000.0F)))),

	PHASED_BLOCK(new ModBlock("phased_block", RuneMain.RUNE_GROUP,
			() -> new PhasedBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL).noCollission().noDrops()
					.noOcclusion().strength(-1.0f, 3600000.0F)))),

	ANCHORED_BLOCK(new ModBlock("anchored_block", RuneMain.RUNE_GROUP, () -> new AnchoredBlock(
			AbstractBlock.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noDrops().noOcclusion())));

	private ModBlock entity;

	private ModBlocks(ModBlock entity) {
		this.entity = entity;
	}

	public Block get() {
		return entity.getModBlock().get();
	}

	public static void register() {
	}

//	private static ToIntFunction<BlockState> litBlockEmission(int p_235420_0_) {
//		return (p_235421_1_) -> {
//			return p_235421_1_.getValue(BlockStateProperties.LIT) ? p_235420_0_ : 0;
//		};
//	}
}
