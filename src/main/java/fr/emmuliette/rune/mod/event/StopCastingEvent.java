package fr.emmuliette.rune.mod.event;

import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class StopCastingEvent extends Event{
	private Spell spell;
	private LivingEntity caster;
	
	public StopCastingEvent(Spell spell, LivingEntity caster) {
		this.spell = spell;
		this.caster = caster;
	}

	public Spell getSpell() {
		return spell;
	}

	public LivingEntity getCaster() {
		return caster;
	}
	
}
