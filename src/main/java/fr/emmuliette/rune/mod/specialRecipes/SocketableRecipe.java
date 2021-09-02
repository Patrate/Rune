package fr.emmuliette.rune.mod.specialRecipes;

import fr.emmuliette.rune.mod.capabilities.socket.ISocket;
import fr.emmuliette.rune.mod.capabilities.socket.SocketCapability;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellCapability;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.setup.ModTags;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SocketableRecipe extends SpecialRecipe {

	public SocketableRecipe(ResourceLocation rl) {
		super(rl);
	}

	public static void register() {
	}

	public boolean matches(CraftingInventory inventory, World world) {
		boolean hasTool = false, hasSocket = false;

		for (int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack itemstack = inventory.getItem(i);
			if (!itemstack.isEmpty()) {
				if (itemstack.getItem().is(ModTags.Items.SOCKETABLE)) {
					if (hasTool)
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
					hasTool = true;
				} else if (itemstack.getItem() == ModItems.SOCKET.get()) {
					if (hasSocket)
						return false;
					hasSocket = true;
				}
			}
		}

		System.out.println("MATCHES SOCKET: " + (hasTool && hasSocket));
		return hasTool && hasSocket;
	}

	@Override
	public ItemStack assemble(CraftingInventory inventory) {
		ItemStack tool = null;
		ItemStack socket = null;

		for (int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack itemstack = inventory.getItem(i);
			if (!itemstack.isEmpty()) {
				if (itemstack.getItem().is(ModTags.Items.SOCKETABLE)) {
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

		ISpell spell = socket.getCapability(SpellCapability.SPELL_CAPABILITY).orElse(null);
		if (spell == null || spell.getSpell() == null) {
			System.out.println("spell is null !");
			return ItemStack.EMPTY;
		}
		ItemStack retour = tool.copy();
		ISocket socketTool = retour.getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
		if (socketTool == null) {
			System.out.println("socketTool is null but shouldn't be !");
			return ItemStack.EMPTY;
		}

		System.out.println("SETTING SPELL ON TOOL");
		socketTool.setSpell(spell.getSpell());
		return retour;
	}

	public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
		return gridWidth * gridHeight >= 2;
	}

	public IRecipeSerializer<?> getSerializer() {
		return Registration.SOCKET_RECIPE.get();
	}
}
