package fr.emmuliette.rune.mod.capabilities.socket;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nullable;

import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.Spell;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class SocketStorage implements Capability.IStorage<ISocket> {
	@Nullable
	@Override
	public INBT writeNBT(Capability<ISocket> capability, ISocket instance, Direction side) {
//    	if(instance instanceof GrimoireSpellImpl) {
//    		return instance.toNBT();
//    	}
		if (instance.getSpell() != null) {
			return instance.getSpell().toNBT();
		} else {
			return new CompoundNBT();
		}
	}

	@Override
	public void readNBT(Capability<ISocket> capability, ISocket instance, Direction side, INBT nbt) {
		if (!(instance instanceof SocketImpl))
			throw new IllegalArgumentException(
					"Can not deserialize to an instance that isn't the default implementation");

		try {
//        	if(instance instanceof GrimoireSpellImpl) {
//        		instance.fromNBT(nbt);
//        	} else
			if (nbt instanceof CompoundNBT && !((CompoundNBT) nbt).isEmpty()) {
				instance.setSpell(Spell.fromNBT((CompoundNBT) nbt));
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | RunePropertiesException e) {
			e.printStackTrace();
		}
	}
}