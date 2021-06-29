package fr.emmuliette.rune.data.client;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.ModObjects;
import fr.emmuliette.rune.mod.NotABlockException;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider{

	public ModBlockStateProvider(net.minecraft.data.DataGenerator p_i232520_1_, ExistingFileHelper exhelp) {
		super(p_i232520_1_, RuneMain.MOD_ID, exhelp);
	}

	@Override
	protected void registerStatesAndModels() {
		try {
			simpleBlock(ModObjects.CASTER_BLOCK.getModBlock());
		} catch (NotABlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
