package fr.emmuliette.rune.mod.spells.properties.variable;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public final class EntityTypeProperty extends VariableProperty<EntityType<?>> {

	public EntityTypeProperty(String name, int manaCost) {
		super(name, manaCost);
	}

	public EntityTypeProperty(String name) {
		super(name, null);
	}

	@Override
	public EntityType<?> getValue() {
		return super.getValue();
	}

	@Override
	protected INBT variableToNBT(EntityType<?> val) {
		return StringNBT.valueOf(EntityType.getKey(val).toString());
	}

	@Override
	protected EntityType<?> NBTToVariable(INBT inbt) {
		return EntityType.byString(((StringNBT) inbt).getAsString()).orElse(null);
	}
}