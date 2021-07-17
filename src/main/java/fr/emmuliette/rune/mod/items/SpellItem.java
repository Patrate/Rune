package fr.emmuliette.rune.mod.items;

import javax.annotation.Nonnull;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SpellItem extends Item {
	public static enum ItemType {
		PARCHMENT, GRIMOIRE, SOCKET, SPELL;
	}
	// private static final Map<Long, Spell> spellCache = new HashMap<Long,

	public static ItemStack buildSpellItem(final Spell spell, ItemType type) {
		SpellItem spellitem;
		try {
			switch (type) {
			case PARCHMENT:
				spellitem = (SpellItem) ModObjects.PARCHMENT.getModItem();
				break;
			case GRIMOIRE:
				spellitem = (SpellItem) ModObjects.GRIMOIRE.getModItem();
				break;
			case SOCKET:
				spellitem = (SpellItem) ModObjects.SOCKET.getModItem();
				break;
			case SPELL:
			default:
				spellitem = (SpellItem) ModObjects.SPELL.getModItem();
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

	@Override
	public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity caster) {
		// TODO on clic milieux, changer de mode si le spell est modal
		final Result retour = new Result(itemStack);
		if (!caster.level.isClientSide) {
			try {
				ISpell cap = itemStack.getCapability(SpellCapability.SPELL_CAPABILITY)
						.orElseThrow(new SpellCapabilityExceptionSupplier(itemStack));
				Spell spell = cap.getSpell();
				if (spell != null) {
					if (spell.castSpecial(itemStack, target, caster.level, caster, null)) {
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

	@Override
	public boolean isFoil(ItemStack itemStack) {
		return true;
	}

	private static class Result {
		ActionResult<ItemStack> result;
		ActionResultType resultType;
		boolean consume;

		public Result(ItemStack item) {
			result = ActionResult.pass(item);
			resultType = ActionResultType.PASS;
			consume = false;
		}
	}

	// On clic droit living entity
	@Override
	public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity target,
			Hand hand) {
		Result retour = castSpell(itemStack, target, null, player, null, hand);
		return retour.resultType;
	}

	// On clic droit air
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		Result retour = castSpell(itemStack, null, world, player, null, hand);
		return retour.result;
	}

	// On clic droit block
	@Override
	public ActionResultType useOn(ItemUseContext itemUseContext) {
		ItemStack itemStack = itemUseContext.getItemInHand();
		Result retour = castSpell(itemStack, null, null, itemUseContext.getPlayer(), itemUseContext,
				itemUseContext.getHand());
		return retour.resultType;
	}

	private Result castSpell(@Nonnull ItemStack itemStack, LivingEntity target, World world,
			@Nonnull LivingEntity caster, ItemUseContext itemUseContext, Hand hand) {
		final Result retour = new Result(itemStack);
		try {
			ISpell cap = itemStack.getCapability(SpellCapability.SPELL_CAPABILITY)
					.orElseThrow(new SpellCapabilityExceptionSupplier(itemStack));
			Spell spell = cap.getSpell();
			if (spell == null) {
				return retour;
			}
			if (!caster.level.isClientSide) {
				Boolean cont = spell.cast(itemStack, target, world, caster, itemUseContext);
				if (cont == null) {
					retour.resultType = ActionResultType.SUCCESS;
					retour.result = ActionResult.success(itemStack);
				} else if (cont) {
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
			if (caster.level.isClientSide) {
				Boolean cont = spell.castable(itemStack, target, world, caster, itemUseContext);
				if (cont == null) {
					retour.resultType = ActionResultType.SUCCESS;
					retour.result = ActionResult.success(itemStack);
				} else if (cont) {
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
		if (retour.consume) {
			itemStack.shrink(1);
			/*
			 * if (itemStack.isEmpty()) { caster.setItemInHand(hand, ItemStack.EMPTY); }
			 */
		}
		return retour;
	}

	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		ISpell cap = stack.getCapability(SpellCapability.SPELL_CAPABILITY, null)
				.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));

		return cap.toNBT();
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
		RuneMain.LOGGER.debug("readShareTag !!! " + nbt.getAsString());
		//super.readShareTag(stack, nbt);
		
		if (nbt != null) {
			ISpell cap = stack.getCapability(SpellCapability.SPELL_CAPABILITY, null)
					.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
			// cap.setLinkedUUID(nbt.getString("linkedUUID"));
			cap.fromNBT(nbt);
		}
	}
}
