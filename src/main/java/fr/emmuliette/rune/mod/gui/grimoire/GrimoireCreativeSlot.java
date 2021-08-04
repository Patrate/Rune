package fr.emmuliette.rune.mod.gui.grimoire;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireCreativeSlot extends Slot {
	private final Slot target;

	public GrimoireCreativeSlot(Slot p_i229959_1_, int p_i229959_2_, int p_i229959_3_, int p_i229959_4_) {
		super(p_i229959_1_.container, p_i229959_2_, p_i229959_3_, p_i229959_4_);
		this.target = p_i229959_1_;
	}

	public ItemStack onTake(PlayerEntity p_190901_1_, ItemStack p_190901_2_) {
		return this.target.onTake(p_190901_1_, p_190901_2_);
	}

	public boolean mayPlace(ItemStack p_75214_1_) {
		return this.target.mayPlace(p_75214_1_);
	}

	public ItemStack getItem() {
		return this.target.getItem();
	}

	public boolean hasItem() {
		return this.target.hasItem();
	}

	public void set(ItemStack p_75215_1_) {
		this.target.set(p_75215_1_);
	}

	public void setChanged() {
		this.target.setChanged();
	}

	public int getMaxStackSize() {
		return this.target.getMaxStackSize();
	}

	public int getMaxStackSize(ItemStack p_178170_1_) {
		return this.target.getMaxStackSize(p_178170_1_);
	}

	@Nullable
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return this.target.getNoItemIcon();
	}

	public ItemStack remove(int p_75209_1_) {
		return this.target.remove(p_75209_1_);
	}

	public boolean isActive() {
		return this.target.isActive();
	}

	public boolean mayPickup(PlayerEntity p_82869_1_) {
		return this.target.mayPickup(p_82869_1_);
	}

	@Override
	public int getSlotIndex() {
		return this.target.getSlotIndex();
	}

	@Override
	public boolean isSameInventory(Slot other) {
		return this.target.isSameInventory(other);
	}

	@Override
	public Slot setBackground(ResourceLocation atlas, ResourceLocation sprite) {
		this.target.setBackground(atlas, sprite);
		return this;
	}
}