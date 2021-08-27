package fr.emmuliette.rune.data.client;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(DataGenerator p_i232552_1_, BlockTagsProvider p_i232552_2_,
			ExistingFileHelper existingFileHelper) {
		super(p_i232552_1_, p_i232552_2_, RuneMain.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		copy(ModTags.Blocks.CASTER_BLOCK, ModTags.Items.CASTER_BLOCK);
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		tag(ModTags.Items.BLANK_RUNE).add(ModItems.BLANK_WOODEN_RUNE.get());
		tag(ModTags.Items.BLANK_RUNE).add(ModItems.BLANK_STONE_RUNE.get());
		tag(ModTags.Items.BLANK_RUNE).add(ModItems.BLANK_IRON_RUNE.get());
		tag(ModTags.Items.BLANK_RUNE).add(ModItems.BLANK_GOLDEN_RUNE.get());
		tag(ModTags.Items.BLANK_RUNE).add(ModItems.BLANK_DIAMOND_RUNE.get());
		tag(ModTags.Items.BLANK_RUNE).add(ModItems.BLANK_NETHERITE_RUNE.get());
	}

}
