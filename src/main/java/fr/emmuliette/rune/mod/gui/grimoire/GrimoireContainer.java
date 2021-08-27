package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.Arrays;
import java.util.Collection;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.SyncHandler;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.containers.ModContainers;
import fr.emmuliette.rune.mod.items.spellItems.AbstractSpellItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;

public class GrimoireContainer extends Container {
	private GrimoireInventory spellSlots;
	private final IWorldPosCallable access;
	private Grimoire grimoire;
	private ISpell selectedSpell;
	private int spellCount;

	public GrimoireContainer(int containerId, PlayerInventory playerInventory, PacketBuffer data) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
	}

	public GrimoireContainer(int containerId, PlayerInventory playerInventory) {
		this(containerId, playerInventory, IWorldPosCallable.NULL);
	}

	public GrimoireContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.GRIMOIRE.get(), containerId);
		this.access = postCall;
		try {
			ICaster cap = playerInventory.player.getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(playerInventory.player));
			grimoire = cap.getGrimoire();
			grimoire.initInventory();
			this.spellSlots = grimoire.getInventory();

			spellCount = grimoire.getSpells().size();
			for (int i = 0; i < spellCount; i++) {
				this.addSlot(new GrimoireSpellSlot(this.spellSlots, i, 26, 32 + 18 * i));
			}

			// Shortcuts TODO
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l, 56 + l * 20, 183));
			}
//			this.broadcastChanges();
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}
	}

	private void getSpellServer(final int slotId) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt(AbstractSpellItem.SPELL_ID, slotId);
		SyncHandler.sendToServer(new CGrimoireActionPacket(nbt));
	}

	public void removed(PlayerEntity player) {
		super.removed(player);
		this.access.execute((p_217068_2_, p_217068_3_) -> {
			this.clearContainer(player, p_217068_2_, this.spellSlots);
		});
	}

	public boolean stillValid(PlayerEntity player) {
		return true;
	}

	public int getSpellCount() {
		return spellCount;
	}

	public Collection<GrimoireCategories> getGrimoireCategories() {
		return Arrays.asList(
				new GrimoireCategories[] { GrimoireCategories.CRAFTING_SEARCH, GrimoireCategories.CRAFTING_REDSTONE });
	}

	public ItemStack quickMoveStack(PlayerEntity player, int slotId) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotId);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (slotId < this.spellCount) {
				if (!this.moveItemStackTo(itemstack1, this.spellCount, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return itemstack;
	}

	@Override
	public ItemStack clicked(int slotId, int button, ClickType clickType, PlayerEntity player) {
		System.out.println("slotId is " + slotId + ", CLIC IS " + clickType + " - BUTTON IS " + button);
		if (slotId != -999 && player.inventory.getCarried().isEmpty()) {
			Slot slot = this.slots.get(slotId);
			if (slot instanceof GrimoireSpellSlot) {
				if (button == 0) {
					getSpellServer(slotId);
//					ItemStack item = this.getSlot(slotId).getItem();
//					player.inventory.setCarried(item);
				} else if (button == 1)
					selectSpell(slotId);
				return ItemStack.EMPTY;
			}
		}
		return super.clicked(slotId, button, clickType, player);
	}

	private void selectSpell(int slotId) {
		if (this.getSlot(slotId).getItem() == ItemStack.EMPTY) {
			RuneMain.LOGGER.info("ITEM is EMPTY");
		}
		selectedSpell = grimoire.getSpell(slotId);
		System.out.println("Selected spell is now " + selectedSpell.getSpell().getName());
	}

	public ISpell getSelectedSpell() {
		return selectedSpell;
	}

	public Grimoire getGrimoire() {
		return grimoire;
	}
}