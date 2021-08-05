package fr.emmuliette.rune;

import java.io.IOException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.setup.AutoUpdater;
import fr.emmuliette.rune.setup.Configuration;
import fr.emmuliette.rune.setup.ModCapabilities;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RuneMain.MOD_ID)
public class RuneMain {

	public static final String MOD_ID = "rune";

	public static final ItemGroup RUNE_GROUP = new ItemGroup("rune_items") {
		@Override
		public ItemStack makeIcon() {
			return ModItems.BLANK_RUNE.getItem().getDefaultInstance();
		}
	};
	public static final ItemGroup RUNE_EFFECT_GROUP = new ItemGroup("rune_effects") {
		@Override
		public ItemStack makeIcon() {
			return ModItems.FIRE_RUNE.getItem().getDefaultInstance();
		}
	};
	public static final ItemGroup RUNE_OTHER_GROUP = new ItemGroup("rune_other") {
		@Override
		public ItemStack makeIcon() {
			return ModItems.PROJECTILE_RUNE.getItem().getDefaultInstance();
		}
	};

	public static final String VERSION = "0.0.1";

	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();

	public RuneMain() {
		// Maybe preloadModifiedClasses(); ?
		Registration.register();
		Configuration.register(ModLoadingContext.get());

		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		if (Configuration.Client.autoUpdate) {
			try {
				AutoUpdater.update();
			} catch (IOException e) {
				LOGGER.warn("AUTO UPDATE FAILED: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			LOGGER.info("AUTO UPDATE DEACTIVATED");
		}
		ModCapabilities.register();
	}

	@SuppressWarnings("resource")
	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
		Registration.clientRegister(event);
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
		InterModComms.sendTo("examplemod", "helloworld", () -> {
			LOGGER.info("Hello world from the MDK");
			return "Hello world";
		});
	}

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		LOGGER.info("Got IMC {}",
				event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
		LOGGER.info("HELLO from server starting");
	}

	// You can use EventBusSubscriber to automatically subscribe events on the
	// contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
			// register a new block here
			LOGGER.info("HELLO from Register Block");
		}
	}
}
