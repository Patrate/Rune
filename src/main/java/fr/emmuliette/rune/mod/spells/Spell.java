package fr.emmuliette.rune.mod.spells;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
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

	public boolean castable(ItemStack itemStack, LivingEntity target, World world, PlayerEntity player,
			ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(itemStack, target, world, player, itemUseContext);
		return startingComponent.canCast(context);
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

		AbstractCastComponent castComponent = (AbstractCastComponent) runeList.get(0).getSpellComponent(runeList.get(0).getProperties());
		for(AbstractSpellComponent component:previous) {
			castComponent.addChildren((AbstractEffectComponent) component);
		}
		return new Spell(name, castComponent);
	}
	
	
	private static final String NBT_NAME = "NAME";
	private static final String NBT_START = "START";
	public static final String NBT_CLASS = "CLASS";
	public static final String NBT_PROPERTIES = "PROPERTIES";
	public static final String NBT_CHILDREN = "CHILDREN";
	
	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.putString(NBT_NAME, this.name);
		retour.put(NBT_START, startingComponent.toNBT());
		return retour;
	}
	
	public static Spell fromNBT(CompoundNBT data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String name = data.getString(NBT_START);
		CompoundNBT startNBT = data.getCompound(NBT_START);
		AbstractCastComponent startingComponent = (AbstractCastComponent) AbstractSpellComponent.fromNBT(startNBT);
		Spell retour = new Spell(name, startingComponent);
		return retour;
	}
}
