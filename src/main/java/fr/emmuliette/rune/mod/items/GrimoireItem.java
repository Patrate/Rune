package fr.emmuliette.rune.mod.items;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.gui.GrimoireBookScreen;
import fr.emmuliette.rune.mod.gui.scripting.RuneUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class GrimoireItem extends WrittenBookItem {
	private static final CompoundNBT MASTER_TAGS = new CompoundNBT();

	public GrimoireItem(Properties p_i48454_1_) {
		super(p_i48454_1_);
		MASTER_TAGS.putString("title", "Grimoire");
		MASTER_TAGS.putString("author", "Emma");
		ListNBT pageNbt = new ListNBT();
		ListNBT runeNbt = new ListNBT();
		for (ModItems runeItem : RuneUtils.runeItems) {
			// ModItems.valueOf(getDescriptionId())f
			runeNbt.add(StringNBT.valueOf(runeItem.name()));
			pageNbt.add(StringNBT.valueOf(runeItem.get().getRegistryName().toString()));
		}
		MASTER_TAGS.put("rune", runeNbt);
		MASTER_TAGS.put("pages", pageNbt);
	}

	@Override
	public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
		ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
		itemstack.setTag(MASTER_TAGS);
//		if (WrittenBookItem.resolveBookComponents(itemstack, this.createCommandSourceStack(), this)) {
//			this.containerMenu.broadcastChanges();
//		}

//		this.connection.send(new SOpenBookWindowPacket(p_184814_2_));
		Minecraft.getInstance().setScreen(new GrimoireBookScreen(new GrimoireBookScreen.GrimoireInfo(itemstack)));
		return super.use(p_77659_1_, p_77659_2_, p_77659_3_);
	}

}
