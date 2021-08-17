package fr.emmuliette.rune.mod.spells.properties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGrid {
	private Set<Coord3D> internalGrid;

	public BlockGrid() {
		this.internalGrid = new HashSet<Coord3D>();
	}

	public int getSize() {
		return internalGrid.size();
	}
	
	public boolean hasPos(int x, int y, int z) {
		return internalGrid.contains(new Coord3D(x, y, z));
	}
	
	public void togglePos(int x, int y, int z) {
		Coord3D nCoord = new Coord3D(x, y, z);
		if(internalGrid.contains(nCoord))
			internalGrid.remove(nCoord);
		else
			internalGrid.add(nCoord);
	}

	public List<BlockPos> getBlockPos(World world, BlockPos center) {
		List<BlockPos> blockPos = new ArrayList<BlockPos>();
		int baseX = center.getX();
		int baseY = center.getY();
		int baseZ = center.getZ();
		for (Coord3D c3d : internalGrid) {
			blockPos.add(new BlockPos(baseX + c3d.x, baseY + c3d.y, baseZ + c3d.z));
		}
		return blockPos;
	}

	public INBT toNBT() {
		ListNBT retour = new ListNBT();
		for (Coord3D c : internalGrid) {
			CompoundNBT coordNBT = new CompoundNBT();
			coordNBT.putInt("x", c.x);
			coordNBT.putInt("y", c.y);
			coordNBT.putInt("z", c.z);
			retour.add(coordNBT);
		}
		return retour;
	}

	public static BlockGrid fromNBT(INBT nbt) {
		if (!(nbt instanceof ListNBT))
			return null; // TODO throw error !
		ListNBT list = (ListNBT) nbt;
		BlockGrid retour = new BlockGrid();
		for (INBT data : list) {
			if (!(data instanceof CompoundNBT)) {
				return retour; // TODO throw error !
			}
			CompoundNBT cdata = (CompoundNBT) data;
			retour.internalGrid.add(retour.new Coord3D(cdata.getInt("x"), cdata.getInt("y"), cdata.getInt("z")));
		}
		return retour;
	}

	public class Coord3D {
		private int x, y, z;

		public Coord3D(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			result = prime * result + z;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Coord3D other = (Coord3D) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			if (z != other.z)
				return false;
			return true;
		}

	}
}
