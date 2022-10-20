package fr.emmuliette.rune.mod.gui.scripting;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CGetRunePacket {
	public static final String NAME_NBT = "name", PROPERTIES_NBT = "properties", CONTAINER_ID_NBT = "container_id", SIZE_NBT = "size";
	//private final CompoundNBT nbt;
	private Set<Point> drawing;
	

	/*public CGetRunePacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}*/
	
	public CGetRunePacket(Set<Point> drawing) {//CompoundNBT nbt) {
		this.drawing = drawing;
	}

	public static void encode(CGetRunePacket msg, PacketBuffer buf) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt(SIZE_NBT, msg.drawing.size());
		int i = 0;
		for(Point p:msg.drawing) {
			nbt.putInt(PROPERTIES_NBT + "_X_" + i, p.x);
			nbt.putInt(PROPERTIES_NBT + "_Y_" + i, p.y);
			i++;
		}
		buf.writeNbt(nbt);
	}

	public static CGetRunePacket decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		int size = nbt.getInt(SIZE_NBT);
		Set<Point> points = new HashSet<Point>(size);
		for(int i = 0; i < size; i++) {
			int x = nbt.getInt(PROPERTIES_NBT + "_X_" + i);
			int y = nbt.getInt(PROPERTIES_NBT + "_Y_" + i);
			points.add(new Point(x, y));
		}
		return new CGetRunePacket(points);
	}

	public static class Handler {
		public static void handle(final CGetRunePacket msg, Supplier<NetworkEvent.Context> ctx) {

			if (ctx.get().getSender().containerMenu instanceof ScriptingContainer) {
				ScriptingContainer container = (ScriptingContainer) ctx.get().getSender().containerMenu;
				container.runMatrixServer(msg.drawing);
			} else {
				System.out.println(ctx.get().getSender().containerMenu.getClass().getSimpleName());
			}

			ctx.get().setPacketHandled(true);
		}

	}
}
