package fr.emmuliette.rune.mod.packets;

import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public interface ISocket {
	
	public Spell getSpell();

	public void setSpell(Spell spell);
	
	public boolean isCooldown();
	public void setCooldown(int cd);
	public int getCooldown();
	public default void tickCooldown() {
		if(isCooldown()) {
			setCooldown(getCooldown() - 1);
		}
	}
	
	public CompoundNBT toNBT();
	public void fromNBT(INBT nbt);
	public void sync(ItemStack itemstack);
	public void sync(ISocket ispell);
	public void sync();
}