package fr.emmuliette.rune.mod.capabilities.spell;

import java.util.function.Supplier;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SpellPacket {
	private final CompoundNBT nbt;

	public SpellPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(SpellPacket msg, PacketBuffer buf) {
		if(buf == null) {
			RuneMain.LOGGER.debug("buf is null in decode SpellPacket");
		}
		if(msg == null) {
			RuneMain.LOGGER.debug("msg is null in decode SpellPacket");
		}
		buf.writeNbt(msg.nbt);
	}

	public static SpellPacket decode(PacketBuffer buf) {
		if(buf == null) {
			RuneMain.LOGGER.debug("buf is null in decode SpellPacket");
		}
		try {
			CompoundNBT nbt = buf.readNbt();
			if(nbt == null) {
				RuneMain.LOGGER.debug("nbt is null in decode SpellPacket");
			} else {
				RuneMain.LOGGER.debug("nbt in decode SpellPacket: " + nbt.toString());
			}
			return new SpellPacket(nbt);
		} catch(Exception e) {
			RuneMain.LOGGER.debug("Exception " + e.getClass().getSimpleName() + " while reading nbt from buf " + e.getMessage());
		}
		return new SpellPacket(new CompoundNBT());
	}

	public static class Handler {
		@SuppressWarnings("resource")
		public static void handle(final SpellPacket msg, Supplier<NetworkEvent.Context> ctx) {
			
			ctx.get().enqueueWork(() -> {
				if(msg == null) {
					RuneMain.LOGGER.debug("msg is null in handle SpellPacket");
				}
				if(msg.nbt == null) {
					RuneMain.LOGGER.debug("nbt is null in handle SpellPacket");
				}
				Minecraft.getInstance().player.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(cap -> cap.fromNBT(msg.nbt));
				
			});
			ctx.get().setPacketHandled(true);

			/*
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
			ctx.get().setPacketHandled(true);*/
		}
	}
}
