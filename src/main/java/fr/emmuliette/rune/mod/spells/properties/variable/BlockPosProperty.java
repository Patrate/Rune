package fr.emmuliette.rune.mod.spells.properties.variable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

public final class BlockPosProperty extends VariableProperty<BlockPos> {
	
	public BlockPosProperty(String name) {
		super(name, null);
	}

	public BlockPosProperty(String name, int cost) {
		super(name, cost);
	}

	@Override
	public BlockPos getValue() {
		return super.getValue();
	}

	@Override
	protected INBT variableToNBT(BlockPos val) {
		return NBTUtil.writeBlockPos(val);
	}

	@Override
	protected BlockPos NBTToVariable(INBT inbt) {
		return NBTUtil.readBlockPos((CompoundNBT) inbt);
	}
}