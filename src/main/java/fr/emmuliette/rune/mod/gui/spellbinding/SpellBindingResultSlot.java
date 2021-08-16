package fr.emmuliette.rune.mod.gui.spellbinding;

import fr.emmuliette.rune.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SpellBindingResultSlot extends Slot {
	private final SpellBindingInventory craftSlots;
	private final PlayerEntity player;
	private int removeCount;
//	private SpellBindingContainer cnt;

	public SpellBindingResultSlot(SpellBindingContainer cnt, PlayerEntity player, SpellBindingInventory craftSlots,
			IInventory inventaire, int p_i45790_4_, int p_i45790_5_, int p_i45790_6_) {
		super(inventaire, p_i45790_4_, p_i45790_5_, p_i45790_6_);
		this.player = player;
		this.craftSlots = craftSlots;
//		this.cnt = cnt;
	}

	public boolean mayPlace(ItemStack p_75214_1_) {
		return false;
	}

	public ItemStack remove(int p_75209_1_) {
		if (this.hasItem()) {
			this.removeCount += Math.min(p_75209_1_, this.getItem().getCount());
		}

		return super.remove(p_75209_1_);
	}

	protected void onQuickCraft(ItemStack p_75210_1_, int p_75210_2_) {
		this.removeCount += p_75210_2_;
		this.checkTakeAchievements(p_75210_1_);
	}

	protected void onSwapCraft(int p_190900_1_) {
		this.removeCount += p_190900_1_;
	}

	protected void checkTakeAchievements(ItemStack p_75208_1_) {
		if (this.removeCount > 0) {
			p_75208_1_.onCraftedBy(this.player.level, this.player, this.removeCount);
			net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, p_75208_1_,
					this.craftSlots);
		}

		if (this.container instanceof IRecipeHolder) {
			((IRecipeHolder) this.container).awardUsedRecipes(this.player);
		}

		this.removeCount = 0;
	}

	public ItemStack onTake(PlayerEntity player, ItemStack iStack) {
		this.checkTakeAchievements(iStack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(player);
		NonNullList<ItemStack> nonnulllist = player.level.getRecipeManager()
				.getRemainingItemsFor(Registration.SPELLBINDING_RECIPE, this.craftSlots, player.level);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

		// Parsing list of ingredients
		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = this.craftSlots.getItem(i);
			ItemStack itemstack1 = nonnulllist.get(i);
			if (!itemstack.isEmpty()) {
				this.craftSlots.removeItem(i, 1);
				itemstack = this.craftSlots.getItem(i);
			}

			if (!itemstack1.isEmpty()) {
				if (itemstack.isEmpty()) {
					this.craftSlots.setItem(i, itemstack1);
				} else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1)) {
					itemstack1.grow(itemstack.getCount());
					this.craftSlots.setItem(i, itemstack1);
				} else if (!this.player.inventory.add(itemstack1)) {
					this.player.drop(itemstack1, false);
				}
			}
		}

		return iStack;
	}
}
