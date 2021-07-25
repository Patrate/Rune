package fr.emmuliette.rune.mod.items;

import java.util.Iterator;

import javax.annotation.Nonnull;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.NotAnItemException;
import fr.emmuliette.rune.mod.ModObjects;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.caster.grimoire.Grimoire;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.capability.GrimoireSpellException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE)
public class GrimoireSpellItem extends AbstractSpellItem {
	private static final String SPELL_ID = "spell_id";

	public static ItemStack getGrimoireSpell(Grimoire source, int id) {
		GrimoireSpellItem spellitem;
		try {
			spellitem = (GrimoireSpellItem) ModObjects.SPELL.getModItem();
			ItemStack itemStack = new ItemStack(spellitem);
			itemStack.addTagElement(SPELL_ID, IntNBT.valueOf(id));
			// Mettre tous les paramètres du spellItem ici ptet
			// itemStack.setHoverName(new StringTextComponent(spell.getName()));
			return itemStack;
		} catch (NotAnItemException e) {
			e.printStackTrace();
		}
		return ItemStack.EMPTY;
	}

	public GrimoireSpellItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Spell getSpell(ItemStack itemStack, LivingEntity owner) throws GrimoireSpellException {
		ICaster caster = owner.getCapability(CasterCapability.CASTER_CAPABILITY)
				.orElseThrow(GrimoireSpellException::new);
		if (itemStack.getTag().contains(SPELL_ID))
			return caster.getGrimoire().getSpell(itemStack.getTag().getInt(SPELL_ID)).getSpell();
		throw new GrimoireSpellException(itemStack);
	}

	@Override
	public Result castSpell(Spell spell, float power, @Nonnull ItemStack itemStack, LivingEntity target, World world,
			@Nonnull LivingEntity caster, BlockPos block, ItemUseContext itemUseContext, Hand hand) {
		return internalcastSpell(spell, power, itemStack, target, world, caster, block, itemUseContext, hand);
	}

	@SubscribeEvent
	public void onDropItemToss(ItemTossEvent event) {
		if (!(event.getEntityItem().getItem().getItem() instanceof GrimoireSpellItem)) {
			return;
		}
		event.setCanceled(true);
	}

	@SubscribeEvent
	public void onDropItemDeath(LivingDropsEvent event) {
		Iterator<ItemEntity> iie = event.getDrops().iterator();
		while (iie.hasNext()) {
			ItemEntity ie = iie.next();
			if (ie.getItem().getItem() instanceof GrimoireSpellItem) {
				iie.remove();
			}
		}
	}
}
