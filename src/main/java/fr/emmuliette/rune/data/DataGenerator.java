package fr.emmuliette.rune.data;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.data.client.ModBlockStateProvider;
import fr.emmuliette.rune.data.client.ModBlockTagsProvider;
import fr.emmuliette.rune.data.client.ModItemModelProvider;
import fr.emmuliette.rune.data.client.ModItemTagsProvider;
import fr.emmuliette.rune.data.client.ModLootTableProvider;
import fr.emmuliette.rune.data.client.ModRecipeProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.MOD)
public final class DataGenerator {

	private DataGenerator() {
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent e) {
		net.minecraft.data.DataGenerator gen = e.getGenerator();
		gen.addProvider(new ModBlockStateProvider(gen, e.getExistingFileHelper()));
		gen.addProvider(new ModItemModelProvider(gen, e.getExistingFileHelper()));

		ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen, e.getExistingFileHelper());
		gen.addProvider(blockTags);
		gen.addProvider(new ModItemTagsProvider(gen, blockTags, e.getExistingFileHelper()));
		gen.addProvider(new ModLootTableProvider(gen));
		gen.addProvider(new ModRecipeProvider(gen));
	}

}
