package fr.emmuliette.rune.mod.player.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerStorage implements Capability.IStorage<IPlayer> {
	@Nullable
	@Override
	public INBT writeNBT(Capability<IPlayer> capability, IPlayer instance, Direction side) {
		return instance.toNBT();
	}

	@Override
	public void readNBT(Capability<IPlayer> capability, IPlayer instance, Direction side, INBT nbt) {
		if (!(instance instanceof PlayerImpl))
			throw new IllegalArgumentException(
					"Can not deserialize to an instance that isn't the default implementation");
		
		if (nbt instanceof CompoundNBT && !((CompoundNBT) nbt).isEmpty()) {
			instance.fromNBT(nbt);
		}
	}
}