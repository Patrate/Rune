package fr.emmuliette.rune.mod.items.wand;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE)
public class WandItem extends Item implements ManaSource {
	private static final String MANA_SOURCE_TAG = "manasource", SAVED_TICK = "saved_tick";
	private static int tickCount = 0;
	private final float maxMana;
	private final int chargeSpeed;

	public WandItem(WandProperties prop) {
		super(prop);
		this.maxMana = prop.maxMana;
		this.chargeSpeed = prop.chargeSpeed;
	}

	@SubscribeEvent
	public static void worldTickEvent(TickEvent.WorldTickEvent event) {
		tickCount++;
	}

	@Override
	public float getMana(ItemStack item) {
		CompoundNBT tags = item.getOrCreateTagElement(MANA_SOURCE_TAG);
		if (!tags.contains(SAVED_TICK))
			tags.put(SAVED_TICK, IntNBT.valueOf(tickCount));
		int savedTick = tags.getInt(SAVED_TICK);
		if (savedTick > tickCount) {
			tags.put(SAVED_TICK, IntNBT.valueOf(tickCount));
			savedTick = tickCount;
		}
		int chargeSpeed = getChargeSpeed(item);
		if (chargeSpeed == 0) {
			// TODO throw error
			return 0f;
		}

		float mana = Math.max(0, Math.min((tickCount - savedTick) / getChargeSpeed(item), getMaxMana(item)));
		return mana;
	}

	@Override
	public boolean useMana(ItemStack item, float amount) {
		float currentMana = getMana(item);
		if (amount < currentMana)
			return false;
		int savedTick = getSavedTick(item);
		int chargeSpeed = getChargeSpeed(item);
		int tickCost = (int) (chargeSpeed * amount);
		int maxTick = (int) Math.ceil(Math.max(savedTick, tickCount - (getMaxMana(item) * chargeSpeed)));
		setSavedTick(item, maxTick + tickCost);
		return true;
	}

	protected void setSavedTick(ItemStack item, int amount) {
		CompoundNBT tags = item.getOrCreateTagElement(MANA_SOURCE_TAG);
		tags.putInt(SAVED_TICK, amount);
	}

	protected int getSavedTick(ItemStack item) {
		CompoundNBT tags = item.getOrCreateTagElement(MANA_SOURCE_TAG);
		if (!tags.contains(SAVED_TICK))
			tags.put(SAVED_TICK, IntNBT.valueOf(tickCount));
		int savedTick = tags.getInt(SAVED_TICK);
		if (savedTick > tickCount) {
			tags.put(SAVED_TICK, IntNBT.valueOf(tickCount));
			savedTick = tickCount;
		}
		return savedTick;
	}

	@Override
	public float getMaxMana(ItemStack item) {
		return ((WandItem) item.getItem()).maxMana;
	}

	@Override
	public int getChargeSpeed(ItemStack item) {
		return ((WandItem) item.getItem()).chargeSpeed;
	}

	public static class WandProperties extends Item.Properties {
		private float maxMana = 10f;
		private int chargeSpeed = 40;

		public WandItem.WandProperties chargeSpeed(int chargeSpeed) {
			this.chargeSpeed = chargeSpeed;
			return this;
		}

		public WandItem.WandProperties maxMana(float maxMana) {
			this.maxMana = maxMana;
			return this;
		}
	}

}
