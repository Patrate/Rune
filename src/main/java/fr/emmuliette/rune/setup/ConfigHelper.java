package fr.emmuliette.rune.setup;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Reloading;
import net.minecraftforge.fml.network.ConnectionType;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * @author Cadiboo
 */
public final class ConfigHelper {
	@Nonnull
	static List<String> getDefaultInactiveItem() {
		return new ArrayList<String>();
	}

	// Only call with correct type.
	public static void saveAndLoad(final ModConfig.Type type) {
		ConfigTracker_getConfig(RuneMain.MOD_ID, type).ifPresent(modConfig -> {
			modConfig.save();
			((CommentedFileConfig) modConfig.getConfigData()).load();
			fireReloadEvent(modConfig);
		});
	}

	private static Optional<ModConfig> ConfigTracker_getConfig(final String modId, final ModConfig.Type type) {
		Map<String, Map<ModConfig.Type, ModConfig>> configsByMod = ObfuscationReflectionHelper.getPrivateValue(ConfigTracker.class, ConfigTracker.INSTANCE, "configsByMod");
		return Optional.ofNullable(configsByMod.getOrDefault(modId, Collections.emptyMap()).getOrDefault(type, null));
	}

	private static void fireReloadEvent(final ModConfig modConfig) {
		final ModContainer modContainer = ModList.get().getModContainerById(modConfig.getModId()).get();
		final Reloading event;
		try {
			event = ObfuscationReflectionHelper.findConstructor(Reloading.class, ModConfig.class).newInstance(modConfig);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		modContainer.dispatchConfigEvent(event);
	}

	// GARDE OK
	/**
	 * Disables collisions if we are connected to a vanilla server.
	 */
	@SuppressWarnings("deprecation")
	public static void performServerConnectionStatusValidation() {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			final ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
			if (connection != null && NetworkHooks.getConnectionType(connection::getConnection) == ConnectionType.MODDED) {
				// TODO: Vérifier que ça fait bien ce que je pense :
				// On peut maybe forcer les valeurs de config des clients à se conformer au serveur ici ?
			} else {
				// TODO: Set all the config for when on a vanilla server (maybe deactivate gui ? idk
				// Configuration.Server.terrainCollisions = false;
			}
		});
	}
}
