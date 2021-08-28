package fr.emmuliette.rune.mod.gui.grimoire;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.SyncHandler;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.containers.ModContainers;
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

	public GrimoireContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable postCall) {
		super(ModContainers.GRIMOIRE.get(), containerId);
		this.access = postCall;
		try {
			ICaster cap = playerInventory.player.getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(playerInventory.player));
			grimoire = cap.getGrimoire();
			this.spellSlots = new GrimoireInventory();
			this.spellSlots.init(grimoire);
			spellCount = grimoire.getSpells().size();

			for (int i = 0; i < spellCount; i++) {
				this.addSlot(new Slot(this.spellSlots, i, 26, 32 + 18 * i) {
					@Override
					public ItemStack onTake(PlayerEntity player, ItemStack item) {
						System.out.println("ON TAKE");
						return item.copy();
					}

					@Override
					public boolean mayPlace(ItemStack item) {
						return false;
					}

					@Override
					public boolean mayPickup(PlayerEntity player) {
						return false;
					}
				});
			}

			int wx = 36;
			int he = 137;

			for (int k = 0; k < 3; ++k) {
				for (int l = 0; l < 9; ++l) {
					this.addSlot(new Slot(playerInventory, l + k * 9 + 9, wx + l * 18, he + k * 18));
				}
			}

			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(playerInventory, i1, wx + i1 * 18, he + 58));
			}
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}
	}

	private void getSpellServer(final int slotId) {
		System.out.println("Getting spell in slot " + slotId);
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt(CGrimoireGetSpellPacket.SLOT_ID, slotId);
		SyncHandler.sendToServer(new CGrimoireGetSpellPacket(nbt));
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

	@Override
	public ItemStack clicked(int slotId, int button, ClickType clickType, PlayerEntity player) {
		System.out.println("slotId is " + slotId + ", CLIC IS " + clickType + " - BUTTON IS " + button);
		if (slotId >= 0 && slotId < spellCount) {
			if (button == 0 && player.inventory.getCarried().isEmpty()) {
				getSpellServer(slotId);
			} else if (button == 1)
				selectSpell(slotId);
			return this.getSlot(slotId).getItem();
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

//	public ItemStack quickMoveStack(PlayerEntity player, int slotId) {
//		ItemStack itemstack = ItemStack.EMPTY;
//		Slot slot = this.slots.get(slotId);
//		if (slot != null && slot.hasItem()) {
//			ItemStack itemstack1 = slot.getItem();
//			itemstack = itemstack1.copy();
//			if (slotId < this.spellCount) {
//				if (!this.moveItemStackTo(itemstack1, this.spellCount, this.slots.size(), true)) {
//					return ItemStack.EMPTY;
//				}
//			} else {
//				return ItemStack.EMPTY;
//			}
//
//			if (itemstack1.isEmpty()) {
//				slot.set(ItemStack.EMPTY);
//			} else {
//				slot.setChanged();
//			}
//		}
//
//		return itemstack;
//	}
}