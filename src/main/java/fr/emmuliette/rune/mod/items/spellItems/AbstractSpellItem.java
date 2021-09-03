package fr.emmuliette.rune.mod.items.spellItems;

import javax.annotation.Nonnull;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.SpellCapabilityException;
import fr.emmuliette.rune.exception.SpellCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellCapability;
import fr.emmuliette.rune.mod.effects.ModEffects;
import fr.emmuliette.rune.mod.event.StopCastingEvent;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.tags.SpellTag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public abstract class AbstractSpellItem extends Item {
//	public static final String SPELL_ID = "spell_id";

	public static ItemStack getGrimoireSpell(Grimoire grimoire, int spellId) {
		return grimoire.getItem(spellId);
	}

	public AbstractSpellItem(Properties prop) {
		super(prop);
	}

	@Override
	public ITextComponent getName(ItemStack iStack) {
		String base;
		if (iStack.getItem() == ModItems.PARCHMENT.get()) {
			// TODO translation
			base = "Parchemin de ";
		} else if (iStack.getItem() == ModItems.GRIMOIRE.get()) {
			// TODO translation
			base = "Grimoire de ";
		} else if (iStack.getItem() == ModItems.SOCKET.get()) {
			// TODO translation
			base = "Socket de ";
		} else {
			base = "";
		}
		ISpell iSpell = iStack.getCapability(SpellCapability.SPELL_CAPABILITY).orElse(null);
		if (iSpell != null && iSpell.getSpell() != null)
			return new StringTextComponent(base + iSpell.getSpell().getName());
		return super.getName(iStack);
	}

	protected static class Result {
		ActionResult<ItemStack> result;
		ActionResultType resultType;
		boolean consume;

		public Result(ItemStack item) {
			result = ActionResult.pass(item);
			resultType = ActionResultType.PASS;
			consume = false;
		}
	}

	public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity caster) {
		// TODO on clic milieux, changer de mode si le spell est modal
		final Result retour = new Result(itemStack);
		if (!caster.level.isClientSide && itemStack.getItem() != ModItems.SOCKET.get()) {
			try {
				Spell spell = getSpell(itemStack, caster);
				if (spell != null) {
					if (spell.castSpecial(getPower(caster), itemStack, target, caster.level, caster, null)) {
						retour.consume = (itemStack.getItem() == ModItems.PARCHMENT.get());
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

	// @Override
	// public boolean hurtEnemy(ItemStack itemStack, LivingEntity target,
	// LivingEntity caster) {
	// TODO on clic milieux, changer de mode si le spell est modal
	/*
	 * final Result retour = new Result(itemStack); if (!caster.level.isClientSide)
	 * { try { ISpell cap =
	 * itemStack.getCapability(SpellCapability.SPELL_CAPABILITY) .orElseThrow(new
	 * SpellCapabilityExceptionSupplier(itemStack)); Spell spell = cap.getSpell();
	 * if (spell != null) { if (spell.castSpecial(itemStack, target, caster.level,
	 * caster, null)) { try { retour.consume = (itemStack.getItem() ==
	 * ModObjects.PARCHMENT.getModItem()); } catch (NotAnItemException e) {
	 * e.printStackTrace(); } if (retour.consume) { retour.resultType =
	 * ActionResultType.CONSUME; retour.result = ActionResult.consume(itemStack); }
	 * else { retour.resultType = ActionResultType.SUCCESS; retour.result =
	 * ActionResult.success(itemStack); } } else { retour.resultType =
	 * ActionResultType.PASS; } } } catch (SpellCapabilityException e) {
	 * e.printStackTrace(); } } // if(retour.resultType)
	 */
	// return super.hurtEnemy(itemStack, target, caster);
	// }

	// ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
	// ItemUseContext itemUseContext, Hand hand

	// On clic droit living entity
	@Override
	public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity caster, LivingEntity target,
			Hand hand) {
		if (itemStack.getItem() != ModItems.SOCKET.get()) {
			try {
				Spell spell = getSpell(itemStack, caster);
				if (learnSpell(itemStack, caster)) {
					return ActionResultType.CONSUME;
				} else {
					spell.setCacheTarget(target);
					Result retour = castSpell(spell, getPower(caster), itemStack, target, null, caster, null, null,
							hand);
					if ((spell.hasTag(SpellTag.CHARGING) || spell.hasTag(SpellTag.LOADING)
							|| spell.hasTag(SpellTag.CHANNELING)) && retour.resultType == ActionResultType.SUCCESS) {
						caster.startUsingItem(hand);
						return ActionResultType.PASS;
					}
					return retour.resultType;

				}
			} catch (SpellCapabilityException e) {
				e.printStackTrace();
			}
		}
		return ActionResultType.PASS;
	}

	// On clic droit air
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity caster, Hand hand) {
		ItemStack itemStack = caster.getItemInHand(hand);
		if (itemStack.getItem() != ModItems.SOCKET.get()) {
			try {
				Spell spell = getSpell(itemStack, caster);
				if (learnSpell(itemStack, caster)) {
					return ActionResult.consume(itemStack);
				} else {
					Result retour = castSpell(spell, getPower(caster), itemStack, null, world, caster, null, null,
							hand);
					if ((spell.hasTag(SpellTag.CHARGING) || spell.hasTag(SpellTag.LOADING)
							|| spell.hasTag(SpellTag.CHANNELING)) && retour.resultType == ActionResultType.SUCCESS) {
						caster.startUsingItem(hand);
						return ActionResult.pass(itemStack);
					}
					return retour.result;
				}
			} catch (SpellCapabilityException e) {
				e.printStackTrace();
			}
		}
		return ActionResult.pass(itemStack);
	}

	// On clic droit block
	@Override
	public ActionResultType useOn(ItemUseContext itemUseContext) {
		ItemStack itemStack = itemUseContext.getItemInHand();
		if (itemStack.getItem() != ModItems.SOCKET.get()) {
			try {
				Spell spell = getSpell(itemStack, itemUseContext.getPlayer());
				if (learnSpell(itemStack, itemUseContext.getPlayer())) {
					return ActionResultType.CONSUME;
				} else {
					spell.setCacheBlock(itemUseContext.getClickedPos());
					Result retour = castSpell(spell, getPower(itemUseContext.getPlayer()), itemStack, null, null,
							itemUseContext.getPlayer(), null, itemUseContext, itemUseContext.getHand());
					if ((spell.hasTag(SpellTag.CHARGING) || spell.hasTag(SpellTag.LOADING)
							|| spell.hasTag(SpellTag.CHANNELING)) && retour.resultType == ActionResultType.SUCCESS) {
						itemUseContext.getPlayer().startUsingItem(itemUseContext.getHand());
						return ActionResultType.PASS;
					}
					return retour.resultType;
				}
			} catch (SpellCapabilityException e) {
				e.printStackTrace();
			}
		}
		return ActionResultType.PASS;
	}

	protected boolean learnSpell(ItemStack item, Entity caster) {
//		if (Configuration.Server.learnFromGrimoire && item.getItem() == ModItems.GRIMOIRE.get())
		if (item.getItem() == ModItems.GRIMOIRE.get())
			return learnFromGrimoire(item, caster);
		return false;
	}

	private boolean learnFromGrimoire(ItemStack itemStack, Entity caster) {
		ICaster icaster = caster.getCapability(CasterCapability.CASTER_CAPABILITY).orElse((ICaster) null);
		ISpell grimoireSpell = itemStack.getCapability(SpellCapability.SPELL_CAPABILITY).orElse((ISpell) null);
		if (icaster == null || grimoireSpell == null)
			return false;
		if (!caster.level.isClientSide) {
			icaster.getGrimoire().addSpell(grimoireSpell);
			icaster.sync();
		}
		itemStack.shrink(1);
		return true;
	}

	// On release using
	@Override
	public void releaseUsing(ItemStack itemStack, World world, LivingEntity caster, int tick) {
		if (caster.level.isClientSide()) {
			return;
		}
		int chargeTime = this.getUseDuration(itemStack) - tick;
		if (chargeTime < 0)
			return;
		try {
			Spell spell = getSpell(itemStack, caster);
			if (spell.hasTag(SpellTag.CHANNELING) || spell.hasTag(SpellTag.LOADING)
					|| spell.hasTag(SpellTag.CHARGING)) {
				StopCastingEvent event = new StopCastingEvent(spell, caster, chargeTime);
				MinecraftForge.EVENT_BUS.post(event);
				return;
			}
		} catch (SpellCapabilityException e) {
			e.printStackTrace();
		}
	}

	public Result castSpell(Spell spell, float power, @Nonnull ItemStack itemStack, LivingEntity target, World world,
			@Nonnull LivingEntity caster, BlockPos block, ItemUseContext itemUseContext, Hand hand) {
		return internalcastSpell(spell, power, itemStack, target, world, caster, block, itemUseContext, hand);
	}

	protected final Result internalcastSpell(Spell spell, float power, @Nonnull ItemStack itemStack,
			LivingEntity target, World world, @Nonnull LivingEntity caster, BlockPos block,
			ItemUseContext itemUseContext, Hand hand) {
		final Result retour = new Result(itemStack);
		if (spell == null || caster.hasEffect(ModEffects.SILENCED.get())) {
			return retour;
		}
		if (!caster.level.isClientSide) {
			Boolean cont = spell.cast(power, itemStack, target, world, caster, block, itemUseContext,
					spell.hasTag(SpellTag.CHANNELING));
			if (cont == null) {
				retour.resultType = ActionResultType.SUCCESS;
				retour.result = ActionResult.success(itemStack);
			} else if (cont) {
				retour.consume = (itemStack.getItem() == ModItems.PARCHMENT.get());
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
		} else {
			Boolean cont = spell.castable(power, itemStack, target, world, caster, block, itemUseContext,
					spell.hasTag(SpellTag.CHANNELING));
			if (cont == null) {
				retour.resultType = ActionResultType.SUCCESS;
				retour.result = ActionResult.success(itemStack);
			} else if (cont) {
				retour.consume = (itemStack.getItem() == ModItems.PARCHMENT.get());
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
		if (retour.consume && !(caster instanceof PlayerEntity && ((PlayerEntity) caster).abilities.instabuild)) {
			itemStack.shrink(1);
		}
		return retour;
	}

	public static Spell getSpell(ItemStack iStack) throws SpellCapabilityException {
		ISpell cap = iStack.getCapability(SpellCapability.SPELL_CAPABILITY)
				.orElseThrow(new SpellCapabilityExceptionSupplier(iStack));
		return cap.getSpell();
	}

	protected Spell getSpell(ItemStack iStack, LivingEntity owner) throws SpellCapabilityException {
		return getSpell(iStack);
	}

	public static float getPowerForTime(int duration) {
		float f = (float) duration / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}

	public UseAction getUseAnimation(ItemStack iStack) {
		try {
			Spell spell = getSpell(iStack);
			if (spell.hasTag(SpellTag.CHANNELING)) {
				return UseAction.DRINK;
			} else if (spell.hasTag(SpellTag.CHARGING)) {
				return UseAction.BLOCK;
			} else if (spell.hasTag(SpellTag.LOADING)) {
				return UseAction.BOW;
			}
		} catch (SpellCapabilityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack iStack) {
		return 72000;
	}

	private float getPower(LivingEntity caster) {
		try {
			ICaster cap = caster.getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(caster));
			return cap.getPower();
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
			return 1f;
		}
	}

	public static boolean hasTag(ItemStack item, SpellTag tag) {
		Spell spell;
		try {
			spell = getSpell(item);
			if (spell == null)
				return false;
			return spell.hasTag(tag);
		} catch (SpellCapabilityException e) {
			e.printStackTrace();
			return false;
		}
	}
}
