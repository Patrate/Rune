package fr.emmuliette.rune.mod.items.spellItems;

import java.util.Iterator;

import javax.annotation.Nonnull;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.GrimoireSpellException;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.StringNBT;
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

	public static ItemStack getGrimoireSpell(Grimoire source, String name) {
		GrimoireSpellItem spellitem;
		spellitem = (GrimoireSpellItem) ModItems.SPELL.getItem();
		ItemStack itemStack = new ItemStack(spellitem);
		itemStack.addTagElement(SPELL_NAME, StringNBT.valueOf(name));
		return itemStack;
	}

	public GrimoireSpellItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Spell getSpell(ItemStack itemStack, LivingEntity owner) throws GrimoireSpellException {
		ICaster caster = owner.getCapability(CasterCapability.CASTER_CAPABILITY)
				.orElseThrow(GrimoireSpellException::new);
		if (itemStack.hasTag() && itemStack.getTag().contains(SPELL_NAME))
			return caster.getGrimoire().getSpell(itemStack.getTag().getString(SPELL_NAME)).getSpell();
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
