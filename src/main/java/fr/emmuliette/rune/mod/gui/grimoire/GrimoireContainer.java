package fr.emmuliette.rune.mod.gui.grimoire;

import fr.emmuliette.rune.mod.containers.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;

public class GrimoireContainer extends Container {
//	private GrimoireInventory spellSlots;
//	private final IWorldPosCallable access;
//	private Grimoire grimoire;
//	private ISpell selectedSpell;
//	private int spellCount;

	public GrimoireContainer(int containerId, PlayerInventory playerInventory, PacketBuffer data) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
	}

	public GrimoireContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.GRIMOIRE.get(), containerId);
		int wx = 8;
		int he = 99;

		for (int k = 0; k < 3; ++k) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + k * 9 + 9, wx + l * 18, he + k * 18));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(playerInventory, i1, wx + i1 * 18, he + 58));
		}
	}

	public void removed(PlayerEntity player) {
		super.removed(player);
	}

	public boolean stillValid(PlayerEntity player) {
		return true;
	}
}