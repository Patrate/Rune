package fr.emmuliette.rune.mod.items.magicItems;

import net.minecraft.entity.Entity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.world.World;

public class MagicTieredItem extends TieredItem implements ManaSource, PowerSource {
	private static final String MANA_SOURCE_TAG = "manasource", TICK = "saved_tick";
	private final float maxMana;
	private final int chargeSpeed;
	private final float power;

	public MagicTieredItem(IItemTier tier, Item.Properties prop) {
		super(tier, prop);
		MagicTier mTier = MagicTier.getMagicTier(tier);
		this.maxMana = mTier.mana;
		this.chargeSpeed = mTier.speed;
		this.power = mTier.power;
	}

	private int getMaxTick(ItemStack item) {
		return (int) getMaxMana(item) * getChargeSpeed(item);
	}

	@Override
	public void inventoryTick(ItemStack item, World world, Entity entity, int jsp, boolean jspnonplus) {
		MagicTieredItem mItem = (MagicTieredItem) item.getItem();
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
		return ((MagicTieredItem) item.getItem()).maxMana;
	}

	@Override
	public int getChargeSpeed(ItemStack item) {
		return ((MagicTieredItem) item.getItem()).chargeSpeed;
	}
}
