package fr.emmuliette.rune.setup;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.blocks.ModBlocks;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE)
public class BiomeLoadingEventListener {

	@SubscribeEvent
	public static void generateOres(BiomeLoadingEvent event) {
		ConfiguredFeature<?, ?> ORE_MANA = Feature.ORE
				.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
						ModBlocks.MANA_ORE_BLOCK.get().defaultBlockState(), 6))
				.range(64).squared().count(16);
		event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ORE_MANA);
	}
}
