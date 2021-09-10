package fr.emmuliette.rune.mod.items;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (player.isCreative()) {
			ItemStack item = player.getItemInHand(hand);
			switch (RuneItem.getGrade(item)) {
			case DIAMOND:
				RuneItem.setGrade(item, Grade.NETHERITE);
				break;
			case GOLD:
				RuneItem.setGrade(item, Grade.DIAMOND);
				break;
			case IRON:
				RuneItem.setGrade(item, Grade.GOLD);
				break;
			case NETHERITE:
				RuneItem.setGrade(item, Grade.WOOD);
				break;
			case STONE:
				RuneItem.setGrade(item, Grade.IRON);
				break;
			case WOOD:
				RuneItem.setGrade(item, Grade.STONE);
				break;
			case SECRET:
			case UNKNOWN:
			default:
				return super.use(world, player, hand);
			}
			return ActionResult.success(player.getItemInHand(hand));
		} else
			return super.use(world, player, hand);
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

	private static final String GRADE_KEY = "grade";

	private static String getGradeTag(ItemStack stack) {
		CompoundNBT tags = stack.getOrCreateTag();
		if (!tags.contains(GRADE_KEY))
			tags.putString(GRADE_KEY, Grade.WOOD.name());
		return tags.getString(GRADE_KEY);
	}

	public static Grade getGrade(ItemStack stack) {
		if (!(stack.getItem() instanceof RuneItem)) {
			return Grade.UNKNOWN;
		}
		return Grade.valueOf(getGradeTag(stack));
	}

	public static void setGrade(ItemStack stack, Grade grade) {
		stack.getOrCreateTag().putString("grade", grade.name());
	}
}
