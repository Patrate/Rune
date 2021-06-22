package fr.emmuliette.rune.setup;

import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

public class ModBlocks {
	
	public static final RegistryObject<Block> CASTER_BLOCK = register("caster_block", () -> new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL).strength(3.0f, 3.0f).harvestLevel(1).harvestTool(ToolType.PICKAXE).sound(SoundType.NETHERRACK)));
	
	private static<T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
		return Registration.BLOCKS.register(name, block);
		
	}
	
	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		RegistryObject<T> ret = registerNoItem(name, block);
		Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
		return ret;
	}
	
	static void register() {
	}
}