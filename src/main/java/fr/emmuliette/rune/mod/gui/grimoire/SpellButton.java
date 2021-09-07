package fr.emmuliette.rune.mod.gui.grimoire;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.emmuliette.rune.mod.capabilities.caster.Grimoire;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SpellButton extends ImageButton {
	private static final ResourceLocation SPELL_BUTTON_LOCATION = new ResourceLocation(
			"textures/gui/recipe_button.png");
	private final GrimoireScreen parent;
	private final Grimoire grimoire;
	private int spellId;

	public SpellButton(GrimoireScreen parent, Grimoire grimoire, int spellId, int x, int y) {
		super(x, y, 20, 18, 0, 0, 19, SPELL_BUTTON_LOCATION, (button) -> {
			parent.getSpellServer(spellId);
			// TODO
			// parent.selectSpell(spellId);
		});
		this.parent = parent;
		this.grimoire = grimoire;
		this.spellId = spellId;
	}

	@Override
	public void render(MatrixStack mStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if(spellId == -1)
			return;
		mStack.pushPose();
		parent.getMinecraft().getTextureManager().bind(GrimoireScreen.GRIMOIRE_LOCATION);
		this.blit(mStack, x, y, 0, 222, 18, 18);
		mStack.popPose();
		mStack.pushPose();
		ItemStack item = grimoire.getItem(spellId);
		parent.getFont().draw(mStack, item.getItem().getName(item), x + 20, y + 2, 4210752);
		mStack.popPose();
		super.render(mStack, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	public void setSpellId(int spellId) {
		this.spellId = spellId;
	}
}
