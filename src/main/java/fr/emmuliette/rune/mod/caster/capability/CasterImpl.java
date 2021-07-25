package fr.emmuliette.rune.mod.caster.capability;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.SyncHandler;
import fr.emmuliette.rune.mod.caster.capability.sync.CasterPacket;
import fr.emmuliette.rune.mod.caster.grimoire.Grimoire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class CasterImpl implements ICaster {
	private final static String GRIMOIRE_KEY = "grimoire", MANA_KEY = "mana", MAXMANA_KEY = "max_mana",
			COOLDOWN_KEY = "cooldown";
	private final static float BASE_MANA = 50;
	private Grimoire grimoire;
	private Entity owner;
	private float currentMana;
	private float maxMana;
	private int currentManaRegen;
	private int manaRegen;
	private int cooldown;

	public CasterImpl(Entity owner) {
		this.owner = owner;
		grimoire = new Grimoire();
		currentMana = BASE_MANA;
		maxMana = BASE_MANA;
		manaRegen = 80;
		currentManaRegen = 0;
		cooldown = 0;
	}

	public CasterImpl() {
		this(null);
	}

	@Override
	public void setOwner(Entity owner) {
		this.owner = owner;
	}

	@Override
	public Entity getOwner() {
		return owner;
	}

	@Override
	public Grimoire getGrimoire() {
		return grimoire;
	}

	@Override
	public void setGrimoire(Grimoire grimoire) {
		this.grimoire = grimoire;
		sync();
	}

	@Override
	public float getMana() {
		return this.currentMana;
	}

	@Override
	public void setMana(float mana) {
		this.currentMana = mana;
		sync();
	}

	@Override
	public float getMaxMana() {
		return this.maxMana;
	}

	@Override
	public void setMaxMana(float maxMana) {
		this.maxMana = maxMana;
		sync();
	}

	@Override
	public int getManaRegenTick() {
		return currentManaRegen;
	}

	@Override
	public int getManaRegen() {
		return manaRegen;
	}

	@Override
	public void setManaCooldown(int cd) {
		currentManaRegen = cd;
		sync();
	}

	@Override
	public void setManaMaxCooldown(int newCooldown) {
		int diff = manaRegen - newCooldown;
		manaRegen = newCooldown;
		currentManaRegen -= diff;
		sync();
	}

	@Override
	public boolean isCooldown() {
		return cooldown > 0;
	}

	@Override
	public void setCooldown(int cd) {
		int previous = this.cooldown;
		this.cooldown = cd;
		if ((previous == 0 && cd != 0) || previous > 0 && cd == 0) {
			sync();
		}
	}

	@Override
	public int getCooldown() {
		return cooldown;
	}

	@Override
	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();

		// this.owner = owner;

		// GRIMOIRE
		if (this.grimoire != null) {
			retour.put(GRIMOIRE_KEY, getGrimoire().toNBT());
		}

		/*
		 * manaRegen = 80; currentManaRegen = 0;
		 */

		retour.put(MANA_KEY, FloatNBT.valueOf(getMana()));
		retour.put(MAXMANA_KEY, FloatNBT.valueOf(getMaxMana()));
		retour.put(COOLDOWN_KEY, IntNBT.valueOf(getCooldown()));

		return retour;
	}

	@Override
	public void fromNBT(INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT cnbt = (CompoundNBT) nbt;

			// GRIMOIRE
			if (cnbt.contains(GRIMOIRE_KEY)) {
				try {
					this.setGrimoire(Grimoire.fromNBT(cnbt.get(GRIMOIRE_KEY)));
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
						| SecurityException | RunePropertiesException e) {
					e.printStackTrace();
				}
			}

			// CURRENT MANA
			if (cnbt.contains(MANA_KEY)) {
				this.setMana(cnbt.getFloat(MANA_KEY));
			}

			// MAX MANA
			if (cnbt.contains(MAXMANA_KEY)) {
				this.setMaxMana(cnbt.getFloat(MAXMANA_KEY));
			}

			// GLOBAL COOLDOWN
			if (cnbt.contains(COOLDOWN_KEY)) {
				this.setCooldown(cnbt.getInt(COOLDOWN_KEY));
			}
		}
	}

	@Override
	public void sync(ServerPlayerEntity player) {
		player.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(c -> this.sync(c));
	}

	@Override
	public void sync(ICaster player) {
		this.grimoire = player.getGrimoire();
		this.currentMana = player.getMana();
		this.maxMana = player.getMaxMana();
		this.currentManaRegen = player.getManaRegenTick();
		this.manaRegen = player.getManaRegen();
	}

	@Override
	public void sync() {
		if (owner instanceof ServerPlayerEntity) {
			SyncHandler.sendTo(new CasterPacket(this.toNBT()), (ServerPlayerEntity) owner);
		}
	}
}