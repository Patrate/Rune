package fr.emmuliette.rune.mod.capabilities.caster;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CasterPacket {
	private final CompoundNBT nbt;

	public CasterPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(CasterPacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}

	public static CasterPacket decode(PacketBuffer buf) {
		return new CasterPacket(buf.readNbt());
	}

	public static class Handler {
		public static void handle(final CasterPacket msg, Supplier<NetworkEvent.Context> ctx) {
			System.out.println("HANDLING PACKET");
			Minecraft minecraft = Minecraft.getInstance();

			ctx.get().enqueueWork(() -> {
				System.out.println("The packet contains: " + msg.nbt.getAsString());
				minecraft.player.getCapability(CasterCapability.CASTER_CAPABILITY)
						.ifPresent(cap -> cap.fromNBT(msg.nbt));

			});
			ctx.get().setPacketHandled(true);
		}
	}
}
