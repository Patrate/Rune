package fr.emmuliette.rune.mod.player.capability;

import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.player.grimoire.Grimoire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public interface ICaster {

	public Entity getOwner();
	public void setOwner(Entity owner);
	public Grimoire getGrimoire();
	public void setGrimoire(Grimoire grimoire);

	public float getMana();
	public float getMaxMana();
	public int getManaRegenTick();
	public void setManaCooldown(int cd);
	public void setManaMaxCooldown(int newCooldown);
	public int getManaRegen();
	public void setMana(float mana);
	public void setMaxMana(float maxMana);
	public boolean isCooldown();
	public void setCooldown(int cd);
	public int getCooldown();
	public default void tickCooldown() {
		if(isCooldown()) {
			setCooldown(getCooldown() - 1);
		}
	}
	
	public default void addMana(float mana) {
		setMana(Math.max(0, Math.min(getMana() + mana, getMaxMana())));
	}
	public default void delMana(float mana) throws NotEnoughManaException {
		if(mana > getMana()) {
			throw new NotEnoughManaException(getOwner(), mana, getMana());
		}
		addMana(-mana);
	}
	
	public CompoundNBT toNBT();
	public void fromNBT(INBT nbt);
	public void sync(ServerPlayerEntity player);
	public void sync(ICaster player);
	public void sync();
}