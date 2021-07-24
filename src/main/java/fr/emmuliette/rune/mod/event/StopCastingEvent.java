package fr.emmuliette.rune.mod.event;

import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class StopCastingEvent extends Event{
	private Spell spell;
	private LivingEntity caster;
	private int count;
	
	public StopCastingEvent(Spell spell, LivingEntity caster, int count) {
		this.spell = spell;
		this.caster = caster;
		this.count = count;
	}

	public Spell getSpell() {
		return spell;
	}

	public LivingEntity getCaster() {
		return caster;
	}
	
	public int getCount() {
		return count;
	}
	
}
