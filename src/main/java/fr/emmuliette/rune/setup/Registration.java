package fr.emmuliette.rune.setup;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.ModObjects;
import fr.emmuliette.rune.mod.SyncHandler;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingRecipe;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingScreen;
import fr.emmuliette.rune.mod.containers.ModContainers;
import fr.emmuliette.rune.mod.effects.ModEffects;
import fr.emmuliette.rune.mod.spells.SpellRecipe;
import fr.emmuliette.rune.mod.spells.entities.ModEntities;
import fr.emmuliette.rune.mod.spells.renderer.ModRenderer;
import fr.emmuliette.rune.mod.spells.tags.MainTag;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			RuneMain.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RuneMain.MOD_ID);

	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS,
			RuneMain.MOD_ID);

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			RuneMain.MOD_ID);

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, RuneMain.MOD_ID);

	public static final DeferredRegister<ContainerType<?>> CONTAINER = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, RuneMain.MOD_ID);

	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, RuneMain.MOD_ID);

	public static final RegistryObject<SpecialRecipeSerializer<SpellRecipe>> SPELL_RECIPE = Registration.RECIPE
			.register("crafting_special_spell", () -> new SpecialRecipeSerializer<>(SpellRecipe::new));

	public static final IRecipeType<SpellBindingRecipe> SPELLBINDING_RECIPE = IRecipeType
			.register("spellbinding_recipe");

	public static void register() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		RECIPE.register(modEventBus);
		EFFECTS.register(modEventBus);
		ENTITIES.register(modEventBus);
		TILE_ENTITIES.register(modEventBus);
		CONTAINER.register(modEventBus);

		ModEffects.register();
		ModObjects.register();
		SyncHandler.register();
		ModContainers.register();
		ModEntities.register();

		MainTag.register();
	}

	// For client only stuff (rendering)
	public static void clientRegister(final FMLClientSetupEvent event) {
		ModRenderer.register();
		ScreenManager.register(ModContainers.SPELLBINDING.get(), SpellBindingScreen::new);
	}
}
