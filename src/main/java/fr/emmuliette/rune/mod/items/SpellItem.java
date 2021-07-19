package fr.emmuliette.rune.mod.items;

import javax.annotation.Nullable;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.NotAnItemException;
import fr.emmuliette.rune.exception.SpellCapabilityException;
import fr.emmuliette.rune.exception.SpellCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.ModObjects;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.capability.ISpell;
import fr.emmuliette.rune.mod.spells.capability.SpellCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;

public class SpellItem extends AbstractSpellItem {
	public static enum ItemType {
		PARCHMENT, GRIMOIRE, SOCKET;
	}
	// private static final Map<Long, Spell> spellCache = new HashMap<Long,

	public static ItemStack buildSpellItem(final Spell spell, ItemType type) {
		SpellItem spellitem;
		try {
			switch (type) {
			case GRIMOIRE:
				spellitem = (SpellItem) ModObjects.GRIMOIRE.getModItem();
				break;
			case SOCKET:
				spellitem = (SpellItem) ModObjects.SOCKET.getModItem();
				break;
			case PARCHMENT:
			default:
				spellitem = (SpellItem) ModObjects.PARCHMENT.getModItem();
				break;
			}
			ItemStack itemStack = new ItemStack(spellitem);
			// itemStack.setHoverName(new StringTextComponent(spell.getName()));
			ISpell ispell = itemStack.getCapability(SpellCapability.SPELL_CAPABILITY)
					.orElseThrow(new SpellCapabilityExceptionSupplier(itemStack));
			ispell.setSpell(spell);
			return itemStack;
		} catch (NotAnItemException | SpellCapabilityException e) {
			e.printStackTrace();
		}
		return ItemStack.EMPTY;
	}

	public SpellItem(Item.Properties properties) {
		super(properties);
	}

	public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity caster) {
		// TODO on clic milieux, changer de mode si le spell est modal
		final Result retour = new Result(itemStack);
		if (!caster.level.isClientSide) {
			try {
				ISpell cap = itemStack.getCapability(SpellCapability.SPELL_CAPABILITY)
						.orElseThrow(new SpellCapabilityExceptionSupplier(itemStack));
				Spell spell = cap.getSpell();
				if (spell != null) {
					if (spell.castSpecial(1f, itemStack, target, caster.level, caster, null)) {
						try {
							retour.consume = (itemStack.getItem() == ModObjects.PARCHMENT.getModItem());
						} catch (NotAnItemException e) {
							e.printStackTrace();
						}
						if (retour.consume) {
							retour.resultType = ActionResultType.CONSUME;
							retour.result = ActionResult.consume(itemStack);
						} else {
							retour.resultType = ActionResultType.SUCCESS;
							retour.result = ActionResult.success(itemStack);
						}
					} else {
						retour.resultType = ActionResultType.PASS;
					}
				}
			} catch (SpellCapabilityException e) {
				e.printStackTrace();
			}
		}
		// if(retour.resultType)
		return super.hurtEnemy(itemStack, target, caster);
	}

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
		if (retour.isEmpty()) {
			RuneMain.LOGGER.debug("retour is null in getShareTag");
		} else {
			RuneMain.LOGGER.debug("retour in getShareTag: " + retour);
		}
		return retour;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
		if (nbt == null) {
			RuneMain.LOGGER.debug("nbt is null in readShareTag");
			return;
		} else {
			RuneMain.LOGGER.debug("nbt in readShareTag: " + nbt.toString());
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
