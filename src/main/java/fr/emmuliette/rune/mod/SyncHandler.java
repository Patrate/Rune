package fr.emmuliette.rune.mod;

import java.util.List;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.caster.capability.sync.CasterPacket;
import fr.emmuliette.rune.mod.spells.capability.sync.SpellPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SyncHandler {
	// PLAYER SERVER SYNC
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(RuneMain.MOD_ID, "spell_channel"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

	public static void register() {
		int disc = 0;
		HANDLER.registerMessage(disc++, SpellPacket.class, SpellPacket::encode, SpellPacket::decode,
				SpellPacket.Handler::handle);
		HANDLER.registerMessage(disc++, CasterPacket.class, CasterPacket::encode, CasterPacket::decode,
				CasterPacket.Handler::handle);
	}

	/**
	 * Sends a packet to a specific player.<br>
	 * Must be called server side.
	 */
	public static void sendTo(Object msg, ServerPlayerEntity player) {
		if (!(player instanceof FakePlayer)) {
			if (player.connection == null || player.connection.getConnection() == null)
				return;
			HANDLER.sendTo(msg, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	/**
	 * Sends a packet to the server.<br>
	 * Must be called client side.
	 */
	public static void sendToServer(Object msg) {
		HANDLER.sendToServer(msg);
	}

	/** Server side. */
	public static void sendToAllPlayers(Object msg, MinecraftServer server) {
		List<ServerPlayerEntity> list = server.getPlayerList().getPlayers();
		for (ServerPlayerEntity e : list) {
			sendTo(msg, e);
		}
	}

	// PLAYER SYNC EVENTS
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {

		LazyOptional<ICaster> oldCap = event.getOriginal().getCapability(CasterCapability.CASTER_CAPABILITY, null);
		LazyOptional<ICaster> newCap = event.getPlayer().getCapability(CasterCapability.CASTER_CAPABILITY, null);
		ICaster oldPlayer = oldCap.orElse(null);

		if (oldPlayer != null) {
			ICaster newPlayer = newCap.orElse(null);

			if (newPlayer != null) {
				newPlayer.sync(oldPlayer);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		player.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(c -> c.sync(player));
	}

	@SubscribeEvent
	public static void onRespawnEvent(PlayerRespawnEvent event) {
		event.getPlayer().getCapability(CasterCapability.CASTER_CAPABILITY)
				.ifPresent(c -> c.sync((ServerPlayerEntity) event.getPlayer()));
	}

	@SubscribeEvent
	public static void onPlayerConnect(PlayerLoggedInEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		player.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(c -> c.sync(player));
	}
}
