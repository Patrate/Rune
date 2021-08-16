package fr.emmuliette.rune.mod.spells.properties;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public abstract class PropertyFactory {
	public static final PropertyFactory EMPTY_FACTORY = new PropertyFactory() {
		@Override
		public RuneProperties build() {
			return new RuneProperties() {

				@Override
				protected void init() {
				}
			};
		}

		@Override
		public RuneProperties fromNBT(INBT compoundNBT) {
			return build();
		}
	};

	public abstract RuneProperties build();

	public RuneProperties fromNBT(INBT data) {
		RuneProperties retour = build();
		if (!(data instanceof ListNBT))
			return retour;
		ListNBT prop = (ListNBT) data;
		for (INBT propertyINBT : prop) {
			CompoundNBT propertyNBT = (CompoundNBT) propertyINBT;
			retour.getProperty(propertyNBT.getString(Property.NAME)).setValue(propertyNBT.get(Property.VALUE));
		}
		return retour;
	}
}
