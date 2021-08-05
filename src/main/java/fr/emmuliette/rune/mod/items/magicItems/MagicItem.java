package fr.emmuliette.rune.mod.items.magicItems;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.world.World;

public class MagicItem extends Item implements ManaSource, PowerSource {
	private static final String MANA_SOURCE_TAG = "manasource", TICK = "saved_tick";
	private final float maxMana;
	private final int chargeSpeed;
	private final float power;

	public MagicItem(MagicItemProperties prop) {
		super(prop);
		this.maxMana = prop.maxMana;
		this.chargeSpeed = prop.chargeSpeed;
		this.power = prop.power;
	}

	private int getMaxTick(ItemStack item) {
		return (int) getMaxMana(item) * getChargeSpeed(item);
	}

	@Override
	public void inventoryTick(ItemStack item, World world, Entity entity, int jsp, boolean jspnonplus) {
		MagicItem mItem = (MagicItem) item.getItem();
		CompoundNBT tags = item.getOrCreateTagElement(MANA_SOURCE_TAG);
		int tick = 0;
		if (tags.contains(TICK))
			tick = tags.getInt(TICK);
		if (tick < mItem.getMaxTick(item)) {
			mItem.setManaTick(item, tick + 1);
			tags.put(TICK, IntNBT.valueOf(tick + 1));
		}
		super.inventoryTick(item, world, entity, jsp, jspnonplus);
	}

	@Override
	public float getPower(ItemStack item) {
		return power;
	}

	@Override
	public float getMana(ItemStack item) {
		CompoundNBT tags = item.getOrCreateTagElement(MANA_SOURCE_TAG);
		if (!tags.contains(TICK))
			tags.put(TICK, IntNBT.valueOf(0));
		int manaTick = tags.getInt(TICK);
		return manaTick / getChargeSpeed(item);
	}

	@Override
	public boolean useMana(ItemStack item, float amount) {
		float currentMana = getMana(item);
		if (amount > currentMana)
			return false;

		delManaTick(item, (int) amount * this.getChargeSpeed(item));
		return true;
	}

	protected void delManaTick(ItemStack item, int amount) {
		CompoundNBT tags = item.getOrCreateTagElement(MANA_SOURCE_TAG);
		tags.putInt(TICK, tags.getInt(TICK) - amount);
//		manaItems.add(item);
	}

	protected void setManaTick(ItemStack item, int amount) {
		CompoundNBT tags = item.getOrCreateTagElement(MANA_SOURCE_TAG);
		tags.putInt(TICK, amount);
	}

	protected int getManaTick(ItemStack item) {
		CompoundNBT tags = item.getOrCreateTagElement(MANA_SOURCE_TAG);
		if (!tags.contains(TICK))
			tags.put(TICK, IntNBT.valueOf(0));
		return tags.getInt(TICK);
	}

	@Override
	public float getMaxMana(ItemStack item) {
		return ((MagicItem) item.getItem()).maxMana;
	}

	@Override
	public int getChargeSpeed(ItemStack item) {
		return ((MagicItem) item.getItem()).chargeSpeed;
	}

	public static class MagicItemProperties extends Item.Properties {
		private float power = 0f;
		private float maxMana = 10f;
		private int chargeSpeed = 40;

		public MagicItem.MagicItemProperties chargeSpeed(int chargeSpeed) {
			this.chargeSpeed = chargeSpeed;
			return this;
		}

		public MagicItem.MagicItemProperties power(float power) {
			this.power = power;
			return this;
		}

		public MagicItem.MagicItemProperties maxMana(float maxMana) {
			this.maxMana = maxMana;
			return this;
		}
	}
}
