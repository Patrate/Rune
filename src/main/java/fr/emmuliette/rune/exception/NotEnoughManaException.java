package fr.emmuliette.rune.exception;

import fr.emmuliette.rune.mod.player.capability.ICaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class NotEnoughManaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1936192834327233958L;

	public NotEnoughManaException(Entity owner, float removing, float current) {
		super("Entity " + owner + " only have " + current + " mana, trying to remove " + removing);
	}
	
	public NotEnoughManaException(ItemStack spell, float manaCost, PlayerEntity player, ICaster cap) {
		super("Player " + player.getName().getString() + " trying to cast " + spell.getDisplayName().getString() + " costing " + manaCost + " but has only " + cap.getMana());
	}
}
