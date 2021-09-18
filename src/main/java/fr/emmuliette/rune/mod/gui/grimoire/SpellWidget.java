package fr.emmuliette.rune.mod.gui.grimoire;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import fr.emmuliette.rune.mod.capabilities.spell.ISpell;
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
		this.buttons.add(new SpellButton(this, x, y, 0, 0, (button) -> {
			((SpellButton) button).getParent().selectSpell();
		}));

		// Up button
		this.buttons.add(new SpellButton(this, x + 119, y, 0, 32, (button) -> {
			((SpellButton) button).getParent().moveSpellUp();
		}) {
			@Override
			protected boolean isVisible() {
				return getParent().canMoveUp();
			}
		});

		// Down button
		this.buttons.add(new SpellButton(this, x + 105, y, 16, 32, (button) -> {
			((SpellButton) button).getParent().moveSpellDown();
		}) {
			@Override
			protected boolean isVisible() {
				return getParent().canMoveDown();
			}
		});

		this.visible = (spellId != -1);
	}

	public void updatePos(boolean menuOpen, int leftPos) {
		int diff = leftPos - x;
		this.x = leftPos;
		for (SpellButton b : buttons) {
			b.x += diff;
		}
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
			if (b.isVisible())
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

	private void selectSpell() {
		parent.selectSpell(spellId);
	}

	private boolean canMoveUp() {
		if (spellId < 0)
			return false;
		return (spellId + 1) < grimoire.grimoireSize();
	}

	private boolean canMoveDown() {
		if (spellId <= 0)
			return false;
		return spellId > 0;
	}

	private void moveSpellUp() {
		if (spellId < 0)
			return;
		grimoire.moveSpell(spellId, spellId + 1);
	}

	private void moveSpellDown() {
		if (spellId < 0)
			return;
		grimoire.moveSpell(spellId, spellId - 1);
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		for (SpellButton b : buttons) {
			if (b.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_))
				return true;
		}
		return false;
	}
}
