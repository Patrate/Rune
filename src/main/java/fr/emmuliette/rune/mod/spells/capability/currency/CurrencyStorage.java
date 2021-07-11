package fr.emmuliette.rune.mod.spells.capability.currency;

import javax.annotation.Nullable;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class CurrencyStorage implements Capability.IStorage<ICurrency> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<ICurrency> capability, ICurrency instance, Direction side) {
        return IntNBT.valueOf(instance.getAmount());
    }

    @Override
    public void readNBT(Capability<ICurrency> capability, ICurrency instance, Direction side, INBT nbt) {
        if (!(instance instanceof Currency))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");

        instance.setAmount(((IntNBT)nbt).getAsInt());
    }
}