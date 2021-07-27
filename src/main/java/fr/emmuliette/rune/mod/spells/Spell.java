package fr.emmuliette.rune.mod.spells;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.tags.SpellTag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Spell {
	private LivingEntity cacheTarget;
	private BlockPos cacheBlock;

	private String name;
	private AbstractCastComponent<?> startingComponent;
	private List<AbstractSpellComponent> components;
	private List<SpellTag> tags;
	private Cost<?> cost;

	public Spell(String name, AbstractCastComponent<?> startingComponent, List<AbstractSpellComponent> components,
			List<SpellTag> tags) {
		this.name = name;
		this.startingComponent = startingComponent;
		this.components = components;
		Set<SpellTag> uniqTag = new HashSet<SpellTag>();
		for (SpellTag tag : tags) {
			if (tag.isUnique())
				if (!uniqTag.add(tag)) {
					RuneMain.LOGGER.error("THE TAG " + tag + " MUST BE UNIQUE IN A SPELL");
					// TODO throw error
				}
		}
		this.tags = tags;
		int i = 0;
		for (AbstractSpellComponent component : components) {
			component.setSpellInternalId(i++);
		}
	}

	public String getName() {
		return name;
	}

	public Cost<?> getCost() {
		return startingComponent.getCost();
	}

	public Cost<?> getCostNew() {
		return cost;
	}

	public Boolean castSpecial(float power, ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(power, itemStack, target, world, caster, itemUseContext);
		return startingComponent.specialCast(context);
	}

	public Boolean castable(float power, ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			BlockPos block, ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(power, itemStack, target, world, caster, block, itemUseContext);
		Boolean retour = startingComponent.canCast(context);
		return retour;
	}

	public Boolean cast(float power, ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			BlockPos block, ItemUseContext itemUseContext) {
		SpellContext context = new SpellContext(power, itemStack, target, world, caster, block, itemUseContext);
		Boolean canCast = startingComponent.canCast(context);
		if (canCast == null || canCast == true) {
			return startingComponent.cast(context);
		}
		return false;
	}

	public void setPropertyValue(int componentId, String key, Object value) {
		components.get(componentId).setPropertyValue(key, value);
	}

	/*public <T> T getPropertyValue(int componentId, String key, T defaut) {
		return (T) components.get(componentId).getPropertyValue(key, defaut);
	}*/

	public List<SpellTag> getTags() {
		return tags;
	}

	public boolean hasTag(SpellTag tag) {
		return tags.contains(tag);
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
		if (this.components.size() != other.components.size()) {
			// TODO throw BAD SYNC error
			RuneMain.LOGGER.debug("SYNC ERROR HERE, DIFFERENT SIZES !");
			return;
		}
		for (int i = 0; i < this.components.size(); i++) {
			AbstractSpellComponent myComp = this.components.get(i);
			AbstractSpellComponent otherComp = other.components.get(i);
			myComp.syncProperties(otherComp);
		}
	}

	public LivingEntity getCacheTarget() {
		return cacheTarget;
	}

	public void setCacheTarget(LivingEntity cacheTarget) {
		this.cacheTarget = cacheTarget;
	}

	public BlockPos getCacheBlock() {
		return cacheBlock;
	}

	public void setCacheBlock(BlockPos cacheBlock) {
		this.cacheBlock = cacheBlock;
	}
	
	public float getMaxPower() {
		// TODO
		return 1f;
	}
	
	public Cost<?> getTickCost() {
		// TODO
		return this.getCost();
	}
}
