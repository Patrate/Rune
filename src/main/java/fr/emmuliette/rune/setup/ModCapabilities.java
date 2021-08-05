package fr.emmuliette.rune.setup;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.CasterImpl;
import fr.emmuliette.rune.mod.capabilities.caster.CasterStorage;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.capabilities.spell.SpellCapability;
import fr.emmuliette.rune.mod.capabilities.spell.SpellImpl;
import fr.emmuliette.rune.mod.capabilities.spell.SpellStorage;
import fr.emmuliette.rune.mod.items.spellItems.SpellItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

//@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE) // , value = Dist.CLIENT)
public class ModCapabilities {

	public static void register() {
		RuneMain.LOGGER.info("Registering mod capabilities");
		CapabilityManager.INSTANCE.register(ISpell.class, new SpellStorage(), SpellImpl::new);
		CapabilityManager.INSTANCE.register(ICaster.class, new CasterStorage(), CasterImpl::new);
	}

	@SubscribeEvent
	public static void attachCapabilitiesItem(final AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof SpellItem) {
			event.addCapability(new ResourceLocation(RuneMain.MOD_ID, SpellCapability.SPELL_CAPABILITY_NAME),
					new SpellCapability());
		}
	}

	@SubscribeEvent
	public static void attachCapabilitiesPlayer(final AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity) {
			event.addCapability(new ResourceLocation(RuneMain.MOD_ID, CasterCapability.CASTER_CAPABILITY_NAME),
					new CasterCapability((Entity) event.getObject()));
		}
	}

}
