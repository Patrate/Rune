package fr.emmuliette.rune.mod.spells.renderer;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.spells.entities.MagicEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MagicEntityRenderer extends EntityRenderer<MagicEntity> {
	protected static final ResourceLocation IMAGE_LOCATION = new ResourceLocation(RuneMain.MOD_ID,
			"textures/gui/magic_circle.png");

	public MagicEntityRenderer(EntityRendererManager erm) {
		super(erm);
	}
	public ResourceLocation getTextureLocation(MagicEntity entity) {
		return IMAGE_LOCATION;
	}
}