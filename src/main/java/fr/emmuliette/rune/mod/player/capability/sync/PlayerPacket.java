package fr.emmuliette.rune.mod.player.capability.sync;

import java.util.function.Supplier;

import fr.emmuliette.rune.exception.PlayerCapabilityException;
import fr.emmuliette.rune.exception.PlayerCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.player.capability.IPlayer;
import fr.emmuliette.rune.mod.player.capability.PlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PlayerPacket {
	private final CompoundNBT nbt;

	public PlayerPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(PlayerPacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}

	public static PlayerPacket decode(PacketBuffer buf) {
		return new PlayerPacket(buf.readNbt());
	}

	public static class Handler {
		public static void handle(final PlayerPacket msg, Supplier<NetworkEvent.Context> ctx) {


			ctx.get().enqueueWork(() -> {
				if (ctx.get().getDirection().getReceptionSide().isClient()
						&& ctx.get().getDirection().getOriginationSide().isServer()) {
					try {
						Minecraft mc = Minecraft.getInstance();
						IPlayer cap = mc.player.getCapability(PlayerCapability.PLAYER_CAPABILITY)
								.orElseThrow(new PlayerCapabilityExceptionSupplier(mc.player));
						cap.fromNBT(msg.nbt);
					} catch (PlayerCapabilityException e) {
						e.printStackTrace();
					}
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
