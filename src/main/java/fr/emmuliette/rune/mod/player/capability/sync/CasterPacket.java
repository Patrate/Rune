package fr.emmuliette.rune.mod.player.capability.sync;

import java.util.function.Supplier;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.player.capability.ICaster;
import fr.emmuliette.rune.mod.player.capability.CasterCapability;
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


			ctx.get().enqueueWork(() -> {
				if (ctx.get().getDirection().getReceptionSide().isClient()
						&& ctx.get().getDirection().getOriginationSide().isServer()) {
					try {
						Minecraft mc = Minecraft.getInstance();
						ICaster cap = mc.player.getCapability(CasterCapability.CASTER_CAPABILITY)
								.orElseThrow(new CasterCapabilityExceptionSupplier(mc.player));
						cap.fromNBT(msg.nbt);
					} catch (CasterCapabilityException e) {
						e.printStackTrace();
					}
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
