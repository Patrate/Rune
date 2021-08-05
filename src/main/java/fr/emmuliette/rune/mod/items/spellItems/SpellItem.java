package fr.emmuliette.rune.mod.items.spellItems;

import javax.annotation.Nullable;

import fr.emmuliette.rune.exception.SpellCapabilityException;
import fr.emmuliette.rune.exception.SpellCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellCapability;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

public class SpellItem extends AbstractSpellItem {
	public static enum ItemType {
		PARCHMENT, GRIMOIRE, SOCKET, SPELL;
	}
	// private static final Map<Long, Spell> spellCache = new HashMap<Long,

	public static ItemStack buildSpellItem(final Spell spell, ItemType type) {
		SpellItem spellitem;
		try {
			switch (type) {
			case GRIMOIRE:
				spellitem = (SpellItem) ModItems.GRIMOIRE.getItem();
				break;
			case SOCKET:
				spellitem = (SpellItem) ModItems.SOCKET.getItem();
				break;
			case SPELL:
				spellitem = (SpellItem) ModItems.SPELL.getItem();
				break;
			case PARCHMENT:
			default:
				spellitem = (SpellItem) ModItems.PARCHMENT.getItem();
				break;
			}
			ItemStack itemStack = new ItemStack(spellitem);
			CompoundNBT tags = itemStack.getOrCreateTag();
			if (!tags.contains("display")) {
				tags.put("display", new CompoundNBT());
			}
			CompoundNBT displayTag = tags.getCompound("display");
			if (!displayTag.contains("Lore")) {
				displayTag.put("Lore", new ListNBT());
			}
			ListNBT lore = (ListNBT) displayTag.get("Lore");
			displayTag.put("Name", StringNBT.valueOf("TOTO"));
			lore.add(StringNBT.valueOf(" BONJOUR JE SUIS LE LORENT"));

			// itemStack.setHoverName(new StringTextComponent(spell.getName()));
			ISpell ispell = itemStack.getCapability(SpellCapability.SPELL_CAPABILITY)
					.orElseThrow(new SpellCapabilityExceptionSupplier(itemStack));
			ispell.setSpell(spell);
			return itemStack;
		} catch (SpellCapabilityException e) {
			e.printStackTrace();
		}
		return ItemStack.EMPTY;
	}

	public SpellItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public boolean isFoil(ItemStack itemStack) {
		return true;
	}

	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		ISpell cap = stack.getCapability(SpellCapability.SPELL_CAPABILITY, null)
				.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
		CompoundNBT retour = new CompoundNBT();
		CompoundNBT superTag = super.getShareTag(stack);
		CompoundNBT capTag = cap.toNBT();

		if (capTag != null)
			retour.put("CAP", capTag);

		if (superTag != null)
			retour.put("TAG", superTag);
		return retour;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
		if (nbt == null) {
			return;
		}
		if (nbt.contains("TAG"))
			super.readShareTag(stack, nbt.getCompound("TAG"));

		if (nbt.contains("CAP")) {
			ISpell cap = stack.getCapability(SpellCapability.SPELL_CAPABILITY, null)
					.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
			cap.fromNBT(nbt.get("CAP"));
		}
	}

}
