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
			//itemStack.setHoverName(new StringTextComponent(itemStack.getHoverName().getContents() + " " + spell.getName()));
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
		boolean consume;

		public Result(ItemStack item) {
			result = ActionResult.pass(item);
			resultType = ActionResultType.PASS;
			consume = false;
		}
	}

	// TODO on clic milieux, changer de mode si le spell est modal

	// On clic droit living entity
	@Override
	public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity target,
			Hand hand) {
		Result retour = castSpell(itemStack, target, null, player, null);
		return retour.resultType;
	}
	

	// On clic droit air
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		Result retour = castSpell(itemStack, null, world, player, null);
		return retour.result;
	}

	// On clic droit block
	@Override
	public ActionResultType useOn(ItemUseContext itemUseContext) {
		ItemStack itemStack = itemUseContext.getItemInHand();
		Result retour = castSpell(itemStack, null, null, itemUseContext.getPlayer(), itemUseContext);
		return retour.resultType;
	}
	

	private Result castSpell(@Nonnull ItemStack itemStack, LivingEntity target, World world, @Nonnull PlayerEntity player,
			ItemUseContext itemUseContext) {
		final Result retour = new Result(itemStack);
		if (!player.level.isClientSide) {
			itemStack.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(new NonNullConsumer<ISpell>() {
				@Override
				public void accept(@Nonnull ISpell iSpell) {
					Spell spell = iSpell.getSpell();
					if (spell != null) {
						if (spell.cast(itemStack, target, world, player, itemUseContext)) {
							try {
								retour.consume = (itemStack.getItem() == ModObjects.PARCHMENT.getModItem());
							} catch (NotAnItemException e) {
								e.printStackTrace();
							}
							if(retour.consume) {
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
				}
			});
		}
		if (player.level.isClientSide) {
			itemStack.getCapability(SpellCapability.SPELL_CAPABILITY).ifPresent(new NonNullConsumer<ISpell>() {
				@Override
				public void accept(@Nonnull ISpell iSpell) {
					Spell spell = iSpell.getSpell();
					if (spell != null) {
						if (spell.castable(itemStack, target, world, player, itemUseContext)) {
							try {
								retour.consume = (itemStack.getItem() == ModObjects.PARCHMENT.getModItem());
							} catch (NotAnItemException e) {
								e.printStackTrace();
							}
							if(retour.consume) {
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
				}
			});
		}
		if (retour.consume) {
			// TODO ca marche pas pk :(
			itemStack.shrink(1);
		}
		return retour;
	}
}
