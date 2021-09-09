package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.items.spellItems.AbstractSpellItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CGrimoireGetSpellPacket {
	static final String SPELL_ID = "spell_id";
	private final CompoundNBT nbt;

	public CGrimoireGetSpellPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(CGrimoireGetSpellPacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}

	public static CGrimoireGetSpellPacket decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		return new CGrimoireGetSpellPacket(nbt);
	}

	public static class Handler {
		public static void handle(final CGrimoireGetSpellPacket msg, Supplier<NetworkEvent.Context> ctx) {
			ServerPlayerEntity player = ctx.get().getSender();

			ICaster caster = player.getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
			if (caster == null)
				System.out.println("CASTER IS NULL");
			else {
				int spellId = msg.nbt.getInt(SPELL_ID);
				ItemStack itemstack = AbstractSpellItem.getGrimoireSpell(caster.getGrimoire(), spellId);
//				player.inventory.setCarried(itemstack);
				player.addItem(itemstack);
				player.inventoryMenu.setSynched(player, true);
				player.inventoryMenu.broadcastChanges();
			}
//			ctx.get().enqueueWork(() -> {
//				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handlePacket(msg, ctx));
//			});

			ctx.get().setPacketHandled(true);
		}
	}

	public static class ClientHandler {
		public static void handlePacket(CGrimoireGetSpellPacket msg, Supplier<NetworkEvent.Context> ctx) {
			ServerPlayerEntity player = ctx.get().getSender();

			ICaster caster = player.getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
			if (caster == null)
				System.out.println("CASTER IS NULL");
			else {
				int spellId = msg.nbt.getInt(SPELL_ID);
				ItemStack itemstack = AbstractSpellItem.getGrimoireSpell(caster.getGrimoire(), spellId);
//				player.inventory.setCarried(itemstack);
				player.addItem(itemstack);
				player.inventoryMenu.setSynched(player, true);
				player.inventoryMenu.broadcastChanges();
			}
		}
	}
}
