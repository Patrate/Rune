package fr.emmuliette.rune.mod.items;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RuneItem extends Item {
	private final Class<? extends AbstractSpellComponent> component;

	public RuneItem(Class<? extends AbstractSpellComponent> component, Item.Properties properties) {
		super(properties);
		this.component = component;
	}

	public Class<? extends AbstractSpellComponent> getComponentClass() {
		return component;
	}

	@Override
	public void onCraftedBy(ItemStack stack, World world, PlayerEntity player) {
		// TODO ?
		super.onCraftedBy(stack, world, player);
	}

	public AbstractSpellComponent getSpellComponent() {
		try {
			AbstractSpellComponent comp = component.getConstructor(AbstractSpellComponent.class)
					.newInstance((AbstractSpellComponent) null);
			comp.initProperties();
			return comp;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			RuneMain.LOGGER.debug("SPELL COMP CLASS IS " + component.getCanonicalName());
			e.printStackTrace();
			return null;
		}
	}

	public static Grade getGrade(ItemStack stack) {
		if (!(stack.getItem() instanceof RuneItem)) {
			return Grade.UNKNOWN;
		}
		return (stack.getTag() != null && stack.getTag().contains("grade"))
				? Grade.valueOf(stack.getTag().getString("grade"))
				: Grade.WOOD;
	}

	public static void setGrade(ItemStack stack, Grade grade) {
		stack.getOrCreateTag().putString("grade", grade.name());
	}
}
