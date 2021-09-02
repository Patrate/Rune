package fr.emmuliette.rune.setup;

import java.util.function.Function;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.SyncHandler;
import fr.emmuliette.rune.mod.blocks.ModBlocks;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingRecipe;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingRecipeSerializer;
import fr.emmuliette.rune.mod.containers.ModContainers;
import fr.emmuliette.rune.mod.effects.ModEffects;
import fr.emmuliette.rune.mod.gui.grimoire.GrimoireScreen;
import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingScreen;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.mod.specialRecipes.RuneRecipe;
import fr.emmuliette.rune.mod.specialRecipes.SocketableRecipe;
import fr.emmuliette.rune.mod.specialRecipes.SpellRecipe;
import fr.emmuliette.rune.mod.spells.entities.ModEntities;
import fr.emmuliette.rune.mod.spells.renderer.ModRenderer;
import fr.emmuliette.rune.mod.spells.tags.MainTag;
import fr.emmuliette.rune.mod.tileEntity.ModTileEntity;
import fr.emmuliette.rune.mod.tileEntityRenderer.ModTileEntityRenderer;
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
import net.minecraft.util.ResourceLocation;
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

	public static final IRecipeType<SpellBindingRecipe> SPELLBINDING_RECIPE = IRecipeType
			.register("spellbinding_recipe");

	public static final RegistryObject<SpellBindingRecipeSerializer<SpellRecipe>> SPELL_RECIPE = Registration.RECIPE
			.register("crafting_special_spell",
					() -> new SpellBindingRecipeSerializer<SpellRecipe>(new Function<ResourceLocation, SpellRecipe>() {
						@Override
						public SpellRecipe apply(ResourceLocation t) {
							return new SpellRecipe(t);
						}
					}));

	public static final RegistryObject<SpecialRecipeSerializer<RuneRecipe>> RUNE_RECIPE = Registration.RECIPE.register(
			"crafting_special_rune",
			() -> new SpecialRecipeSerializer<RuneRecipe>(new Function<ResourceLocation, RuneRecipe>() {
				@Override
				public RuneRecipe apply(ResourceLocation t) {
					return new RuneRecipe(t);
				}
			}));
	public static final RegistryObject<SpecialRecipeSerializer<SocketableRecipe>> SOCKET_RECIPE = Registration.RECIPE
			.register("crafting_special_socket", () -> new SpecialRecipeSerializer<SocketableRecipe>(
					new Function<ResourceLocation, SocketableRecipe>() {
						@Override
						public SocketableRecipe apply(ResourceLocation t) {
							return new SocketableRecipe(t);
						}
					}));

//	public static final RegistryObject<SpecialRecipeSerializer<SpellRecipe>> CRAFTING_SPELL_RECIPE = Registration.RECIPE
//			.register("crafting_special_spell", () -> new SpecialRecipeSerializer<SpellRecipe>(new Function<ResourceLocation, SpellRecipe>() {
//				@Override
//				public SpellRecipe apply(ResourceLocation t) {
//					return new SpellRecipe(t);
//				}
//			}));

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
		ModBlocks.register();
		ModTileEntity.register();
		ModItems.register();
		SyncHandler.register();
		ModContainers.register();
		ModEntities.register();
		MainTag.register();
	}

	public static void setup() {
		ModCapabilities.register();
		RuneRecipe.register();
	}

	// For client only stuff (rendering)
	public static void clientRegister(final FMLClientSetupEvent event) {
		RenderTypeRegistry.register();
		ModRenderer.register();
		ModTileEntityRenderer.register();
		ScreenManager.register(ModContainers.SPELLBINDING.get(), SpellBindingScreen::new);
		ScreenManager.register(ModContainers.GRIMOIRE.get(), GrimoireScreen::new);
		event.enqueueWork(() -> {
			ModItemModelsProperties.register();
		});
	}
}
