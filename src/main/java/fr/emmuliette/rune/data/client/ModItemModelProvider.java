package fr.emmuliette.rune.data.client;

import javax.annotation.Nonnull;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.AbstractModObject;
import fr.emmuliette.rune.mod.ModObjectsManager;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.setup.ModItemModelsProperties;
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
		for (AbstractModObject entity : ModObjectsManager.getItemRegister()) {
			try {
				builder(itemGenerated, entity, entity.getName());
			} catch (Exception e) {
				try {
					builder(itemGenerated, entity, "no_texture");
					System.err.println("Texture for item " + entity.getName() + " doesn't exist ! " + e.getMessage());
				} catch (Exception e2) {
					System.err.println("Texture for item " + entity.getName()
							+ " doesn't exist and no replacement found ! " + e.getMessage());
				}
			}
		}
	}

	private ItemModelBuilder builder(ModelFile itemGenerated, AbstractModObject entity, String tname) {
		String name = entity.getName();
		if (entity.getGroup() != null
				&& (entity.getGroup() == RuneMain.RUNE_EFFECT_GROUP || entity.getGroup() == RuneMain.RUNE_CAST_GROUP)) {
			String[] grades = { "wooden", "stone", "iron", "golden", "diamond", "netherite" };

			ItemModelBuilder base = getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + tname);
			float i = Grade.WOOD.getLevel();
			for (String grade : grades) {
				ItemModelBuilder b = getBuilder(grade + "_" + name).parent(itemGenerated)
						.texture("layer0", "item/blank_" + grade + "_rune").texture("layer1", "item/" + tname);
				base.override().predicate(ModItemModelsProperties.gradeRL, i++).model(b).end();
			}
			return base;
		} else {
			return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
		}
	}

}
