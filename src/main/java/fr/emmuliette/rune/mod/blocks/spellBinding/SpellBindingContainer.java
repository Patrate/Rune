package fr.emmuliette.rune.mod.blocks.spellBinding;

import java.util.Optional;

import fr.emmuliette.rune.exception.NotABlockException;
import fr.emmuliette.rune.mod.ModObjects;
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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpellBindingContainer extends RecipeBookContainer<SpellBindingInventory> {
	private final SpellBindingInventory craftSlots = new SpellBindingInventory(this, 3, 3);
	private final CraftResultInventory resultSlots = new CraftResultInventory();
	private final IWorldPosCallable access;
	private final PlayerEntity player;

	public SpellBindingContainer(int containerId, PlayerInventory playerInventory, PacketBuffer data) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
		// TODO do thing with packetBuffer ?
	}

	public SpellBindingContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.SPELLBINDING.get(), containerId);
		this.access = postCall;
		this.player = playerInventory.player;
		this.addSlot(new SpellBindingResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));

		// Crafting slots
//		for(int i = 0; i < 6; ++i) {
//			this.addSlot(new Slot(this.craftSlots, i, 30 + i * 18, 25));
//		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new SpellBindingRuneSlot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
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

	protected static void slotChangedCraftingGrid(int containerId, World world, PlayerEntity player,
			SpellBindingInventory SpellBindingInventory, CraftResultInventory craftingResultInventory) {
		if (!world.isClientSide) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<SpellBindingRecipe> optional = world.getServer().getRecipeManager()
					.getRecipeFor(Registration.SPELLBINDING_RECIPE, SpellBindingInventory, world);
			if (optional.isPresent()) {
				SpellBindingRecipe icraftingrecipe = optional.get();
				if (craftingResultInventory.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
					itemstack = icraftingrecipe.assemble(SpellBindingInventory);
				}
			}

			craftingResultInventory.setItem(0, itemstack);
			// return NetworkHooks.getEntitySpawningPacket(this);
			serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
		}
	}

	public void slotsChanged(IInventory slot) {
		this.access.execute((world, p_217069_2_) -> {
			slotChangedCraftingGrid(this.containerId, world, this.player, this.craftSlots, this.resultSlots);
		});
	}

	public void fillCraftSlotsStackedContents(RecipeItemHelper recipe) {
		this.craftSlots.fillStackedContents(recipe);
	}

	public void clearCraftingContent() {
		this.craftSlots.clearContent();
		this.resultSlots.clearContent();
	}

	public boolean recipeMatches(IRecipe<? super SpellBindingInventory> recipe) {
		return recipe.matches(this.craftSlots, this.player.level);
	}

	public void removed(PlayerEntity player) {
		super.removed(player);
		this.access.execute((p_217068_2_, p_217068_3_) -> {
			this.clearContainer(player, p_217068_2_, this.craftSlots);
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
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotId);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (slotId == 0) {
				this.access.execute((p_217067_2_, p_217067_3_) -> {
					itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, player);
				});
				if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotId >= 10 && slotId < 46) {
				if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
					if (slotId < 37) {
						if (!this.moveItemStackTo(itemstack1, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.moveItemStackTo(itemstack1, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
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
		return this.craftSlots.getWidth();
	}

	public int getGridHeight() {
		return this.craftSlots.getHeight();
	}

	@OnlyIn(Dist.CLIENT)
	public int getSize() {
		return 10;
	}

	@OnlyIn(Dist.CLIENT)
	public RecipeBookCategory getRecipeBookType() {
		return RecipeBookCategory.CRAFTING;
		// TODO changer reciupeBook !
	}
}