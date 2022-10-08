package fr.emmuliette.rune.mod.gui.scripting;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import fr.emmuliette.rune.mod.blocks.ModBlocks;
import fr.emmuliette.rune.mod.containers.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;

public class ScriptingContainer extends Container {
	private final IWorldPosCallable access;
	private HashSet<Point> drawing;

	public ScriptingContainer(int containerId, PlayerInventory playerInventory, PacketBuffer data) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
	}

	public ScriptingContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.SCRIPTING.get(), containerId);
		this.access = postCall;
		// Inventory
		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		// Shortcuts
		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
		}
		drawing = new HashSet<Point>();
	}

	protected void removeSlot(int index) {
		this.slots.remove(index);
	}

	public boolean stillValid(PlayerEntity player) {
		return stillValid(this.access, player, ModBlocks.SCRIPTING_BLOCK.get());
	}
	
	public Set<Point> getPoints(){
		return drawing;
	}

	public void addPoint(double mouseX, double mouseY, int pixSize) {
		int pX = ((int) mouseX) - ((int) mouseX) % pixSize;
		int pY = ((int) mouseY) - ((int) mouseY) % pixSize;
		drawing.add(new Point(pX, pY));
	}
}