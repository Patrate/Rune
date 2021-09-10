package fr.emmuliette.rune.mod.items.magicItems;

import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class WandItem extends MagicTieredItem {
	private static final String CURRENT_SPELL = "current_spell";

	public WandItem(ItemTier tier, Item.Properties prop) {
		super(tier, prop);
	}

	@Override
	public boolean isValidRepairItem(ItemStack item, ItemStack p_82789_2_) {
		return false;
	}

	public static Spell getCurrentSpell(ItemStack wandItem, PlayerEntity player) {
		ICaster caster = player.getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
		if (caster != null && !caster.getGrimoire().getSpells().isEmpty()) {
			CompoundNBT tag = wandItem.getOrCreateTag();
			if (!tag.contains(CURRENT_SPELL))
				tag.putInt(CURRENT_SPELL, 0);
			int spellId = tag.getInt(CURRENT_SPELL);
			if(caster.getGrimoire().getSpell(spellId) == null) {
				tag.putInt(CURRENT_SPELL, 0);
				spellId = 0;
			}
			return caster.getGrimoire().getSpell(spellId).getSpell();
		}
		return (Spell) null;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack wandItem = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			nextSpell(wandItem, player);
			return ActionResult.pass(wandItem);
		}
		ICaster caster = player.getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
		Spell wandSpell = getCurrentSpell(wandItem, player);
		if (caster != null && wandSpell != null) {
			if (wandSpell.cast(caster.getPower(), wandItem, null, world, player, null, null, false)) {
				return ActionResult.success(wandItem);
			}
			return ActionResult.pass(wandItem);
		} else
			return super.use(world, player, hand);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		World world = context.getLevel();
		ItemStack wandItem = context.getItemInHand();
		if (player.isShiftKeyDown()) {
			nextSpell(wandItem, player);
			return ActionResultType.PASS;
		}
		ICaster caster = player.getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
		Spell wandSpell = getCurrentSpell(wandItem, player);
		if (caster != null && wandSpell != null) {
			if (wandSpell.cast(caster.getPower(), wandItem, null, world, player, context.getClickedPos(), context,
					false)) {
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		} else
			return super.useOn(context);
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack wandItem, PlayerEntity player, LivingEntity target,
			Hand hand) {
		if (player.isShiftKeyDown()) {
			nextSpell(wandItem, player);
			return ActionResultType.PASS;
		}
		World world = player.level;
		ICaster caster = player.getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
		Spell wandSpell = getCurrentSpell(wandItem, player);
		if (caster != null && wandSpell != null) {
			if (wandSpell.cast(caster.getPower(), wandItem, target, world, player, null, null, false)) {
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		} else
			return super.interactLivingEntity(wandItem, player, target, hand);
	}

	private static void nextSpell(ItemStack wandItem, PlayerEntity player) {
		CompoundNBT tag = wandItem.getOrCreateTag();
		if (!tag.contains(CURRENT_SPELL))
			tag.putInt(CURRENT_SPELL, 0);
		ICaster caster = player.getCapability(CasterCapability.CASTER_CAPABILITY).orElse(null);
		if (caster != null) {
			tag.putInt(CURRENT_SPELL, (tag.getInt(CURRENT_SPELL) + 1) % caster.getGrimoire().grimoireSize());
		}
	}

	@Override
	public UseAction getUseAnimation(ItemStack item) {
		return UseAction.SPEAR;
	}
}
