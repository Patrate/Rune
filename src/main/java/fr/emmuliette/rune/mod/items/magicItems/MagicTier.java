package fr.emmuliette.rune.mod.items.magicItems;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;

public enum MagicTier {
	WOOD(ItemTier.WOOD, 50, 5, 0), STONE(ItemTier.STONE, 150, 7, 0), IRON(ItemTier.IRON, 550, 9, 1),
	GOLD(ItemTier.GOLD, 100, 20, 3), DIAMOND(ItemTier.DIAMOND, 1000, 12, 2), NETHERITE(ItemTier.NETHERITE, 1500, 15, 3);

	private IItemTier tier;
	public final float mana, power;
	public final int speed;

	private MagicTier(IItemTier tier, float mana, int speed, float power) {
		this.tier = tier;
		this.mana = mana;
		this.speed = speed;
		this.power = power;
	}

	public static MagicTier getMagicTier(IItemTier tier) {
		for (MagicTier mt : MagicTier.values()) {
			if (mt.tier == tier)
				return mt;
		}
		RuneMain.LOGGER.info("Magic tier for " + tier + " doesn't exist");
		return null;
	}
}
