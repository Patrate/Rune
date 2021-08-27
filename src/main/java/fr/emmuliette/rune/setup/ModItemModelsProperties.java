package fr.emmuliette.rune.setup;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.items.ModItems;
import fr.emmuliette.rune.mod.items.RuneItem;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModItemModelsProperties {
	public static final ResourceLocation gradeRL = new ResourceLocation(RuneMain.MOD_ID, "grade");

	public static void register() {
		for (ModItems mItem : ModItems.values()) {
			if (mItem.get() instanceof RuneItem) {
				ItemModelsProperties.register(mItem.get(), gradeRL, (stack, world, living) -> {
					return RuneItem.getGrade(stack).getLevel();
				});
			}
		}
	}
}