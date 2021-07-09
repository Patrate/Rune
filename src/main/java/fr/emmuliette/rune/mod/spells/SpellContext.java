package fr.emmuliette.rune.mod.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.world.World;

public class SpellContext {
	public static enum TargetType {
		BLOCK, ENTITY, AIR;
	}
	
	public SpellContext(ItemStack itemStack, LivingEntity target, World world, PlayerEntity player, ItemUseContext itemUseContext) {
		if(target!= null) {
			setLivingEntityContext(itemStack, player, target);
		}
		// target block
		if(itemUseContext != null) {
			setBlockContext(itemUseContext);
		}
		// target air
		if(world != null) {
			setAirContext(world, player);
		}
	}
	
	
	private void setBlockContext(ItemUseContext itemUseContext) {
		this.targetType = TargetType.BLOCK;
		this.itemStack = itemUseContext.getItemInHand();
		this.target = null;
		this.world = itemUseContext.getLevel();;
		this.player = itemUseContext.getPlayer();
		this.itemUseContext = itemUseContext;
	}
	private void setAirContext(World world, PlayerEntity player) {
		this.targetType = TargetType.AIR;
		this.itemStack = null;
		this.target = null;
		this.world = world;
		this.player = player;
		this.itemUseContext = null;
	}
	private void setLivingEntityContext(ItemStack itemStack, PlayerEntity player, LivingEntity target) {
		this.targetType = TargetType.ENTITY;
		this.itemStack = itemStack;
		this.target = target;
		this.world = player.level;
		this.player = player;
		this.itemUseContext = null;
	}

	private TargetType targetType;
	private ItemStack itemStack;
	private LivingEntity target;
	private World world;
	private PlayerEntity player;
	private ItemUseContext itemUseContext;

	public TargetType getTargetType() {
		return targetType;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}

	public LivingEntity getTarget() {
		return target;
	}

	public World getWorld() {
		return world;
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public ItemUseContext getItemUseContext() {
		return itemUseContext;
	}
}
