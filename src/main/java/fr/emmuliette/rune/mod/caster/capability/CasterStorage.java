package fr.emmuliette.rune.mod.caster.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class CasterStorage implements Capability.IStorage<ICaster> {
	@Nullable
	@Override
	public INBT writeNBT(Capability<ICaster> capability, ICaster instance, Direction side) {
		return instance.toNBT();
	}

	@Override
	public void readNBT(Capability<ICaster> capability, ICaster instance, Direction side, INBT nbt) {
		if (!(instance instanceof CasterImpl))
			throw new IllegalArgumentException(
					"Can not deserialize to an instance that isn't the default implementation");
		
		if (nbt instanceof CompoundNBT && !((CompoundNBT) nbt).isEmpty()) {
			instance.fromNBT(nbt);
		}
	}
}