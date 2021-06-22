package fr.emmuliette.rune.data.client;

import javax.annotation.Nonnull;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, RuneMain.MOD_ID, existingFileHelper);
	}

	@Nonnull
	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	protected void registerModels() {
		withExistingParent("caster_block", modLoc("block/caster_block"));

		ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
		builder(itemGenerated, "blank_rune");
	}

	private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
		return getBuilder(name).parent(itemGenerated).texture("layer0","item/"+name);
	}

}
