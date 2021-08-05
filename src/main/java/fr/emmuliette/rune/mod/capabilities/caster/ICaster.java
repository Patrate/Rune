package fr.emmuliette.rune.mod.capabilities.caster;

import fr.emmuliette.rune.exception.NotEnoughManaException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public interface ICaster {

	public Entity getOwner();

	public void setOwner(Entity owner);

	public Grimoire getGrimoire();

	public void setGrimoire(Grimoire grimoire);

	public float getPower();

	public void setPower(float power);

	public float getMana();

	public float getMaxMana();

	public int getManaRegenTick();

	public void setManaCooldown(int cd);

	public void setManaRegen(int newCooldown);

	public int getManaRegen();

	void setMana(float mana);

	void setMaxMana(float maxMana);

	public boolean isCooldown();

	public void setCooldown(int cd);

	public int getCooldown();

	float getManaInternal();

	float getMaxManaInternal();

	float getPowerInternal();

	public default void tickCooldown() {
		if (isCooldown()) {
			setCooldown(getCooldown() - 1);
		}
	}

	public void delMana(float cost) throws NotEnoughManaException;

	public default void addMana(float mana) {
		setMana(Math.max(0, Math.min(getManaInternal() + mana, getMaxManaInternal())));
	}

	public default void delManaInternal(float cost) throws NotEnoughManaException {
		if (cost > getManaInternal()) {
			throw new NotEnoughManaException(getOwner(), cost, getManaInternal());
		}
		addMana(-cost);
	}

	public CompoundNBT toNBT();

	public void fromNBT(INBT nbt);

	public void sync(ServerPlayerEntity player);

	public void sync(ICaster player);

	public void sync();
}