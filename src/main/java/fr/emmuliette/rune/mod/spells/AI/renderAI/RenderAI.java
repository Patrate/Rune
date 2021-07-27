package fr.emmuliette.rune.mod.spells.AI.renderAI;

import java.util.Random;
import java.util.function.Supplier;

import fr.emmuliette.rune.mod.spells.AI.AbstractAI;
import fr.emmuliette.rune.mod.spells.entities.MagicEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo.Color;

public abstract class RenderAI extends AbstractAI {
	public static Supplier<RenderAI> DEFAULT = () -> new RenderAI() {
		@Override
		public void render(MagicEntity entity, Random random) {
			for (int i = 0; i < 2; ++i) {
				float f1 = random.nextFloat() * ((float) Math.PI * 2F);
				float f2 = MathHelper.sqrt(random.nextFloat()) * 0.2F;
				float f3 = MathHelper.cos(f1) * f2;
				float f4 = MathHelper.sin(f1) * f2;
				int j = random.nextBoolean()? random.nextBoolean() ? Color.BLUE.ordinal() : Color.GREEN.ordinal(): random.nextBoolean()? Color.PINK.ordinal():Color.PURPLE.ordinal();
				int k = j >> 16 & 255;
				int l = j >> 8 & 255;
				int i1 = j & 255;
				entity.level.addAlwaysVisibleParticle(ParticleTypes.ENTITY_EFFECT, entity.getX() + (double) f3, entity.getY(),
						entity.getZ() + (double) f4, (double) ((float) k / 255.0F), (double) ((float) l / 255.0F),
						(double) ((float) i1 / 255.0F));
			}
		}
	};
	public abstract void render(MagicEntity entity, Random random);
}
