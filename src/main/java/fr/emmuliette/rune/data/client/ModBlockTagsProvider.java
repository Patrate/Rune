package fr.emmuliette.rune.data.client;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.blocks.ModBlocks;
import fr.emmuliette.rune.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {

	public ModBlockTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
		super(gen, RuneMain.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(ModTags.Blocks.CASTER_BLOCK).add(ModBlocks.CASTER_BLOCK.get());

		tag(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.CASTER_BLOCK);
	}

}
