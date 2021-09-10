package fr.emmuliette.rune.data.client;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import fr.emmuliette.rune.mod.blocks.ModBlocks;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class ModLootTableProvider extends LootTableProvider {

	public ModLootTableProvider(DataGenerator p_i50789_1_) {
		super(p_i50789_1_);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
		return ImmutableList.of(Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK),
				Pair.of(ModEntityLootTables::new, LootParameterSets.ENTITY));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationTracker) {
		map.forEach(
				(p_218436_2_, p_218436_3_) -> LootTableManager.validate(validationTracker, p_218436_2_, p_218436_3_));
	}

	public static class ModBlockLootTables extends BlockLootTables {
		@Override
		protected void addTables() {
			dropSelf(ModBlocks.CASTER_BLOCK.get());
			dropSelf(ModBlocks.SPELLBINDING_BLOCK.get());
			dropSelf(ModBlocks.SPELLIVERSE_BLOCK.get());

			add(ModBlocks.MANA_ORE_BLOCK.get(), (block) -> {
				return createSilkTouchDispatchTable(block,
						applyExplosionDecay(block,
								ItemLootEntry.lootTableItem(ModItems.MANA_ORE.get())
										.apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F)))
										.apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
			});

		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
		}
	}

	public static class ModEntityLootTables extends EntityLootTables {
		@Override
		protected void addTables() {
			// TODO loot tables
			addRuneTable(EntityType.ZOMBIE, ModItems.TOUCH_RUNE.get(), ModItems.DAMAGE_RUNE.get());
			addRuneTable(EntityType.SKELETON, ModItems.PROJECTILE_RUNE.get());
//			addRuneTable(EntityType.CREEPER, ModItems..get());
			addRuneTable(EntityType.ENDERMAN, ModItems.TELEPORT_RUNE.get());
			/*
			 * this.dropSelf(ModBlocks.CASTER_BLOCK.get());
			 * dropSelf(ModBlocks.SPELLBINDING_BLOCK.get());
			 * dropSelf(ModBlocks.SPELLIVERSE_BLOCK.get());
			 * 
			 * add(ModBlocks.MANA_ORE_BLOCK.get(), (block) -> { return
			 * createSilkTouchDispatchTable(block, applyExplosionDecay(block,
			 * ItemLootEntry.lootTableItem(ModItems.MANA_ORE.get())
			 * .apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F)))
			 * .apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))); });
			 */

		}

		private void addRuneTable(EntityType<?> entity, Item... runes) {
			// TODO Rune loot tables
//			this.add(entity, LootTable.lootTable().
//					LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
//							.add(ItemLootEntry.lootTableItem(rune)
//									.apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F)))
//									.apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))
//							.when(KilledByPlayer.killedByPlayer())));
		}

		@Override
		protected List<EntityType<?>> getKnownEntities() {
			return Registration.ENTITIES.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
		}
	}

}
