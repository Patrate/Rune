package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class GrimoirePacket {
	private final CompoundNBT nbt;

	public GrimoirePacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(GrimoirePacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}

	public static GrimoirePacket decode(PacketBuffer buf) {
		return new GrimoirePacket(buf.readNbt());
	}

	public static class Handler {
		public static void handle(final GrimoirePacket msg, Supplier<NetworkEvent.Context> ctx) {
			System.out.println("HANDLING PACKET");
//			Minecraft minecraft = Minecraft.getInstance();

			ctx.get().enqueueWork(() -> {
				System.out.println("The packet contains: " + msg.nbt.getAsString());
//				minecraft.player.getCapability(CasterCapability.CASTER_CAPABILITY)
//						.ifPresent(cap -> cap.fromNBT(msg.nbt));

			});
			ctx.get().setPacketHandled(true);
		}
	}
}
