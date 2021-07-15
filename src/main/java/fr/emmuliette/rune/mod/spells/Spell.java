package fr.emmuliette.rune.mod.spells;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.exception.SpellBuildingException;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.ICastableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class Spell {
	private String name;
	private ICastableComponent startingComponent;
	
	
	public Spell(String name, ICastableComponent startingComponent) {
		this.name = name;
		this.startingComponent = startingComponent;
	}

	public String getName() {
		return name;
	}
	
	public float getManaCost() {
		return startingComponent.getManaCost();
	}

	public boolean castSpecial(ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(itemStack, target, world, caster, itemUseContext);
		return startingComponent.specialCast(context);
	}
	
	public boolean castable(ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(itemStack, target, world, caster, itemUseContext);
		return startingComponent.canCast(context);
	}
	
	public boolean cast(ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(itemStack, target, world, caster, itemUseContext);
		if (startingComponent.canCast(context)) {
			return startingComponent.cast(context);
		}
		return false;
	}

	public static Spell buildSpell(String name, List<RuneItem> runeList) throws RunePropertiesException, SpellBuildingException {
		AbstractSpellComponent current = null;
		List<AbstractSpellComponent> previous = new ArrayList<AbstractSpellComponent>();
		for (int i = runeList.size() - 1; i > 0; i--) {
			current = runeList.get(i).getSpellComponent();
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

		ICastableComponent castComponent = (ICastableComponent) runeList.get(0).getSpellComponent();
		if(castComponent instanceof ComponentContainer) {
			ComponentContainer<?> container = (ComponentContainer<?>) castComponent;
			for(AbstractSpellComponent component:previous) {
				if(container.canAddChildren(component)) {
					container.addChildren(component);
				} else {
					throw new SpellBuildingException("Can't add the component " + component + " to the container " + container);
				}
			}
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
