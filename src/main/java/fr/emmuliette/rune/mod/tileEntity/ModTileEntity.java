package fr.emmuliette.rune.mod.tileEntity;

import fr.emmuliette.rune.mod.blocks.ModBlock;
import fr.emmuliette.rune.mod.blocks.ModBlocks;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class ModTileEntity {
	public static final RegistryObject<TileEntityType<PhasedTileEntity>> PHASED_TE = Registration.TILE_ENTITIES
			.register("phased_block", () -> TileEntityType.Builder
					.of(PhasedTileEntity::new, ModBlocks.PHASED_BLOCK.getBlock()).build(null));

	public static final RegistryObject<TileEntityType<AnchoredTileEntity>> ANCHORED_TE = Registration.TILE_ENTITIES
			.register("anchored_block", () -> TileEntityType.Builder
					.of(AnchoredTileEntity::new, ModBlocks.ANCHORED_BLOCK.getBlock()).build(null));

	private ModBlock entity;

	private ModTileEntity(ModBlock entity) {
		this.entity = entity;
	}

	public Block getBlock() {
		return entity.getModBlock().get();
	}

	public static void register() {
	}
}
