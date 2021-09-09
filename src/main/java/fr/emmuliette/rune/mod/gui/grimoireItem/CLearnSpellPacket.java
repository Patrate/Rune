package fr.emmuliette.rune.mod.gui.grimoireItem;

import java.util.function.Supplier;

import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellCapability;
import fr.emmuliette.rune.mod.items.ModItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

public class CLearnSpellPacket {
	public static final String HAND_NBT = "hand", COST_NBT = "cost";
	private final CompoundNBT nbt;

	public CLearnSpellPacket(Hand hand, int cost) {
		this.nbt = new CompoundNBT();
		nbt.putString(HAND_NBT, hand.toString());
		nbt.putInt(COST_NBT, cost);
	}

	public CLearnSpellPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(CLearnSpellPacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}

	public static CLearnSpellPacket decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		return new CLearnSpellPacket(nbt);
	}

	public static class Handler {
		public static void handle(final CLearnSpellPacket msg, Supplier<NetworkEvent.Context> ctx) {
			Hand hand = Hand.valueOf(msg.nbt.getString(HAND_NBT));
			ServerPlayerEntity player = ctx.get().getSender();
			ItemStack itemstack = player.getItemInHand(hand);
			if (itemstack.getItem() == ModItems.GRIMOIRE.get()) {
				ICaster caster = player.getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
				ISpell spell = itemstack.getCapability(SpellCapability.SPELL_CAPABILITY).orElse(null);
				if (caster != null && spell != null) {
					caster.getGrimoire().addSpell(spell);
					player.experienceLevel -= msg.nbt.getInt(COST_NBT);
					caster.sync();
//					WritableBookItem
				} else {
					System.err.println("THERE IS A PROBLEM");
					if (caster == null)
						System.err.println("Caster is null");
					else
						System.err.println("Caster is present");
					if (spell == null)
						System.err.println("spell is null");
					else {
						System.err.println("spell is present");
						if (spell.getSpell() == null)
							System.err.println("getSpell is null");
						else
							System.err.println("getSpell is present");

					}
				}
			}

			ctx.get().setPacketHandled(true);
		}
	}
}
