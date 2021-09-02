package fr.emmuliette.rune.mod.gui.spelliverse;

import fr.emmuliette.rune.mod.containers.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;

public class SpelliverseContainer extends Container {
	
	
	public SpelliverseContainer(int containerId, PlayerInventory playerInventory, PacketBuffer data) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
	}

	public SpelliverseContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.SPELLIVERSE.get(), containerId);
		int baseWidth = 48;
		int baseHeight = 99 + 23;// + 68;

		for (int k = 0; k < 3; ++k) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + k * 9 + 9, baseWidth + l * 18, baseHeight + k * 18));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(playerInventory, i1, baseWidth + i1 * 18, baseHeight + 58));
		}
	}

	public void removed(PlayerEntity player) {
		super.removed(player);
	}

	public boolean stillValid(PlayerEntity player) {
		return true;
	}
}