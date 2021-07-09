package fr.emmuliette.rune.mod.items;

import fr.emmuliette.rune.mod.ModObjects;
import fr.emmuliette.rune.mod.NotAnItemException;
import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SpellItem extends Item {
	private Spell spell;

	public static ItemStack buildSpellItem(Spell spell) {
		SpellItem spellitem;
		try {
			spellitem = (SpellItem) ModObjects.SPELL.getModItem();
			spellitem.spell = spell;
			return new ItemStack(spellitem);
		} catch (NotAnItemException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public SpellItem(Item.Properties properties) {
		super(properties);
		spell = null;
	}

	// On clic gauche living entity
	/*@Override
	public boolean hurtEnemy(ItemStack p_77644_1_, LivingEntity p_77644_2_, LivingEntity p_77644_3_) {
		System.out.println("hurtEnemy");
		return super.hurtEnemy(p_77644_1_, p_77644_2_, p_77644_3_);
	}*/
	
	@Override
	public boolean isFoil(ItemStack itemStack) {
		return spell != null;
	}
	
	// TODO on clic milieux, changer de mode si le spell est modal

	// On clic droit living entity
	@Override
	public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity target, Hand hand) {
		if(spell != null) {
			return (spell.cast(itemStack, target, null, player, null))?ActionResultType.SUCCESS:ActionResultType.PASS;
		}
		return ActionResultType.PASS;
	}
	
	// On clic droit air
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if(spell != null) {
			return (spell.cast(null, null, world, player, null))?ActionResult.success(itemstack):ActionResult.pass(itemstack);
		}
		return ActionResult.pass(itemstack);
	}

	// On clic droit block
	@Override
	public ActionResultType useOn(ItemUseContext itemUseContext) {
		if(spell != null) {
			return (spell.cast(null, null, null, null, itemUseContext))?ActionResultType.SUCCESS:ActionResultType.PASS;
		}
		return ActionResultType.PASS;
	}
}
