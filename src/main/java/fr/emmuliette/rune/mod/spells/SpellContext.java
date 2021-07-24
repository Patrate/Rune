package fr.emmuliette.rune.mod.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellContext {
	public static enum TargetType {
		BLOCK, ENTITY, AIR;
	}

	public SpellContext(float power, ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			ItemUseContext itemUseContext) {
		this(power, itemStack, target, world, caster, null, itemUseContext);
	}

	public SpellContext(float power, ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			BlockPos block, ItemUseContext itemUseContext) {
		if (target != null) {
			setLivingEntityContext(power, itemStack, caster, target);
		}
		// target block
		if (itemUseContext != null) {
			setBlockContext(power, itemUseContext);
		}
		// target air
		if (world != null) {
			setAirContext(power, itemStack, world, caster);
		}
		if (block != null) {
			this.block = block;
		}
	}

	private void setBlockContext(float power, ItemUseContext itemUseContext) {
		this.targetType = TargetType.BLOCK;
		this.itemStack = itemUseContext.getItemInHand();
		this.target = null;
		this.world = itemUseContext.getLevel();
		this.caster = itemUseContext.getPlayer();
		this.itemUseContext = itemUseContext;
		this.block = itemUseContext.getClickedPos();
	}

	private void setAirContext(float power, ItemStack itemStack, World world, LivingEntity caster) {
		this.targetType = TargetType.AIR;
		this.itemStack = itemStack;
		this.target = null;
		this.world = world;
		this.caster = caster;
		this.itemUseContext = null;
	}

	private void setLivingEntityContext(float power, ItemStack itemStack, LivingEntity caster, LivingEntity target) {
		this.targetType = TargetType.ENTITY;
		this.itemStack = itemStack;
		this.target = target;
		this.world = caster.level;
		this.caster = caster;
		this.itemUseContext = null;
	}

	private TargetType targetType;
	private ItemStack itemStack;
	private LivingEntity target;
	private World world;
	private LivingEntity caster;
	private ItemUseContext itemUseContext;
	private float power;
	private BlockPos block;

	public float getPower() {
		return power;
	}

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

	public LivingEntity getCaster() {
		return caster;
	}

	public BlockPos getBlock() {
		return block;
	}

	public ItemUseContext getItemUseContext() {
		return itemUseContext;
	}
}
