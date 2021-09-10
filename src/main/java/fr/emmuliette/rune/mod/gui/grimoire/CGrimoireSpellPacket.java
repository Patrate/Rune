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

public class CGrimoireSpellPacket {
	public static enum Action {
		GET, REMOVE;
	}

	static final String SPELL_ID = "spell_id", ACTION_ID = "action_id";
	private final CompoundNBT nbt;

	public CGrimoireSpellPacket(int spellId, Action action) {
		this.nbt = new CompoundNBT();
		nbt.putInt(SPELL_ID, spellId);
		nbt.putString(ACTION_ID, action.name());
	}

	private CGrimoireSpellPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(CGrimoireSpellPacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}

	public static CGrimoireSpellPacket decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		return new CGrimoireSpellPacket(nbt);
	}

	public static class Handler {
		public static void handle(final CGrimoireSpellPacket msg, Supplier<NetworkEvent.Context> ctx) {
			ServerPlayerEntity player = ctx.get().getSender();

			ICaster caster = player.getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
			if (caster == null) {
				System.out.println("CASTER IS NULL");
			} else {
				Action action = Action.valueOf(msg.nbt.getString(ACTION_ID));
				int spellId = msg.nbt.getInt(SPELL_ID);

				switch (action) {
				case GET:
					ItemStack itemstack = AbstractSpellItem.getGrimoireSpell(caster.getGrimoire(), spellId);
					player.addItem(itemstack);
					player.inventoryMenu.setSynched(player, true);
					player.inventoryMenu.broadcastChanges();
					break;
				case REMOVE:
					caster.getGrimoire().removeSpell(spellId);
					caster.sync();
					break;
				default:
					break;

				}
			}

			ctx.get().setPacketHandled(true);
		}
	}
}
