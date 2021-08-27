package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.items.spellItems.AbstractSpellItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class CGrimoireActionPacket {
	private final CompoundNBT nbt;

	public CGrimoireActionPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(CGrimoireActionPacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}

	public static CGrimoireActionPacket decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		return new CGrimoireActionPacket(nbt);
	}

	public static class Handler {
		public static void handle(final CGrimoireActionPacket msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handlePacket(msg, ctx));
			});

			ctx.get().setPacketHandled(true);
		}
	}

	public static class ClientHandler {
		public static void handlePacket(CGrimoireActionPacket msg, Supplier<NetworkEvent.Context> ctx) {
			System.out.println("Retrieving " + msg.nbt.getString(AbstractSpellItem.SPELL_ID));
			ICaster caster = ctx.get().getSender().getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
			if (caster == null)
				System.out.println("CASTER IS NULL");
			else {
				ItemStack spellItem = AbstractSpellItem.getGrimoireSpell(caster.getGrimoire(),
						msg.nbt.getInt(AbstractSpellItem.SPELL_ID));
				ctx.get().getSender().inventory.setCarried(spellItem);
			}
		}
	}
}
