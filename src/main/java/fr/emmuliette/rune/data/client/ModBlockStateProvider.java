package fr.emmuliette.rune.data.client;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.blocks.ModBlocks;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(net.minecraft.data.DataGenerator dataGenerator, ExistingFileHelper exhelp) {
		super(dataGenerator, RuneMain.MOD_ID, exhelp);
	}

	@Override
	protected void registerStatesAndModels() {
		for(ModBlocks modBlock:ModBlocks.values()) {
//			simpleBlock(ModBlocks.CASTER_BLOCK.getBlock());
			try {
				simpleBlock(modBlock.get());
			} catch(Exception e) {
				System.out.println("No texture for " + modBlock.name() + ": " + e.getMessage());
			}
		}
//		simpleBlock(ModBlocks.CASTER_BLOCK.getBlock());
//		simpleBlock(ModBlocks.ANCHORED_BLOCK.getBlock());
//		simpleBlock(ModBlocks.PHASED_BLOCK.getBlock());
	}

}
