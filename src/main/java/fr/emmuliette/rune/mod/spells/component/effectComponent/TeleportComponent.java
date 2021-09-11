package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.exception.UnknownPropertyException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ManaCost;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.properties.RuneProperties;
import fr.emmuliette.rune.mod.spells.properties.common.BoolProperty;
import fr.emmuliette.rune.mod.spells.properties.variable.BlockPosProperty;
import fr.emmuliette.rune.mod.spells.tags.RestrictionTag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TeleportComponent extends AbstractEffectComponent {
	public TeleportComponent(AbstractSpellComponent parent) {
		super(PROPFACT, parent);
		this.addTag(RestrictionTag.VARIABLE);
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		Entity teleported;
		boolean teleportOther = this.getBoolProperty(KEY_OTHER);
		if (teleportOther)
			teleported = target;
		else
			teleported = context.getCaster();

		BlockPos pos = null;
		try {
			pos = ((BlockPosProperty) this.getProperty(KEY_POS)).getValue();
		} catch (UnknownPropertyException e) {
			e.printStackTrace();
		}
		if (pos == null) {
			if (teleportOther)
				pos = context.getCaster().blockPosition();
			else
				pos = target.blockPosition();
		}

		teleported.teleportTo(pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos position, SpellContext context) {
		if (this.getBoolProperty(KEY_OTHER))
			return false;
		context.getCaster().teleportTo(position.getX(), position.getY() + 1, position.getZ());
		return true;
	}

	@Override
	public Cost<?> getCost() {
		Cost<?> retour = new ManaCost(5);
		retour.add(super.getCost());
		return retour;
	}

	private static final String KEY_POS = "position", KEY_OTHER = "teleport_other";
	private static final PropertyFactory PROPFACT = new PropertyFactory() {
		@Override
		public RuneProperties buildInternal() {
			RuneProperties retour = new RuneProperties() {
				@Override
				protected void init() {
					this.addNewProperty(new BlockPosProperty(KEY_POS, 2));
					this.addNewProperty(new BoolProperty(KEY_OTHER, Grade.IRON, () -> new ManaCost(2)));
				}
			};
			return retour;
		}
	};
}
