package fr.emmuliette.rune;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.Entity;

import fr.emmuliette.rune.mod.RuneItemGroup;
import fr.emmuliette.rune.mod.items.SpellItem;
import fr.emmuliette.rune.mod.player.capability.CasterCapability;
import fr.emmuliette.rune.mod.spells.capability.SpellCapability;
import fr.emmuliette.rune.setup.Registration;

import java.util.stream.Collectors;
	
// The value here should match an entry in the META-INF/mods.toml file
@Mod(RuneMain.MOD_ID)
public class RuneMain {
	
	public static final String MOD_ID = "rune";
	
	public static final RuneItemGroup RUNE_GROUP = new RuneItemGroup("rune");
	
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public RuneMain() {
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

    	Registration.register();
    	
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

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        SpellCapability.register();
        CasterCapability.register();
    }
    
    // ====================================================================================
    // ================================ CAPABILITIES ======================================
    // ====================================================================================
    
    @SubscribeEvent
    public void attachCapabilitiesEntity(final AttachCapabilitiesEvent<ItemStack> event)
    {
		if(event.getObject().getItem() instanceof SpellItem) {
        	event.addCapability(new ResourceLocation(MOD_ID, SpellCapability.SPELL_CAPABILITY_NAME), new SpellCapability());
    	}
    }
    
    @SubscribeEvent
    public void attachCapabilitiesPlayer(final AttachCapabilitiesEvent<Entity> event)
    {
    	if(event.getObject() instanceof PlayerEntity) {
    		event.addCapability(new ResourceLocation(MOD_ID, CasterCapability.CASTER_CAPABILITY_NAME), new CasterCapability((Entity) event.getObject()));
    	}
    }

    @SuppressWarnings("resource")
	private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
