package fr.emmuliette.rune.mod.spells.properties.variable;

import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.cost.ItemCost;
import fr.emmuliette.rune.mod.spells.properties.exception.PropertyException;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;

public final class BlockTypeProperty extends VariableProperty<BlockState> {
	private int blockCost;

	public BlockTypeProperty(String name, int blockCost, int manaCost) {
		super(name, manaCost);
		this.blockCost = blockCost;
	}

	public BlockTypeProperty(String name, int blockCost) {
		super(name, null);
		this.blockCost = blockCost;
	}

	@Override
	public BlockState getValue() {
		return super.getValue();
	}

	@Override
	public Cost<?> getCost() throws PropertyException {
		Cost<?> retour = super.getCost();
		if(blockCost > 0) {
			BlockState val = this.getValue();
			if(val != null) {
				Item item = val.getBlock().asItem();
				retour.add(new ItemCost(item, blockCost));
			}
		}
		return retour;
	}

	@Override
	protected INBT variableToNBT(BlockState val) {
		return NBTUtil.writeBlockState(val);
	}

	@Override
	protected BlockState NBTToVariable(INBT inbt) {
		return NBTUtil.readBlockState((CompoundNBT) inbt);
	}
}