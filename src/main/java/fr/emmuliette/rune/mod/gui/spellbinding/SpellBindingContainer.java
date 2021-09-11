package fr.emmuliette.rune.mod.gui.spellbinding;

import java.util.Optional;

import fr.emmuliette.rune.mod.blocks.ModBlocks;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingRecipe;
import fr.emmuliette.rune.mod.containers.ModContainers;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.mod.specialRecipes.SpellRecipe;
import fr.emmuliette.rune.mod.sync.SyncHandler;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpellBindingContainer extends Container {
	private final int WIDTH = 5;
	private final int HEIGHT = 3;
	private final SpellBindingInventory craftSlots = new SpellBindingInventory(this, WIDTH, HEIGHT);

	private final CraftResultInventory resultSlots = new CraftResultInventory();
	private final IWorldPosCallable access;
	private final PlayerEntity player;
	String errorMessage;

	public SpellBindingContainer(int containerId, PlayerInventory playerInventory, PacketBuffer data) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
	}

	public SpellBindingContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.SPELLBINDING.get(), containerId);
		this.errorMessage = null;
		this.access = postCall;
		this.player = playerInventory.player;
		// Result slot
		this.addSlot(new SpellBindingResultSlot(this, playerInventory.player, this.getCraftSlots(), this.resultSlots, 0,
				138, 31));

		// Book or parchment slot
		this.addSlot(new Slot(this.craftSlots, 1, 138, 62) {
			@Override
			public boolean mayPlace(ItemStack iStack) {
				return iStack.getItem() == Items.BOOK || iStack.getItem() == Items.PAPER
						|| iStack.getItem() == ModItems.EMPTY_SOCKET.get();
			}
		});

		// Crafting slots

		for (int i = 0; i < HEIGHT; ++i) {
			for (int j = 0; j < WIDTH; ++j) {
				this.addSlot(new SpellBindingRuneSlot(this.craftSlots, 2 + j + i * WIDTH, 11 + j * 19, 16 + i * 19));
			}
		}

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
	}

	protected void removeSlot(int index) {
		this.slots.remove(index);
	}

	protected static void slotChangedCraftingGrid(int containerId, World world, PlayerEntity player,
			SpellBindingInventory spellBindingInventory, CraftResultInventory craftingResultInventory) {
		if (!world.isClientSide) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
			ItemStack itemstack = ItemStack.EMPTY;
			if (!spellBindingInventory.getSpellName().isEmpty()) {
				Optional<SpellBindingRecipe> optional = world.getServer().getRecipeManager()
						.getRecipeFor(Registration.SPELLBINDING_RECIPE, spellBindingInventory, world);
				if (optional.isPresent()) {
					SpellBindingRecipe icraftingrecipe = optional.get();
					if (craftingResultInventory.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
						itemstack = icraftingrecipe.assemble(spellBindingInventory);
					}
				}
			}
			craftingResultInventory.setItem(0, itemstack);
			serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
		}
	}

	public void propertyChanged() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt(CSpellBindingSlotPacket.CONTAINER_ID_NBT, this.containerId);
		nbt.put(CSpellBindingSlotPacket.PROPERTIES_NBT, this.craftSlots.getProperties());
		nbt.putString(CSpellBindingSlotPacket.NAME_NBT, this.getSpellName());
//		System.out.println("Sending to server: " + nbt.toString());
		SyncHandler.sendToServer(new CSpellBindingSlotPacket(nbt));
		updateErrorMessage();
	}

	public void slotChangedCraftingGrid() {
		slotChangedCraftingGrid(this.containerId, this.player.level, this.player, this.craftSlots, this.resultSlots);
		updateErrorMessage();
	}

	public void updateProperties(INBT nbt) {
		this.craftSlots.setProperties(nbt);
		this.access.execute((world, blockPos) -> {
			slotChangedCraftingGrid(this.containerId, world, this.player, this.craftSlots, this.resultSlots);
		});
		updateErrorMessage();
	}

	public void slotsChanged(IInventory slot) {
		this.access.execute((world, blockPos) -> {
			slotChangedCraftingGrid(this.containerId, world, this.player, this.craftSlots, this.resultSlots);
		});
		updateErrorMessage();
	}

	private void updateErrorMessage() {
		if(!this.player.level.isClientSide)
			return;
		if (this.craftSlots.getSpellName().isEmpty()) {
			errorMessage = "Must have a spell name";
		} else {
			errorMessage = SpellRecipe.validate(this.craftSlots);
		}
		System.out.println("[ERROR] " + errorMessage);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void fillCraftSlotsStackedContents(RecipeItemHelper recipe) {
	}

	public void clearCraftingContent() {
		this.getCraftSlots().clearContent();
		this.resultSlots.clearContent();
	}

	public void removed(PlayerEntity player) {
		super.removed(player);
		this.access.execute((p_217068_2_, p_217068_3_) -> {
			this.clearContainer(player, p_217068_2_, this.getCraftSlots());
		});
	}

	public boolean stillValid(PlayerEntity player) {
		return stillValid(this.access, player, ModBlocks.SPELLBINDING_BLOCK.get());
	}

	public ItemStack quickMoveStack(PlayerEntity player, int slotId) {
		// Inventory = 10 to 46 I guess
		// rune = 2 to 2 + WIDTH * HEIGHT
		// book = 1
		// result = 0
		int bookSlot = 1, runeSlotMin = bookSlot + 1, runeSlotMax = runeSlotMin - 1 + WIDTH * HEIGHT,
				inventoryMin = runeSlotMax + 1, inventoryMax = inventoryMin + 36, shortcutsMin = inventoryMax - 9;
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotId);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (slotId == getResultSlotIndex()) { // RESULT SLOT ?
				this.access.execute((world, p_217067_3_) -> {
					itemstack1.getItem().onCraftedBy(itemstack1, world, player);
				});
				// Put it in the inventory
				if (!this.moveItemStackTo(itemstack1, inventoryMin, inventoryMax, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotId >= inventoryMin && slotId < inventoryMax) { // IN INVENTORY
				if (!this.moveItemStackTo(itemstack1, 1, inventoryMin, false)) {
					if (slotId < shortcutsMin) { //
						if (!this.moveItemStackTo(itemstack1, shortcutsMin, inventoryMax, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.moveItemStackTo(itemstack1, inventoryMin, shortcutsMin, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(itemstack1, inventoryMin, inventoryMax, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(player, itemstack1);
			if (slotId == 0) {
				player.drop(itemstack2, false);
			}
		}

		return itemstack;
	}

	public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
		return slot.container != this.resultSlots && super.canTakeItemForPickAll(itemStack, slot);
	}

	public int getResultSlotIndex() {
		return 0;
	}

	public int getGridWidth() {
		return WIDTH;
	}

	public int getGridHeight() {
		return HEIGHT;
	}

	@OnlyIn(Dist.CLIENT)
	public int getSize() {
		return getGridWidth() * getGridHeight();
	}

	public SpellBindingInventory getCraftSlots() {
		return craftSlots;
	}

	public String getSpellName() {
		return this.craftSlots.getSpellName();
	}

	public boolean setSpellNameNoUpdate(String name) {
		return this.craftSlots.setSpellName(name);
	}

	public void setSpellName(String name) {
		if (setSpellNameNoUpdate(name)) {
			propertyChanged();
		}
	}
}