package fr.emmuliette.rune.mod.items;

import javax.annotation.Nonnull;

import fr.emmuliette.rune.mod.ModObjects;
import fr.emmuliette.rune.mod.NotAnItemException;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.capability.spell.SpellCapability;
import fr.emmuliette.rune.mod.spells.capability.spell.ISpell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullConsumer;

public class SpellItem extends Item {
	// private static final Map<Long, Spell> spellCache = new HashMap<Long,
	// Spell>();

	public static ItemStack buildSpellItem(final Spell spell) {
		SpellItem spellitem;
		try {
			spellitem = (SpellItem) ModObjects.SPELL.getModItem();
			ItemStack itemStack = new ItemStack(spellitem);
			itemStack.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(new NonNullConsumer<ISpell>() {
				@Override
				public void accept(@Nonnull ISpell iSpell) {
					iSpell.setSpell(spell);
				}
			});
			return itemStack;
		} catch (NotAnItemException e) {
			e.printStackTrace();
		}
		return ItemStack.EMPTY;
	}

	public SpellItem(Item.Properties properties) {
		super(properties);
	}

	// On clic gauche living entity
	/*
	 * @Override public boolean hurtEnemy(ItemStack p_77644_1_, LivingEntity
	 * p_77644_2_, LivingEntity p_77644_3_) { System.out.println("hurtEnemy");
	 * return super.hurtEnemy(p_77644_1_, p_77644_2_, p_77644_3_); }
	 */

	@Override
	public boolean isFoil(ItemStack itemStack) {
		return true;
	}

	private static class Result {
		ActionResult<ItemStack> result;
		ActionResultType resultType;

		public Result(ItemStack item) {
			result = ActionResult.pass(item);
			resultType = ActionResultType.PASS;
		}
	}

	// TODO on clic milieux, changer de mode si le spell est modal

	// On clic droit living entity
	@Override
	public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity target,
			Hand hand) {
		final Result retour = new Result(itemStack);
		if (!player.level.isClientSide) {
			itemStack.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(new NonNullConsumer<ISpell>() {
				@Override
				public void accept(@Nonnull ISpell iSpell) {
					// Spell spell = spellCache.get()
					Spell spell = iSpell.getSpell();
					if (spell != null) {
						retour.resultType = ((spell.cast(itemStack, target, null, player, null))
								? ActionResultType.SUCCESS
								: ActionResultType.PASS);
					} else {
						System.out.println("spell is null");
					}
				}
			});
		}
		return retour.resultType;
	}

	// On clic droit air
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		final Result retour = new Result(itemStack);
		if (!world.isClientSide) {
			itemStack.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(new NonNullConsumer<ISpell>() {
				@Override
				public void accept(@Nonnull ISpell iSpell) {
					// Spell spell = spellCache.get()
					Spell spell = iSpell.getSpell();
					if (spell != null) {
						retour.result = ((spell.cast(null, null, world, player, null)) ? ActionResult.success(itemStack)
								: ActionResult.pass(itemStack));
					} else {
						System.out.println("spell is null");
					}
				}
			});
		}
		return retour.result;
	}

	// On clic droit block
	@Override
	public ActionResultType useOn(ItemUseContext itemUseContext) {
		ItemStack itemStack = itemUseContext.getItemInHand();
		final Result retour = new Result(itemStack);
		if (!itemUseContext.getLevel().isClientSide) {
			itemStack.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(new NonNullConsumer<ISpell>() {
				@Override
				public void accept(@Nonnull ISpell iSpell) {
					// Spell spell = spellCache.get()
					Spell spell = iSpell.getSpell();
					if (spell != null) {
						retour.resultType = ((spell.cast(null, null, null, null, itemUseContext))
								? ActionResultType.SUCCESS
								: ActionResultType.PASS);
					} else {
						System.out.println("spell is null");
					}
				}
			});
		}
		return retour.resultType;
	}
}
