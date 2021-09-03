package fr.emmuliette.rune.mod.specialRecipes;

import fr.emmuliette.rune.exception.SpellCapabilityException;
import fr.emmuliette.rune.mod.capabilities.socket.ISocket;
import fr.emmuliette.rune.mod.capabilities.socket.SocketCapability;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellCapability;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.mod.items.spellItems.AbstractSpellItem;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.tags.OtherTag;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SocketableRecipe extends SpecialRecipe {

	public static boolean isSocketable(Item item) {
		return isArmorSocketable(item) || isToolSocketable(item) || isShootableSocketable(item);
	}

	public static boolean isArmorSocketable(Item item) {
		return (item instanceof ArmorItem);
	}

	public static boolean isToolSocketable(Item item) {
		return (item instanceof SwordItem || item instanceof ShovelItem || item instanceof AxeItem
				|| item instanceof HoeItem || item instanceof PickaxeItem);
	}

	public static boolean isShootableSocketable(Item item) {
		return (item instanceof ShootableItem);
	}

	public SocketableRecipe(ResourceLocation rl) {
		super(rl);
	}

	public static void register() {
	}

	public boolean matches(CraftingInventory inventory, World world) {
		ItemStack tool = null, socket = null;

		for (int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack itemstack = inventory.getItem(i);
			if (!itemstack.isEmpty()) {
				if (isSocketable(itemstack.getItem())) {
					if (tool != null)
						return false;
					ISocket toolSocket = itemstack.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
					if (toolSocket == null) {
						// TODO throw error;
						return false;
					}
					if (toolSocket.getSpell() != null) {
						// TODO warn ?
						return false;
					}
					tool = itemstack;
				} else if (itemstack.getItem() == ModItems.SOCKET.get()) {
					if (socket != null)
						return false;
					socket = itemstack;
				}
			}
		}
		if (tool == null || socket == null)
			return false;

		try {
			Item toolItem = tool.getItem();
			Spell spell = AbstractSpellItem.getSpell(socket);

			return ((isArmorSocketable(toolItem)
					&& spell.getStartingComponent().getTags().hasTag(OtherTag.SOCKETABLE_ARMOR))
					|| (isToolSocketable(toolItem)
							&& spell.getStartingComponent().getTags().hasTag(OtherTag.SOCKETABLE_TOOL))
					|| (isShootableSocketable(toolItem)
							&& spell.getStartingComponent().getTags().hasTag(OtherTag.SOCKETABLE_DISTANCE)));

		} catch (SpellCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ItemStack assemble(CraftingInventory inventory) {
		ItemStack tool = null;
		ItemStack socket = null;

		for (int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack itemstack = inventory.getItem(i);
			if (!itemstack.isEmpty()) {
				if (isSocketable(itemstack.getItem())) {
					if (tool != null)
						return ItemStack.EMPTY;
					ISocket toolSocket = itemstack.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
					if (toolSocket == null) {
						// TODO throw error;
						return ItemStack.EMPTY;
					}
					if (toolSocket.getSpell() != null) {
						// TODO warn ?
						return ItemStack.EMPTY;
					}
					tool = itemstack;
					System.out.println("SET TOOL TO: " + tool.getItem().getName(tool));
				} else if (itemstack.getItem() == ModItems.SOCKET.get()) {
					if (socket != null)
						return ItemStack.EMPTY;
					socket = itemstack;
					System.out.println("SET SOCKET");
				}
			}
		}

		if (tool == null || socket == null) {
			System.out.println("TOOL OR SOCKET IS NULL");
			return ItemStack.EMPTY;
		}

		ISpell ispell = socket.getCapability(SpellCapability.SPELL_CAPABILITY).orElse(null);
		if (ispell == null || ispell.getSpell() == null) {
			System.out.println("spell is null !");
			return ItemStack.EMPTY;
		}

		Spell spell = ispell.getSpell();
		Item toolItem = tool.getItem();

		if ((isArmorSocketable(toolItem) && spell.getStartingComponent().getTags().hasTag(OtherTag.SOCKETABLE_ARMOR))
				|| (isToolSocketable(toolItem)
						&& spell.getStartingComponent().getTags().hasTag(OtherTag.SOCKETABLE_TOOL))
				|| (isShootableSocketable(toolItem)
						&& spell.getStartingComponent().getTags().hasTag(OtherTag.SOCKETABLE_DISTANCE))) {

			ItemStack retour = tool.copy();
			ISocket socketTool = retour.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
			if (socketTool == null) {
				System.out.println("socketTool is null but shouldn't be !");
				return ItemStack.EMPTY;
			}

			System.out.println("SETTING SPELL ON TOOL");
			socketTool.setSpell(spell);
			return retour;
		}
		return ItemStack.EMPTY;
	}

	public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
		return gridWidth * gridHeight >= 2;
	}

	public IRecipeSerializer<?> getSerializer() {
		return Registration.SOCKET_RECIPE.get();
	}
}
