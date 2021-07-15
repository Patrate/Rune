package fr.emmuliette.rune.setup;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.ModObjects;
import fr.emmuliette.rune.mod.SyncHandler;
import fr.emmuliette.rune.mod.spells.SpellRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			RuneMain.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RuneMain.MOD_ID);

	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RuneMain.MOD_ID);

	public static final RegistryObject<SpecialRecipeSerializer<SpellRecipe>> SPELL_RECIPE = Registration.RECIPE
			.register("crafting_special_spell", () -> new SpecialRecipeSerializer<>(SpellRecipe::new));

	public static void register() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		RECIPE.register(modEventBus);
		ModObjects.register();
		SyncHandler.register();
	}
}
