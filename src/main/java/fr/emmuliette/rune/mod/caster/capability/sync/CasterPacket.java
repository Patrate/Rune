package fr.emmuliette.rune.mod.caster.capability.sync;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
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
		@SuppressWarnings("resource")
		public static void handle(final CasterPacket msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {

				Minecraft.getInstance().player.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(cap -> cap.fromNBT(msg.nbt));

			});
			ctx.get().setPacketHandled(true);

			/*
			 * ctx.get().enqueueWork(() -> { if
			 * (ctx.get().getDirection().getReceptionSide().isClient() &&
			 * ctx.get().getDirection().getOriginationSide().isServer()) { try { Minecraft
			 * mc = Minecraft.getInstance(); ICaster cap =
			 * mc.player.getCapability(CasterCapability.CASTER_CAPABILITY) .orElseThrow(new
			 * CasterCapabilityExceptionSupplier(mc.player)); cap.fromNBT(msg.nbt); } catch
			 * (CasterCapabilityException e) { e.printStackTrace(); } } });
			 * ctx.get().setPacketHandled(true);
			 */
		}
	}
}
