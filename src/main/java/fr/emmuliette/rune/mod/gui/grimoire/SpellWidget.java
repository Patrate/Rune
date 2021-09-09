package fr.emmuliette.rune.mod.gui.grimoire;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

public class SpellWidget extends Widget {
	private final GrimoireScreen parent;
	private final Grimoire grimoire;
	private SpellButton spellButton;
	private int spellId;

	public SpellWidget(GrimoireScreen parent, Grimoire grimoire, int spellId, int x, int y) {
		super(x, y, 135, 14, StringTextComponent.EMPTY);
		this.parent = parent;
		this.grimoire = grimoire;
		this.spellId = spellId;
		this.spellButton = new SpellButton(this, parent, grimoire, x, y);
		this.visible = (spellId != -1);
	}

	@Override
	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (getSpellId() == -1)
			return;
		ItemStack item = grimoire.getItem(getSpellId());
		if (item == null || item == ItemStack.EMPTY)
			return;
		mStack.pushPose();
		parent.getFont().draw(mStack, item.getItem().getName(item), x + 20, y + 2, 4210752);
		mStack.popPose();

		this.spellButton.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	void setSpellId(int spellId) {
		this.spellId = spellId;
		this.visible = (spellId != -1);
	}

	int getSpellId() {
		return spellId;
	}

	Minecraft getMinecraft() {
		return parent.getMinecraft();
	}

	void getSpellServer(int spellId) {
		parent.getSpellServer(spellId);
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		if (spellButton.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
			return true;
		}
		return false;
//		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}
}
