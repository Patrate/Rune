package fr.emmuliette.rune.mod.gui.grimoire;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireSpellSlot extends Slot {
//	private ItemStack spellitem;

	public GrimoireSpellSlot(IInventory iInventory, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(iInventory, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		// this.spell = spell;
//		spellitem = GrimoireSpellItem.getGrimoireSpell(grimoire, spellName);
//		this.set(spellitem);
	}

    public boolean mayPickup(PlayerEntity p_82869_1_) {
       if (super.mayPickup(p_82869_1_) && this.hasItem()) {
          return this.getItem().getTagElement("CustomCreativeLock") == null;
       } else {
          return !this.hasItem();
		}
	}

//	@Override
//	public boolean mayPlace(ItemStack iStack) {
//		return false;
//	}
//
//	@Override
//	public void set(ItemStack p_75215_1_) {
//		// TODO throw exception >=(
//	}
//
//	@Override
//	public ItemStack remove(int p_75209_1_) {
//		// TODO throw exception
////		return super.remove(p_75209_1_);
//		return spellitem.copy();
//	}
//
//	@Override
//	public ItemStack onTake(PlayerEntity player, ItemStack iStack) {
//		// this.setChanged();
//		System.out.println("TAKING ?!");
//		return iStack.copy();
//	}

}
