package fr.emmuliette.rune.mod.gui.grimoireItem;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.sync.ClientPlayNetHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class SOpenGrimoirePacket {
	public static final String HAND_NBT = "hand";
	private final CompoundNBT nbt;

	public static CompoundNBT buildNBT(Hand hand) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString(SOpenGrimoirePacket.HAND_NBT, hand.toString());
		return nbt;
	}

	public SOpenGrimoirePacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(SOpenGrimoirePacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}

	public static SOpenGrimoirePacket decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		return new SOpenGrimoirePacket(nbt);
	}

	public static class Handler {
		public static void handle(final SOpenGrimoirePacket msg, Supplier<NetworkEvent.Context> ctx) {
			Hand hand = Hand.valueOf(msg.nbt.getString(HAND_NBT));
			ctx.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPlayNetHandler.handleOpenGrimoire(hand));
			});

			ctx.get().setPacketHandled(true);
		}
	}
}
