package fr.emmuliette.rune.data.client;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.blocks.ModBlocks;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(net.minecraft.data.DataGenerator p_i232520_1_, ExistingFileHelper exhelp) {
		super(p_i232520_1_, RuneMain.MOD_ID, exhelp);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ModBlocks.CASTER_BLOCK.getBlock());
	}

}
