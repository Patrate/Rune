package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.Arrays;
import java.util.Collection;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.containers.ModContainers;
import fr.emmuliette.rune.mod.items.GrimoireSpellItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
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

	private void getSpell(final PlayerEntity player, final int slotId) {
		this.access.execute((world, blockPos) -> {

			// TODO
//			if (!world.isClientSide) {
//				ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
//				ItemStack itemstack = this.getSlot(slotId).getItem();
//				player.inventory.setCarried(itemstack.copy());
//				serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
//			}
		});
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

	public int getSpellCount() {
		return spellCount;
	}

//	@SuppressWarnings("resource")
//	public ItemStack quickMoveStack(PlayerEntity player, int slotId) {
//		ItemStack itemstack = ItemStack.EMPTY;
//		Slot slot = this.slots.get(slotId);
//		if (slot != null && slot.hasItem() && slotId < this.getSpellNumber()) {
//			itemstack = slot.getItem();
//			player.addItem(itemstack.copy());
//			Minecraft.getInstance().player.inventoryMenu.broadcastChanges();
//		}
//		return itemstack;
//	}

//	public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
//		return true;
//	}

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

//	@Override
//	public boolean canDragTo(Slot slot) {
//		return !(slot instanceof GrimoireSpellSlot);
//	}

	@Override
	public ItemStack clicked(int slotId, int button, ClickType clickType, PlayerEntity player) {
		System.out.println("slotId is " + slotId + ", CLIC IS " + clickType + " - BUTTON IS " + button);
		if (slotId != -999 && player.inventory.getCarried().isEmpty()) {
			Slot slot = this.slots.get(slotId);
			if (slot instanceof GrimoireSpellSlot) {
				ItemStack item = this.getSlot(slotId).getItem();
				if (button == 0)
					getSpell(player, slotId);
//					player.inventory.setCarried(item);
				else if (button == 1)
					selectSpell(slotId);
				return item;
			}
		}
		return super.clicked(slotId, button, clickType, player);
//		if (slotId < 9 || slotId >= 9 + spellSlotNbr)
//			return super.clicked(slotId, button, clickType, player);
//		if (button == 1) // clic droit seulement
//			selectSpell(slotId);
//		ItemStack item = this.getSlot(slotId).getItem();
//		switch (clickType) {
//		// normal et ctrl
//		case PICKUP:
//			// Maj appuyé
//		case QUICK_MOVE:
//			ItemStack item2 = this.getSlot(slotId).onTake(player, item);
//			if (item2.hasTag()) {
//				System.out.println("SPELL NAME IS " + item2.getTag().getString(GrimoireSpellItem.SPELL_NAME));
//				// WTF POURQUOI CA MARCHE PAS :'(
//				Slot slot = this.slots.get(slotId);
//				if (slot != null && slot.hasItem()) {
////					ItemStack spellItemstack = slot.getItem().copy();
////					spellItemstack.setCount(1);
//					player.inventory.setCarried(item2);
//					Minecraft.getInstance().player.inventoryMenu.broadcastChanges();
//				}
//			} else {
//				System.out.println("PUTAIN POURQUOI Y'A PAS DE TAG");
//			}
//			return item;
//
//		// clic milieux
//		case CLONE:
//
//			// jsp
//		case PICKUP_ALL:
//		case QUICK_CRAFT:
//		case SWAP:
//		case THROW:
//		default:
//			return item;
//		}
//		return super.clicked(slotId, button, clickType, player);
	}

	private void selectSpell(int slotId) {
		ItemStack item = this.getSlot(slotId).getItem();
		if (item == ItemStack.EMPTY) {
			System.out.println("ITEM is EMPTY");
			return;
		}
		if (!item.hasTag()) {
			System.out.println("No tags :(");
			return;
		}

		selectedSpell = grimoire.getSpell(item.getTag().getString(GrimoireSpellItem.SPELL_NAME));
		System.out.println("Selected spell is now " + selectedSpell.getSpell().getName());
	}

	public ISpell getSelectedSpell() {
		return selectedSpell;
	}

	public Grimoire getGrimoire() {
		return grimoire;
	}
}