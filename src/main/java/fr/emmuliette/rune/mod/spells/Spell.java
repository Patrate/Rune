package fr.emmuliette.rune.mod.spells;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.items.RuneItem;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.CastModContainerComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class Spell {
	private String name;
	private AbstractCastComponent<?> startingComponent;
	
	
	public Spell(String name, AbstractCastComponent<?> startingComponent) {
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

	public static Spell buildSpell(String name, List<RuneItem> runeList) throws RunePropertiesException {
		// TODO allow multiple castMod
		List<AbstractCastModComponent> castMods = new ArrayList<AbstractCastModComponent>();
		AbstractCastEffectComponent castComponent = null;
		AbstractCastComponent<?> start = null;
		List<AbstractEffectComponent> effects = new ArrayList<AbstractEffectComponent>();
		
		AbstractSpellComponent current;
		int step = 0;
		for(int i = 0; i < runeList.size(); i++) {
			current = runeList.get(i).getSpellComponent();
			switch(step) {
			case 0:
				if (current instanceof AbstractCastModComponent) {
					castMods.add((AbstractCastModComponent) current);
					break;
				} else
					step ++;
			case 1:
				if (!(current instanceof AbstractCastEffectComponent)) {
					return null;
				}
				castComponent = (AbstractCastEffectComponent) current;
				step++;
				break;
			case 2:
				if (!(current instanceof AbstractEffectComponent)) {
					return null;
				}
				effects.add((AbstractEffectComponent) current);
				break;
			default:
				System.out.println("UNRECOGNIZED STEP");
				return null;
			}
		}
		if(castMods.isEmpty()) {
			start = castComponent;
		} else {
			if(castMods.size() == 1) {
				start = castMods.get(0); 
				start.addChildren(castComponent);
			} else {
				start = new CastModContainerComponent();
				start.addChildren(castComponent);
				for(AbstractCastModComponent mod:castMods) {
					start.addChildren(mod);
				}
			}
		}
		for(AbstractEffectComponent effect:effects) {
			castComponent.addChildren(effect);
		}
		return new Spell(name, start);
		
		
		
		
		
		/*
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

		AbstractCastComponent<?> castComponent = (AbstractCastComponent<?>) runeList.get(0).getSpellComponent();
		for(AbstractSpellComponent component:previous) {
			castComponent.addChildren((AbstractEffectComponent) component);
		}
		return new Spell(name, castComponent);
		*/
	}
	
	public static boolean parseSpell(List<RuneItem> runeList) {
		if(runeList.size() < 2) {
			return false;
		}
		Class<? extends AbstractSpellComponent> current;
		int step = 0;
		for(int i = 0; i < runeList.size(); i++) {
			current = runeList.get(i).getComponentClass();
			switch(step) {
			case 0:
				if (AbstractCastModComponent.class.isAssignableFrom(current)) {
					break;
				} else
					step ++;
			case 1:
				if (!AbstractCastEffectComponent.class.isAssignableFrom(current)) {
					return false;
				}
				step++;
				break;
			case 2:
				if (!AbstractEffectComponent.class.isAssignableFrom(current)) {
					return false;
				}
				break;
			default:
				System.out.println("UNRECOGNIZED STEP");
				return false;
			}
		}
		return true;
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
		AbstractCastComponent<?> startingComponent = (AbstractCastComponent<?>) AbstractSpellComponent.fromNBT(startNBT);
		Spell retour = new Spell(name, startingComponent);
		return retour;
	}
}
