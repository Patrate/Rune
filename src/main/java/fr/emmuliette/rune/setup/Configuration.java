package fr.emmuliette.rune.setup;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

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
		public static ManaRenderMode renderManaMode;
		public static List<? extends Integer> manaBarPosition;
		public static List<? extends Integer> wandSpellPosition;

		static {
			final Pair<ConfigImpl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigImpl::new);
			SPEC = specPair.getRight();
			INSTANCE = specPair.getLeft();
		}

		public static void bake() {
			autoUpdate = INSTANCE.autoUpdate.get();
			renderManaMode = INSTANCE.renderManaMode.get();
			manaBarPosition = INSTANCE.manaBarPosition.get();
			wandSpellPosition = INSTANCE.wandSpellPosition.get();
		}

		static class ConfigImpl {
			final BooleanValue autoUpdate;
			final EnumValue<ManaRenderMode> renderManaMode;
			final ConfigValue<List<? extends Integer>> manaBarPosition;

			final ConfigValue<List<? extends Integer>> wandSpellPosition;

			private ConfigImpl(final ForgeConfigSpec.Builder builder) {
				autoUpdate = builder.comment("Check for mod update on launch [false/true | default true]")
						.translation(RuneMain.MOD_ID + ".config.autoUpdate").define("autoUpdate", true);

				renderManaMode = builder.comment("Rendering mode for mana (stars, bar)")
						.translation(RuneMain.MOD_ID + ".config.renderManaMode")
						.defineEnum("renderManaMode", ManaRenderMode.BAR);

				manaBarPosition = builder.comment("Screen position of mana bar as [x, y], [] for default position")
						.translation(RuneMain.MOD_ID + ".config.manaBarPosition")
						.defineList("manaBarPosition", Arrays.asList(new Integer[] {}), new Predicate<Object>() {
							@Override
							public boolean test(Object t) {
								return true;
							}
						});

				wandSpellPosition = builder.comment("Screen position of wand spell as [x, y], [] for default positon")
						.translation(RuneMain.MOD_ID + ".config.wandSpellPosition").defineList("wandSpellPosition",
								Arrays.asList(new Integer[] { 2, 2 }), new Predicate<Object>() {
									@Override
									public boolean test(Object t) {
										return true;
									}
								});
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
				learnFromGrimoire = builder
						.comment("Spell must be learnt before being castable  [false/true | default true]")
						.translation(RuneMain.MOD_ID + ".config.learnFromGrimoire").define("learnFromGrimoire", true);
			}

		}

	}
}
