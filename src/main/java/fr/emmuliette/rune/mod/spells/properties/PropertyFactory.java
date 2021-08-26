package fr.emmuliette.rune.mod.spells.properties;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public abstract class PropertyFactory {
	public static final PropertyFactory EMPTY_FACTORY = new PropertyFactory() {
		@Override
		public RuneProperties buildInternal() {
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

	private List<PropertyFactory> subFactories = new ArrayList<PropertyFactory>();

	protected abstract RuneProperties buildInternal();

	public final RuneProperties build() {
		RuneProperties retour = buildInternal();
		for (PropertyFactory subProp : subFactories) {
			retour.addAll(subProp.build());
		}
		return retour;
	}

	public PropertyFactory addSub(PropertyFactory sub) {
		subFactories.add(sub);
		return this;
	}

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
