package fr.emmuliette.rune;

import fr.emmuliette.rune.mod.spells.capability.sync.BulkUpdateSpellMessage;
import fr.emmuliette.rune.mod.spells.capability.sync.UpdateSpellMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {
	public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(RuneMain.MOD_ID, "mod_network");

	public static final String NETWORK_VERSION = new ResourceLocation(RuneMain.MOD_ID, "1").toString();

	public static SimpleChannel getNetworkChannel() {
		final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
				.clientAcceptedVersions(version -> true)
				.serverAcceptedVersions(version -> true)
				.networkProtocolVersion(() -> NETWORK_VERSION)
				.simpleChannel();

		channel.messageBuilder(BulkUpdateSpellMessage.class, 9)
				.decoder(BulkUpdateSpellMessage::decode)
				.encoder(BulkUpdateSpellMessage::encode)
				.consumer(BulkUpdateSpellMessage::handle)
				.add();

		channel.messageBuilder(UpdateSpellMessage.class, 10)
				.decoder(UpdateSpellMessage::decode)
				.encoder(UpdateSpellMessage::encode)
				.consumer(UpdateSpellMessage::handle)
				.add();


		return channel;
	}
}