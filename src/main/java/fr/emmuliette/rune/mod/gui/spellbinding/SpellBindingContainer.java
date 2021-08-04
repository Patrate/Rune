package fr.emmuliette.rune.mod.gui.spellbinding;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import fr.emmuliette.rune.exception.NotABlockException;
import fr.emmuliette.rune.mod.ModObjects;
import fr.emmuliette.rune.mod.blocks.spellBinding.SpellBindingRecipe;
import fr.emmuliette.rune.mod.containers.ModContainers;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpellBindingContainer extends RecipeBookContainer<SpellBindingInventory> {
	private final int WIDTH = 5;
	private final int HEIGHT = 3;
	private final SpellBindingInventory craftSlots = new SpellBindingInventory(this, WIDTH, HEIGHT);
	private String spellName;

	private final CraftResultInventory resultSlots = new CraftResultInventory();
	private final IWorldPosCallable access;
	private final PlayerEntity player;

	public SpellBindingContainer(int containerId, PlayerInventory playerInventory, PacketBuffer data) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
		// TODO do thing with packetBuffer ?
	}

	public SpellBindingContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.SPELLBINDING.get(), containerId);
		this.spellName = "";
		this.access = postCall;
		this.player = playerInventory.player;
		// Result slot
		this.addSlot(
				new SpellBindingResultSlot(playerInventory.player, this.getCraftSlots(), this.resultSlots, 0, 138, 31));

		// Book or parchment slot
		this.addSlot(new Slot(this.craftSlots, 1, 138, 62) {
			@Override
			public boolean mayPlace(ItemStack iStack) {
				return iStack.getItem() == Items.BOOK || iStack.getItem() == Items.PAPER;
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

//	private void reduce() {
//		this.removeSlot(this.slots.size() - 1);
//	}
//
//	private void add(int index) {
//		int i = index / 3;
//		int j = index % 3;
//		this.addSlot(new SpellBindingRuneSlot(this.craftSlots, index, 27 + j * 20, 15 + i * 20));
//	}

	protected static void slotChangedCraftingGrid(int containerId, World world, PlayerEntity player,
			SpellBindingInventory spellBindingInventory, CraftResultInventory craftingResultInventory) {
		if (!world.isClientSide) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<SpellBindingRecipe> optional = world.getServer().getRecipeManager()
					.getRecipeFor(Registration.SPELLBINDING_RECIPE, spellBindingInventory, world);
			if (optional.isPresent()) {
				SpellBindingRecipe icraftingrecipe = optional.get();
				if (craftingResultInventory.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
					itemstack = icraftingrecipe.assemble(spellBindingInventory);
				}
			}

			ItemStack itemstack1 = craftingResultInventory.getItem(0).copy();
			itemstack.setHoverName(new StringTextComponent(itemstack1.getHoverName().getString()));
			craftingResultInventory.setItem(0, itemstack);
			serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
		}
	}

	public void slotsChanged(IInventory slot) {
		// super.slotsChanged(slot);
		if (slot == this.resultSlots) {
			this.createResult();
		}
		this.access.execute((world, blockPos) -> {
			slotChangedCraftingGrid(this.containerId, world, this.player, this.craftSlots, this.resultSlots);
		});
	}

	protected static void nameChangedCraftingGrid(int containerId, World world, PlayerEntity player,
			SpellBindingInventory spellBindingInventory, CraftResultInventory craftingResultInventory) {
		if (!world.isClientSide) {
			System.out.println("NAME CHANGED AND NAME IS " + spellBindingInventory.getSpellName());
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<SpellBindingRecipe> optional = world.getServer().getRecipeManager()
					.getRecipeFor(Registration.SPELLBINDING_RECIPE, spellBindingInventory, world);
			if (optional.isPresent()) {
				SpellBindingRecipe icraftingrecipe = optional.get();
				if (craftingResultInventory.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
					itemstack = icraftingrecipe.assemble(spellBindingInventory);
				}
			}

			craftingResultInventory.setItem(0, itemstack);
			serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
		} else {
			System.out.println("We client, not updating");
		}
	}

	public void nameChanged() {
		System.out.println("HERE IT DOESN'T WORK FFS MAÏTE");
		this.broadcastChanges();
		// nameChangedCraftingGrid(this.containerId, this.player.level, this.player,
		// this.getCraftSlots(), this.resultSlots);

//		nameChangedCraftingGrid(this.containerId, this.player.level, this.player, this.craftSlots, this.resultSlots);
		// this.access.execute((world, p_217069_2_) -> {
//			nameChangedCraftingGrid(this.containerId, world, this.player, this.craftSlots, this.resultSlots);
		// });
		/*
		 * if (StringUtils.isBlank(this.spellName)) { if
		 * (this.resultSlots.getItem(0).hasCustomHoverName()) {
		 * this.resultSlots.getItem(0).resetHoverName(); } } else if
		 * (!this.spellName.equals(this.resultSlots.getItem(0).getHoverName().getString(
		 * ))) { this.resultSlots.getItem(0).setHoverName(new
		 * StringTextComponent(this.spellName)); }
		 */
	}

	public void fillCraftSlotsStackedContents(RecipeItemHelper recipe) {
		// this.getCraftSlots().fillStackedContents(recipe);
	}

	public void clearCraftingContent() {
		this.getCraftSlots().clearContent();
		this.resultSlots.clearContent();
	}

	@Override
	public boolean recipeMatches(IRecipe<? super SpellBindingInventory> recipe) {
		System.out.println("CHECKING SPELL NAME IS " + getSpellName());
		if (StringUtils.isBlank(this.getSpellName()))
			return false;
		return recipe.matches(this.getCraftSlots(), this.player.level);
	}

	public void removed(PlayerEntity player) {
		super.removed(player);
		this.access.execute((p_217068_2_, p_217068_3_) -> {
			this.clearContainer(player, p_217068_2_, this.getCraftSlots());
		});
	}

	public boolean stillValid(PlayerEntity player) {
		try {
			return stillValid(this.access, player, ModObjects.SPELLBINDING_BLOCK.getModBlock());
		} catch (NotABlockException e) {
			e.printStackTrace();
		}
		return false;
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
				this.access.execute((p_217067_2_, p_217067_3_) -> {
					itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, player);
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

	@OnlyIn(Dist.CLIENT)
	public RecipeBookCategory getRecipeBookType() {
		return RecipeBookCategory.CRAFTING;
		// TODO changer reciupeBook !
	}

	public SpellBindingInventory getCraftSlots() {
		return craftSlots;
	}

	public String getSpellName() {
		return this.resultSlots.getItem(0).getHoverName().getString();
	}

	public void setSpellName(String name) {
		this.spellName = name;
		if (this.getSlot(0).hasItem()) {
			ItemStack itemstack = this.resultSlots.getItem(0);
			if (StringUtils.isBlank(name)) {
				itemstack.resetHoverName();
			} else {
				itemstack.setHoverName(new StringTextComponent(this.spellName));
			}
		}

		this.createResult();
		nameChanged();
	}

	public void createResult() {
		ItemStack itemstack = this.resultSlots.getItem(0);
		if (itemstack.isEmpty()) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
		} else {
			ItemStack itemstack1 = itemstack.copy();
			if (StringUtils.isBlank(this.spellName)) {
				if (itemstack.hasCustomHoverName()) {
					itemstack1.resetHoverName();
				}
			} else if (!this.spellName.equals(itemstack.getHoverName().getString())) {
				itemstack1.setHoverName(new StringTextComponent(this.spellName));
			}
			this.resultSlots.setItem(0, itemstack1);
			this.broadcastChanges();
		}
	}
}