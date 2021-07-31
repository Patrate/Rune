package fr.emmuliette.rune.mod.spells.AI.renderAI;

import java.awt.Color;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.AI.AbstractAI;
import fr.emmuliette.rune.mod.spells.entities.MagicEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;

public abstract class RenderAI extends AbstractAI {

	private static final Color[] colorList = new Color[] {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};
	
	public static Supplier<RenderAI> DEFAULT = () -> new RenderAI() {
		@Override
		public void render(MagicEntity entity) {
			for (int i = 0; i < 2; ++i) {
				float f1 = entity.getRandom().nextFloat() * ((float) Math.PI * 2F);
				float f2 = MathHelper.sqrt(entity.getRandom().nextFloat()) * 0.2F;
				float f3 = MathHelper.cos(f1) * f2;
				float f4 = MathHelper.sin(f1) * f2;
				Color c = colorList[entity.getRandom().nextInt(colorList.length)];
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				entity.level.addAlwaysVisibleParticle(ParticleTypes.ENTITY_EFFECT, entity.getX() + (double) f3,
						entity.getY(), entity.getZ() + (double) f4, (double) ((float) r / 255.0F),
						(double) ((float) g / 255.0F), (double) ((float) b / 255.0F));
			}
		}
	};

	public abstract void render(MagicEntity entity);
}
