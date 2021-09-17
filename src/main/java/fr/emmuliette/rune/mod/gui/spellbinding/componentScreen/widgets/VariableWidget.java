package fr.emmuliette.rune.mod.gui.spellbinding.componentScreen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.variable.VariableProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public abstract class VariableWidget<U, T extends VariableProperty<U>> extends PropertyWidget<T>
		implements IContainerListener {
	private static final ResourceLocation SPELL_BUTTON_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/spell_buttons.png");
	private final IInventory inInventory = new Inventory(1);
	private final Container inContainer = new InternalContainer();
	private final Minecraft minecraft = Minecraft.getInstance();

	class InternalContainer extends Container {
		public InternalContainer() {
			super(null, 0);
			this.addSlot(new Slot(inInventory, 0, x + 25, y + 15));
		}

		@Override
		public boolean stillValid(PlayerEntity player) {
			return true;
		}
	}

	protected VariableWidget(Grade grade, T property, AbstractSpellComponent component, int x, int y) {
		super(grade, property, component, x, y, 1);
		inContainer.addSlotListener(this);
//		valueSlot = new Slot(inInventory, 0, x + 25, y + 15);
	}

	@Override
	protected void internalRender(MatrixStack mStack, int a, int b, float c) {
//		if(!this.getProperty().isVariable())
//			valueSlot.render()

		minecraft.getTextureManager().bind(SPELL_BUTTON_LOCATION);
		mStack.pushPose();
		renderCase(mStack, x + 25, y + 15);
		mStack.popPose();
	}

//	@Override
//	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
//		if(inContainer.clicked(p_231044_1_, p_231044_3_, p_231044_5_, minecraft.player))
//			return true;
//		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
//	}

	@Override
	protected void internalClic(double x, double y) {
		System.out.println("YO");
//		try {
//			this.getProperty().setValueInternal(!this.getProperty().getValue());
//		} catch (PropertyException e) {
//			e.printStackTrace();
//		}
	}

	private void renderCase(MatrixStack mStack, int x, int y) {
		this.blit(mStack, x - 1, y - 1, 0, 0, 15, 15);
	}

	@Override
	public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {
	}

	@Override
	public void slotChanged(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
	}

	@Override
	public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
	}
}
