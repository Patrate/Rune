package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.Arrays;
import java.util.Collection;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.caster.grimoire.Grimoire;
import fr.emmuliette.rune.mod.containers.ModContainers;
import fr.emmuliette.rune.mod.spells.capability.ISpell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;

public class GrimoireContainer extends Container {
	private final GrimoireInventory spellSlots;
	private final IWorldPosCallable access;
	private Grimoire grimoire;

	public GrimoireContainer(int containerId, PlayerInventory playerInventory, PacketBuffer data) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
	}

	public GrimoireContainer(int containerId, PlayerInventory playerInventory) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
	}

	public GrimoireContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.SPELLBINDING.get(), containerId);
		this.spellSlots = new GrimoireInventory(this, playerInventory.player);
		this.access = postCall;
		try {
			ICaster cap = playerInventory.player.getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(playerInventory.player));
			grimoire = cap.getGrimoire();
			// Spell slots
			int i = 0;
			for (ISpell spell : grimoire.getSpells()) {
				this.addSlot(new GrimoireSpellSlot(this.spellSlots, 1, 100, 62 + 16 * i, spell));
				++i;
			}
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}

		// Shortcuts TODO
//		for (int l = 0; l < 9; ++l) {
//			this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 100));
//		}
	}

//	protected static void slotChangedCraftingGrid(int containerId, World world, PlayerEntity player,
//			GrimoireInventory spellBindingInventory, GrimoireInventory craftingResultInventory) {
//		if (!world.isClientSide) {
//			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
//			ItemStack itemstack = ItemStack.EMPTY;
//			Optional<SpellBindingRecipe> optional = world.getServer().getRecipeManager()
//					.getRecipeFor(Registration.SPELLBINDING_RECIPE, spellBindingInventory, world);
//			if (optional.isPresent()) {
//				SpellBindingRecipe icraftingrecipe = optional.get();
//				if (craftingResultInventory.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
//					itemstack = icraftingrecipe.assemble(spellBindingInventory);
//				}
//			}
//
//			ItemStack itemstack1 = craftingResultInventory.getItem(0).copy();
//			itemstack.setHoverName(new StringTextComponent(itemstack1.getHoverName().getString()));
//			craftingResultInventory.setItem(0, itemstack);
//			serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
//		}
//	}
//
//	public void slotsChanged(IInventory slot) {
//		// super.slotsChanged(slot);
//		if (slot == this.resultSlots) {
//			this.createResult();
//		}
//		this.access.execute((world, blockPos) -> {
//			slotChangedCraftingGrid(this.containerId, world, this.player, this.craftSlots, this.resultSlots);
//		});
//	}

	public void removed(PlayerEntity player) {
		super.removed(player);
		this.access.execute((p_217068_2_, p_217068_3_) -> {
			this.clearContainer(player, p_217068_2_, this.spellSlots);
		});
	}

	public boolean stillValid(PlayerEntity player) {
		return true;
	}

	public int getSpellNumber() {
		return grimoire.getSpells().size();
	}

	public ItemStack quickMoveStack(PlayerEntity player, int slotId) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotId);
		if (slot != null && slot.hasItem() && slotId < this.getSpellNumber()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (!this.moveItemStackTo(itemstack1, this.getSpellNumber(), this.getSpellNumber() + 9, true)) {
				return ItemStack.EMPTY;
			}
		}
		return itemstack;
	}

	public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
		return false;
	}

	public Collection<GrimoireCategories> getGrimoireCategories() {
		return Arrays.asList(
				new GrimoireCategories[] { GrimoireCategories.CRAFTING_SEARCH, GrimoireCategories.CRAFTING_REDSTONE });
	}
}