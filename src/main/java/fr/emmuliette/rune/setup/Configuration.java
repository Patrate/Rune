package fr.emmuliette.rune.setup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import fr.emmuliette.rune.RuneMain;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Thanks to Cadiboo/NoCubes
 *
 */
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Configuration {

	public static void register(final ModLoadingContext context) {
		context.registerConfig(ModConfig.Type.CLIENT, Client.SPEC);
		context.registerConfig(ModConfig.Type.SERVER, Server.SPEC);
	}

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
		RuneMain.LOGGER.info("Baking configuration");
		final ForgeConfigSpec spec = configEvent.getConfig().getSpec();
		if (spec == Client.SPEC)
			Client.bake();
		else if (spec == Server.SPEC)
			Server.bake();
	}

	public static class Client {
		public static enum ManaRenderMode {
			STARS, BAR;
		}

		public static final ConfigImpl INSTANCE;
		public static final ForgeConfigSpec SPEC;

		public static boolean autoUpdate;
		public static boolean renderShortGrass;
		public static Set<String> inactiveItem;
		public static ManaRenderMode renderManaMode;

		static {
			final Pair<ConfigImpl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigImpl::new);
			SPEC = specPair.getRight();
			INSTANCE = specPair.getLeft();
		}

		public static void bake() {
			autoUpdate = INSTANCE.autoUpdate.get();
			inactiveItem = new HashSet<String>(INSTANCE.inactiveItemConfig.get());
			renderManaMode = INSTANCE.renderManaMode.get();
		}

		static class ConfigImpl {
			final BooleanValue autoUpdate;
			final ConfigValue<List<String>> inactiveItemConfig;
			final EnumValue<ManaRenderMode> renderManaMode;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			private ConfigImpl(final ForgeConfigSpec.Builder builder) {
				autoUpdate = builder.comment("Check for mod update on launch [false/true | default true]")
						.translation(RuneMain.MOD_ID + ".config.autoUpdate").define("autoUpdate", true);
				
				renderManaMode = builder.comment("Rendering mode for mana (stars, bar)")
						.translation(RuneMain.MOD_ID + ".config.renderManaMode").defineEnum("renderManaMode", ManaRenderMode.BAR);

				inactiveItemConfig = ((ConfigValue) builder.comment("List of inactive items")
						.translation(RuneMain.MOD_ID + ".config.inactiveItemConfig").defineList("inactiveItemConfig",
								ConfigHelper::getDefaultInactiveItem, String.class::isInstance));
			}

		}

	}

	public static class Server {

		public static final ConfigImpl INSTANCE;
		public static final ForgeConfigSpec SPEC;

		public static Set<String> inactiveItem;
		public static boolean learnFromGrimoire;
		
		static {
			final Pair<ConfigImpl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigImpl::new);
			SPEC = specPair.getRight();
			INSTANCE = specPair.getLeft();
		}

		public static void bake() {
			inactiveItem = new HashSet<String>(INSTANCE.inactiveItem.get());
			learnFromGrimoire = INSTANCE.learnFromGrimoire.get();
			ConfigHelper.performServerConnectionStatusValidation();
		}

		static class ConfigImpl {
			final BooleanValue learnFromGrimoire;
			final ConfigValue<List<String>> inactiveItem;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			private ConfigImpl(final ForgeConfigSpec.Builder builder) {
				inactiveItem = ((ConfigValue) builder.defineList("inactiveItem", ConfigHelper::getDefaultInactiveItem,
						String.class::isInstance));
				learnFromGrimoire = builder.comment("Spell must be learnt before being castable  [false/true | default true]")
						.translation(RuneMain.MOD_ID + ".config.learnFromGrimoire").define("learnFromGrimoire", true);
			}

		}

	}
}
