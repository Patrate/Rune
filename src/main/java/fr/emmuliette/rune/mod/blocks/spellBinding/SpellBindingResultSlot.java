package fr.emmuliette.rune.mod.blocks.spellBinding;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class SpellBindingResultSlot extends Slot {
	private final SpellBindingInventory craftSlots;
	private final PlayerEntity player;
	private int removeCount;

	public SpellBindingResultSlot(PlayerEntity p_i45790_1_, SpellBindingInventory p_i45790_2_, IInventory p_i45790_3_,
			int p_i45790_4_, int p_i45790_5_, int p_i45790_6_) {
		super(p_i45790_3_, p_i45790_4_, p_i45790_5_, p_i45790_6_);
		this.player = p_i45790_1_;
		this.craftSlots = p_i45790_2_;
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

	public <C extends IInventory, T extends IRecipe<C>> NonNullList<ItemStack> getRemainingItemsFor(
			IRecipeType<ICraftingRecipe> crafting, SpellBindingInventory craftSlots2, World p_215369_3_) {
		return null;
		// TODO
		/*
		 * Optional<T> optional = this.getRecipeFor(crafting, craftSlots2, p_215369_3_);
		 * if (optional.isPresent()) { return
		 * optional.get().getRemainingItems(craftSlots2); } else {
		 * NonNullList<ItemStack> nonnulllist =
		 * NonNullList.withSize(craftSlots2.getContainerSize(), ItemStack.EMPTY);
		 * 
		 * for(int i = 0; i < nonnulllist.size(); ++i) { nonnulllist.set(i,
		 * craftSlots2.getItem(i)); }
		 * 
		 * return nonnulllist; }
		 */
	}

	public ItemStack onTake(PlayerEntity p_190901_1_, ItemStack p_190901_2_) {
		this.checkTakeAchievements(p_190901_2_);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(p_190901_1_);
		// NonNullList<ItemStack> nonnulllist =
		// getRemainingItemsFor(IRecipeType.CRAFTING, this.craftSlots,
		// p_190901_1_.level);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
		// TODO
		// nonnulllist est null
		/*
		 * for(int i = 0; i < nonnulllist.size(); ++i) { ItemStack itemstack =
		 * this.craftSlots.getItem(i); ItemStack itemstack1 = nonnulllist.get(i); if
		 * (!itemstack.isEmpty()) { this.craftSlots.removeItem(i, 1); itemstack =
		 * this.craftSlots.getItem(i); }
		 * 
		 * if (!itemstack1.isEmpty()) { if (itemstack.isEmpty()) {
		 * this.craftSlots.setItem(i, itemstack1); } else if
		 * (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack,
		 * itemstack1)) { itemstack1.grow(itemstack.getCount());
		 * this.craftSlots.setItem(i, itemstack1); } else if
		 * (!this.player.inventory.add(itemstack1)) { this.player.drop(itemstack1,
		 * false); } } }
		 */
		return p_190901_2_;
	}
}
