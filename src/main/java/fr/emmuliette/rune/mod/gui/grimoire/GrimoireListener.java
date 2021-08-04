package fr.emmuliette.rune.mod.gui.grimoire;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireListener implements IContainerListener {
//	private final Minecraft minecraft;

	public GrimoireListener(Minecraft minecraft) {
//		this.minecraft = minecraft;
	}

	public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {
	}

	public void slotChanged(Container container, int slot, ItemStack item) {
//		  this.minecraft.gameMode.connection.send(new CCreativeInventoryActionPacket(slot, item));

	}

	public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
	}
}
