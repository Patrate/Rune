package fr.emmuliette.rune.mod.spells;

import fr.emmuliette.rune.mod.items.SpellItem;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.world.World;

public class Spell {
	private String name;
	private AbstractCastComponent startingComponent;

	public Spell(String name, AbstractCastComponent startingComponent) {
		this.name = name;
		this.startingComponent = startingComponent;
	}
	
	public String getName() {
		return name;
	}

	public SpellItem getItem() {
		return SpellItem.buildSpellItem(this);
	}
	
	public boolean cast(ItemStack itemStack, LivingEntity target, World world, PlayerEntity player, ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(itemStack, target, world, player, itemUseContext);
		if(startingComponent.canCast(context)) {
			return startingComponent.cast(context);
		}
		return false;
	}
}
