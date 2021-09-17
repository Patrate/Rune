package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
import fr.emmuliette.rune.mod.gui.grimoire.CGrimoireSpellPacket.Action;
import fr.emmuliette.rune.mod.sync.SyncHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

public class SpellWidget extends Widget {

	private final GrimoireScreen parent;
	private final Grimoire grimoire;
	private List<SpellButton> buttons;
	private int spellId;

	public SpellWidget(GrimoireScreen parent, Grimoire grimoire, int spellId, int x, int y) {
		super(x, y, 135, 17, StringTextComponent.EMPTY);
		this.parent = parent;
		this.grimoire = grimoire;
		this.spellId = spellId;
		this.buttons = new ArrayList<SpellButton>(3);

		// Get spell
		this.buttons.add(new SpellButton(this, parent, grimoire, x, y, 0, 0, (button) -> {
			SpellButton sButton = (SpellButton) button;
			sButton.getParent().selectSpell();
		}));

		// Remove spell
		this.buttons.add(new SpellButton(this, parent, grimoire, x + 119, y, 16, 0, (button) -> {
			SpellButton sButton = (SpellButton) button;
			sButton.getParent().removeSpellServer();
		}));

		this.visible = (spellId != -1);
	}

	@Override
	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (getSpellId() == -1)
			return;
		ISpell spell = grimoire.getSpell(getSpellId());
		if (spell == null || spell.getSpell() == null)
			return;
		mStack.pushPose();
		parent.getFont().draw(mStack, spell.getSpell().getName(), x + 20, y + 3, 4210752);
		mStack.popPose();

		for (SpellButton b : buttons) {
			b.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
		}
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

	void selectSpell() {
		parent.selectSpell(spellId);
	}

	void removeSpellServer() {
		SyncHandler.sendToServer(new CGrimoireSpellPacket(spellId, Action.REMOVE));
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		for (SpellButton b : buttons) {
			if (b.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_))
				return true;
		}
		return false;
//		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}
}
