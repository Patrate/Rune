package fr.emmuliette.rune.mod;

import fr.emmuliette.rune.mod.blocks.ModBlock;
import fr.emmuliette.rune.mod.items.ModItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;

public enum ModObjects {
	BLANK_RUNE(new ModItem("blank_rune", ItemGroup.TAB_MISC)), PROJECTILE_RUNE(new ModItem("projectile_rune", ItemGroup.TAB_MISC)), FIRE_RUNE(new ModItem("fire_rune", ItemGroup.TAB_MISC)),
	CASTER_BLOCK(new ModBlock("caster_block", ItemGroup.TAB_MISC,
			() -> new Block(net.minecraft.block.AbstractBlock.Properties.of(Material.HEAVY_METAL).strength(3.0f, 3.0f).harvestLevel(1).harvestTool(ToolType.PICKAXE).sound(SoundType.NETHERRACK))));
	
	private AbstractModObject entity;
	
	private ModObjects(AbstractModObject entity) {
		
		this.entity = entity;
	}
	
	public static void register() {}
	
	public AbstractModObject getEntity() {
		return entity;
	}
	
	public ModItem getItem() throws NotAnItemException {
		if(entity instanceof ModItem) {
			return (ModItem) entity;
		}
		throw new NotAnItemException(entity);
	}
	
	public ModBlock getBlock() throws NotABlockException {
		if(entity instanceof ModBlock) {
			return (ModBlock) entity;
		}
		throw new NotABlockException(entity);
	}
	
	public Item getModItem() throws NotAnItemException {
		if(entity instanceof ModItem) {
			return ((ModItem) entity).getModItem().get();
		}
		throw new NotAnItemException(entity);
	}
	
	public Block getModBlock() throws NotABlockException {
		if(entity instanceof ModBlock) {
			return ((ModBlock) entity).getModBlock().get();
		}
		throw new NotABlockException(entity);
	}
}
