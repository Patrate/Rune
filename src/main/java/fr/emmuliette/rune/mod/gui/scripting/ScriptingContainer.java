package fr.emmuliette.rune.mod.gui.scripting;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fr.emmuliette.rune.mod.blocks.ModBlocks;
import fr.emmuliette.rune.mod.containers.ModContainers;
import fr.emmuliette.rune.mod.sync.SyncHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

public class ScriptingContainer extends Container {
	private final IWorldPosCallable access;
	private HashSet<Point> drawing;
	private PlayerInventory playerInventory;
	private final CraftResultInventory resultSlots = new CraftResultInventory();
	private final CraftingInventory craftSlots = new CraftingInventory(this, 2, 1);

	public ScriptingContainer(int containerId, PlayerInventory playerInventory, PacketBuffer data) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
	}

	public ScriptingContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.SCRIPTING.get(), containerId);
		this.access = postCall;
		this.playerInventory = playerInventory;
		this.addSlot(new Slot(this.craftSlots, 0, 126, 9) {
			@Override
			public boolean mayPlace(ItemStack itemstack) {
				return itemstack.getItem() == Items.PAPER;
			}
		});

		this.addSlot(new Slot(this.craftSlots, 1, 146, 9) {
			@Override
			public boolean mayPlace(ItemStack itemstack) {
				return itemstack.getItem() == Items.LAPIS_LAZULI;
			}
		});

		this.addSlot(new Slot(this.resultSlots, 0, 86, 9) {
			@Override
			public ItemStack onTake(PlayerEntity player, ItemStack item) {
				drawing.clear();
				craftSlots.removeItem(0, 1);
				craftSlots.removeItem(1, 1);
				return super.onTake(player, item);
			}

			@Override
			public boolean mayPickup(PlayerEntity player) {
				if (craftSlots.countItem(Items.LAPIS_LAZULI) == 0 || craftSlots.countItem(Items.PAPER) == 0)
					return false;
				return super.mayPickup(player);
			}

			@Override
			public boolean mayPlace(ItemStack item) {
				return false;
			}
		});

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

	public Integer[][] toMatrix() {
		int rmin = Integer.MAX_VALUE, rmax = 0, cmin = Integer.MAX_VALUE, cmax = 0;
		for (Point p : drawing) {
			if (p.x / ScriptingScreen.PIX_SIZE < rmin)
				rmin = p.x / ScriptingScreen.PIX_SIZE;
			if (p.x / ScriptingScreen.PIX_SIZE > rmax)
				rmax = p.x / ScriptingScreen.PIX_SIZE;
			if (p.y / ScriptingScreen.PIX_SIZE < cmin)
				cmin = p.y / ScriptingScreen.PIX_SIZE;
			if (p.y / ScriptingScreen.PIX_SIZE > cmax)
				cmax = p.y / ScriptingScreen.PIX_SIZE;
		}
		int row = rmax - rmin + 1, col = cmax - cmin + 1;
		Integer[][] matrix = new Integer[row][col];
		for (int x = 0; x < row; x++) {
			Arrays.fill(matrix[x], 0);
		}
		for (Point p : drawing) {
			matrix[(p.x / ScriptingScreen.PIX_SIZE) - rmin][(p.y / ScriptingScreen.PIX_SIZE) - cmin] = 1;
		}
		return matrix;
	}

	public void runMatrix() {
		if (drawing.isEmpty()) {
			System.out.println("Empty drawing");
			return;
		}
		SyncHandler.sendToServer(new CGetRunePacket(drawing));
	}

	public void runMatrixServer(Set<Point> drawing2) {
		setDrawing(drawing2);
		if (drawing.isEmpty()) {
			System.out.println("Empty drawing");
			return;
		}
		this.access.execute((world, blockPos) -> {
			runMatrix(this, world, this.playerInventory.player, toMatrix());
		});
	}

	public static void runMatrix(ScriptingContainer container, World world, PlayerEntity player,
			Integer[][] otherDrawing) {
		if (!world.isClientSide) {
			Item runeItem = RuneUtils.testMatrix(otherDrawing);
			if (runeItem != null) {
				ItemStack itemstack = new ItemStack(runeItem);
				container.resultSlots.setItem(0, itemstack);
				// player.inventory.add(itemstack);
			}
		}
	}

	protected void removeSlot(int index) {
		this.slots.remove(index);
	}

	public boolean stillValid(PlayerEntity player) {
		return stillValid(this.access, player, ModBlocks.SCRIPTING_BLOCK.get());
	}

	public Set<Point> getPoints() {
		return drawing;
	}

	public void addPoint(double mouseX, double mouseY, int pixSize) {
		int pX = ((int) mouseX) - ((int) mouseX) % pixSize;
		int pY = ((int) mouseY) - ((int) mouseY) % pixSize;
		drawing.add(new Point(pX, pY));
	}

	public void removePoint(double mouseX, double mouseY, int pixSize) {
		int pX = ((int) mouseX) - ((int) mouseX) % pixSize;
		int pY = ((int) mouseY) - ((int) mouseY) % pixSize;
		drawing.remove(new Point(pX, pY));
	}

	private void setDrawing(Set<Point> drawing2) {
		this.drawing.clear();
		this.drawing.addAll(drawing2);
	}

	public void clear() {
		this.drawing.clear();
	}

	@Override
	protected void clearContainer(PlayerEntity player, World world, IInventory inventory) {
		player.inventory.placeItemBackInInventory(world, craftSlots.removeItemNoUpdate(0));
		player.inventory.placeItemBackInInventory(world, craftSlots.removeItemNoUpdate(1));
		super.clearContainer(player, world, inventory);
	}

}