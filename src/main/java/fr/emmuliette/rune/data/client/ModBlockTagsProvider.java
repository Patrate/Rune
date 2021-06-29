package fr.emmuliette.rune.data.client;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.ModObjects;
import fr.emmuliette.rune.mod.NotABlockException;
import fr.emmuliette.rune.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider{

	public ModBlockTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
		super(gen, RuneMain.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags() {
		try {
			tag(ModTags.Blocks.CASTER_BLOCK).add(ModObjects.CASTER_BLOCK.getModBlock());
		} catch (NotABlockException e) {
			e.printStackTrace();
		}
		tag(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.CASTER_BLOCK);
	}

}
