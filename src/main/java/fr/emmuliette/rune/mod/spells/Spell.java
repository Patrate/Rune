package fr.emmuliette.rune.mod.spells;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.tags.SpellTag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
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
		SpellContext context = new SpellContext(power, itemStack, target, world, caster, itemUseContext, null);
		return startingComponent.specialCast(context);
	}

	public Boolean castable(float power, ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			BlockPos block, ItemUseContext itemUseContext, boolean channeling) {
		SpellContext context = new SpellContext(power, itemStack, target, world, caster, block, itemUseContext, null);
		Boolean retour = startingComponent.canCast(context);
		return retour;
	}

	public Boolean cast(float power, ItemStack itemStack, LivingEntity target, World world, LivingEntity caster,
			BlockPos block, ItemUseContext itemUseContext, boolean channeling) {
		return castSocketItem(power, itemStack, target, world, caster, block, itemUseContext, channeling, null);
	}

	public Boolean castSocketItem(float power, ItemStack itemStack, LivingEntity target, World world,
			LivingEntity caster, BlockPos block, ItemUseContext itemUseContext, boolean channeling,
			ItemStack socketItem) {
		SpellContext context = new SpellContext(power, itemStack, target, world, caster, block, itemUseContext,
				socketItem);
		Boolean canCast = startingComponent.canCast(context);
		if (canCast == null || canCast == true) {
			if (caster != null)
				drawEffect(caster);
			return startingComponent.cast(context);
		}
		return false;
	}

	private static final Color[] colorList = new Color[] { Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA,
			Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW };

	private void drawEffect(LivingEntity entity) {
		for (int i = 0; i < 2; ++i) {
			float f1 = entity.getRandom().nextFloat() * ((float) Math.PI * 2F);
			float f2 = MathHelper.sqrt(entity.getRandom().nextFloat()) * 0.2F;
			float f3 = MathHelper.cos(f1) * f2;
			float f4 = MathHelper.sin(f1) * f2;
			Color c = colorList[entity.getRandom().nextInt(colorList.length)];
			int r = c.getRed();
			int g = c.getGreen();
			int b = c.getBlue();
			entity.level.addAlwaysVisibleParticle(ParticleTypes.ENTITY_EFFECT, entity.getX() + (double) f3,
					entity.getY(), entity.getZ() + (double) f4, (double) ((float) r / 255.0F),
					(double) ((float) g / 255.0F), (double) ((float) b / 255.0F));
		}
	}

	public void setPropertyValue(int componentId, String key, Object value) {
		components.get(componentId).setPropertyValue(key, value);
	}

	/*
	 * public <T> T getPropertyValue(int componentId, String key, T defaut) { return
	 * (T) components.get(componentId).getPropertyValue(key, defaut); }
	 */

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
		if (componentsNBT != null) {
			for (int i = 0; i < componentsNBT.size(); i++) {
				components.add(AbstractSpellComponent.fromNBT(componentsNBT.getCompound(i)));
			}
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
		// TODO maxPower
		return 1f;
	}

	public Cost<?> getTickCost() {
		// TODO tickCost
		return this.getCost();
	}

	public AbstractCastComponent<?> getStartingComponent() {
		return startingComponent;
	}

	public Collection<? extends ITextComponent> getTooltips() {
		List<ITextComponent> toolTip = new ArrayList<ITextComponent>();
		for (AbstractSpellComponent component : this.components) {
			toolTip.addAll(component.getTooltips());
		}
		return toolTip;
	}
}
