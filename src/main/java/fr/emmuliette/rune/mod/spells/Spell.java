package fr.emmuliette.rune.mod.spells;

import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
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

	public boolean cast(ItemStack itemStack, LivingEntity target, World world, PlayerEntity player,
			ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(itemStack, target, world, player, itemUseContext);
		if (startingComponent.canCast(context)) {
			return startingComponent.cast(context);
		}
		return false;
	}

	public static Spell buildSpell(String name, List<RuneItem> runeList) throws RunePropertiesException {
		AbstractSpellComponent current = null;
		List<AbstractSpellComponent> previous = new ArrayList<AbstractSpellComponent>();
		for (int i = runeList.size() - 1; i > 0; i--) {
			current = runeList.get(i).getSpellComponent(runeList.get(i).getProperties());
			if (!(current instanceof ComponentContainer)) {
				previous.add(current);
			} else {
				if (previous.isEmpty()) {
					System.out.println("Attention ContainerComponent sans components !");
				} else {
					@SuppressWarnings("unchecked")
					ComponentContainer<AbstractSpellComponent> container = (ComponentContainer<AbstractSpellComponent>) current;
					for (AbstractSpellComponent previousCompo : previous) {
						if (container.canAddChildren(previousCompo)) {
							container.addChildren(previousCompo);
						} else {
							System.out.println("Erreur, can't add this component !");
						}
					}
					previous.clear();
					previous.add(current);
				}
			}
		}

		RuneProperties properties = runeList.get(0).getProperties();
		properties.setProperty(RuneProperties.Property.PREVIOUS, previous.get(0));
		AbstractCastComponent castComponent = (AbstractCastComponent) runeList.get(0).getSpellComponent(properties);
		return new Spell(name, castComponent);
	}
}
