package fr.emmuliette.rune.mod.spells;

import java.util.Set;

import fr.emmuliette.rune.mod.spells.component.effectComponent.ChannelEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellContext {
	public static enum TargetType {
		BLOCK, ENTITY, AIR;
	}

	private TargetType targetType;
	private ItemStack itemStack;
	private LivingEntity target;
	private World world;
	private Entity currentCaster;
	private Entity originalCaster;
	private ItemUseContext itemUseContext;
	private float power;
	private BlockPos block;
	private Set<ChannelEffect> channeled;
	private Direction casterFacing;

	public SpellContext(float power, ItemStack itemStack, LivingEntity target, World world, Entity caster,
			ItemUseContext itemUseContext) {
		this(power, itemStack, target, world, caster, null, itemUseContext);
	}

	public SpellContext(float power, ItemStack itemStack, LivingEntity target, World world, Entity caster,
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
		if (this.getOriginalCaster() == null) {
			this.originalCaster = this.currentCaster;
		}
		this.casterFacing = this.currentCaster.getDirection();
	}

	private void setBlockContext(float power, ItemUseContext itemUseContext) {
		this.targetType = TargetType.BLOCK;
		this.itemStack = itemUseContext.getItemInHand();
		this.target = null;
		this.world = itemUseContext.getLevel();
		this.currentCaster = itemUseContext.getPlayer();
		this.itemUseContext = itemUseContext;
		this.block = itemUseContext.getClickedPos();
	}

	private void setAirContext(float power, ItemStack itemStack, World world, Entity caster) {
		this.targetType = TargetType.AIR;
		this.itemStack = itemStack;
		this.target = null;
		this.world = world;
		this.currentCaster = caster;
		this.itemUseContext = null;
	}

	private void setLivingEntityContext(float power, ItemStack itemStack, Entity caster, LivingEntity target) {
		this.targetType = TargetType.ENTITY;
		this.itemStack = itemStack;
		this.target = target;
		this.world = caster.level;
		this.currentCaster = caster;
		this.itemUseContext = null;
	}

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

	public Entity getCaster() {
		return currentCaster;
	}

	public BlockPos getBlock() {
		return block;
	}

	public ItemUseContext getItemUseContext() {
		return itemUseContext;
	}

	public void setPower(float power) {
		this.power = power;
	}

	public void setTarget(LivingEntity target) {
		this.target = target;
	}

	public void setBlock(BlockPos block) {
		this.block = block;
	}

	public void setCurrentCaster(Entity caster) {
		this.currentCaster = caster;
	}

	public Entity getOriginalCaster() {
		return originalCaster;
	}

	public boolean isChanneling() {
		return channeled != null;
	}

	public void setChanneled(Set<ChannelEffect> effects) {
		this.channeled = effects;
	}

	public void addChanneledEffect(ChannelEffect effect) {
		if (isChanneling())
			this.channeled.add(effect);
	}

	public Set<ChannelEffect> getChanneledEffects() {
		return this.channeled;
	}

	public Direction getCasterFacing() {
		return casterFacing;
	}

	public void setCasterFacing(Direction casterFacing) {
		this.casterFacing = casterFacing;
	}
}
