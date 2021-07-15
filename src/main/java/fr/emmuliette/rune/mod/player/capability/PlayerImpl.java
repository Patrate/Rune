package fr.emmuliette.rune.mod.player.capability;

import fr.emmuliette.rune.mod.player.capability.sync.PlayerHandler;
import fr.emmuliette.rune.mod.player.capability.sync.PlayerPacket;
import fr.emmuliette.rune.mod.player.grimoire.Grimoire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;

public class PlayerImpl implements IPlayer {
	private final static String GRIMOIRE_KEY = "grimoire", MANA_KEY = "mana", MAXMANA_KEY = "max_mana";
	private final static float BASE_MANA = 20;
    private Grimoire grimoire;
    private Entity owner;
    private float currentMana;
    private float maxMana;
    private int currentManaRegen;
    private int manaRegen;

    public PlayerImpl(Entity owner) {
    	this.owner = owner;
    	grimoire = new Grimoire();
    	currentMana = BASE_MANA;
    	maxMana = BASE_MANA;
    	manaRegen = 80;
    	currentManaRegen = 0;
    }
    
    public PlayerImpl() {
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
	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		
		//this.owner = owner;
		
		// GRIMOIRE
		if(this.grimoire != null) {
			retour.put(GRIMOIRE_KEY, getGrimoire().toNBT());
		}

    	
    	/*manaRegen = 80;
    	currentManaRegen = 0;*/

		retour.put(MANA_KEY, FloatNBT.valueOf(getMana()));
		retour.put(MAXMANA_KEY, FloatNBT.valueOf(getMaxMana()));
		
		return retour;
	}
	@Override
	public void fromNBT(INBT nbt) {
		if(nbt instanceof CompoundNBT) {
			CompoundNBT cnbt = (CompoundNBT) nbt;
			
			// GRIMOIRE
			if(cnbt.contains(GRIMOIRE_KEY)) {
				this.setGrimoire(Grimoire.fromNBT(cnbt.get(GRIMOIRE_KEY)));
			}
			
			// CURRENT MANA
			if(cnbt.contains(MANA_KEY)) {
				this.setMana(cnbt.getFloat(MANA_KEY));
			}
			
			// MAX MANA
			if(cnbt.contains(MAXMANA_KEY)) {
				this.setMaxMana(cnbt.getFloat(MAXMANA_KEY));
			}
		}
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
	public void sync(ServerPlayerEntity player) {
		player.getCapability(PlayerCapability.PLAYER_CAPABILITY).ifPresent(c -> this.sync(c));
	}
	
	@Override
	public void sync(IPlayer player) {
		this.grimoire = player.getGrimoire();
	    this.currentMana = player.getMana();
	    this.maxMana = player.getMaxMana();
	    this.currentManaRegen = player.getManaRegenTick();
	    this.manaRegen = player.getManaRegen();
	}
	
	@Override
	public void sync() {
		if(owner instanceof ServerPlayerEntity) {
			PlayerHandler.sendTo(new PlayerPacket(this.toNBT()), (ServerPlayerEntity) owner);
		}
	}
	
	/*@Override
	public String toString() {
		return "mana: " + this.getMana() + "/" + this.currentMana;
	}*/
}