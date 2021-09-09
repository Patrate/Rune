package fr.emmuliette.rune.mod.gui.spellbinding;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CSpellBindingSlotPacket {
	public static final String NAME_NBT = "name", PROPERTIES_NBT = "properties", CONTAINER_ID_NBT = "container_id";
	private final CompoundNBT nbt;

	public CSpellBindingSlotPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(CSpellBindingSlotPacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}

	public static CSpellBindingSlotPacket decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		return new CSpellBindingSlotPacket(nbt);
	}

	public static class Handler {
		public static void handle(final CSpellBindingSlotPacket msg, Supplier<NetworkEvent.Context> ctx) {

			if (ctx.get().getSender().containerMenu instanceof SpellBindingContainer) {
				SpellBindingContainer container = (SpellBindingContainer) ctx.get().getSender().containerMenu;
				container.setSpellNameNoUpdate(msg.nbt.getString(NAME_NBT));
				container.updateProperties(msg.nbt.get(PROPERTIES_NBT));
				container.slotChangedCraftingGrid();
			} else {
				System.out.println(ctx.get().getSender().containerMenu.getClass().getSimpleName());
			}

			ctx.get().setPacketHandled(true);
		}

	}
}
