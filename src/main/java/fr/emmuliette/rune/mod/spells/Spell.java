package fr.emmuliette.rune.mod.spells;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.build.SpellBuilder;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;

public class Spell {
	private String name;
	private AbstractCastComponent<?> startingComponent;
	private List<AbstractSpellComponent> components;

	public Spell(String name, AbstractCastComponent<?> startingComponent, List<AbstractSpellComponent> components) {
		this.name = name;
		this.startingComponent = startingComponent;
		this.components = components;
	}

	public String getName() {
		return name;
	}

	public float getManaCost() {
		return startingComponent.getManaCost();
	}

	public Boolean castSpecial(ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(itemStack, target, world, caster, itemUseContext);
		return startingComponent.specialCast(context);
	}

	public Boolean castable(ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(itemStack, target, world, caster, itemUseContext);
		return startingComponent.canCast(context);
	}

	public Boolean cast(ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(itemStack, target, world, caster, itemUseContext);
		Boolean canCast = startingComponent.canCast(context);
		if (canCast == null || canCast == true) {
			return startingComponent.cast(context);
		}
		return false;
	}

	private static final String NBT_NAME = "NAME";
	private static final String NBT_COMPONENTS = "COMPONENTS";
	public static final String NBT_CLASS = "CLASS";
	public static final String NBT_PROPERTIES = "PROPERTIES";
	public static final String NBT_CHILDREN = "CHILDREN";

	public CompoundNBT toNBT() {
		CompoundNBT retour = new CompoundNBT();
		retour.putString(NBT_NAME, this.name);
		ListNBT componentList = new ListNBT();
		for (int i = 0; i < components.size(); i++) {
			componentList.add(components.get(i).toNBT());
		}
		retour.put(NBT_COMPONENTS, componentList);
		return retour;
	}

	public static Spell fromNBT(CompoundNBT data)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, RunePropertiesException {
		String name = data.getString(NBT_NAME);
		ListNBT componentsNBT = (ListNBT) data.get(NBT_COMPONENTS);
		List<AbstractSpellComponent> components = new ArrayList<AbstractSpellComponent>();
		for (int i = 0; i < componentsNBT.size(); i++) {
			components.add(AbstractSpellComponent.fromNBT(componentsNBT.getCompound(i)));
		}
		return SpellBuilder.buildSpell(name, components);
	}
	
	public void sync(Spell other) {
		if(this.components.size() != other.components.size()) {
			// TODO throw BAD SYNC error
			RuneMain.LOGGER.debug("SYNC ERROR HERE, DIFFERENT SIZES !");
			return;
		}
		for(int i = 0; i < this.components.size(); i++) {
			AbstractSpellComponent myComp = this.components.get(i);
			AbstractSpellComponent otherComp = other.components.get(i);
			myComp.syncProperties(otherComp);
		}
	}
}
