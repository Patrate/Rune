package fr.emmuliette.rune.setup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import fr.emmuliette.rune.RuneMain;

public class AutoUpdater {
	private static final String VERSION_URL = "http://runemod.emmathie.fr/version";
	private static final String UPDATE_URL = "http://runemod.emmathie.fr/update";

	public static void update() throws IOException {
		if (!checkVersion()) {
			RuneMain.LOGGER.info("New version found, updating to " + getDistantVersion());
			FileOutputStream fileOutputStream = null;
			try {
				ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(UPDATE_URL).openStream());
				fileOutputStream = new FileOutputStream(getOutputFileName());
				fileOutputStream.getChannel()
				  .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
				// TODO maybe reload
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				RuneMain.LOGGER.error("Couldn't update: " + e.getMessage());
			} catch (MalformedURLException e) {
				e.printStackTrace();
				RuneMain.LOGGER.error("Couldn't update: " + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				RuneMain.LOGGER.error("Couldn't update: " + e.getMessage());
			} finally {
				if(fileOutputStream != null) {
					fileOutputStream.close();
				}
			}
		} else {
			RuneMain.LOGGER.info("Version " + RuneMain.VERSION + " is up to date");
		}
	}

	private static String getOutputFileName() throws MalformedURLException {
		return RuneMain.MOD_ID + getDistantVersion() + ".jar";
	}
	
	private static boolean checkVersion() {
		try {
			return RuneMain.VERSION.equals(getDistantVersion());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String getDistantVersion() throws MalformedURLException {
		String retour = "";
		URL url = new URL(VERSION_URL);
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			int status = con.getResponseCode();
			if (status >= 200 && status < 300) {
				BufferedReader in = null;
				try {
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						content.append(inputLine);
					}
					retour = content.toString();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						in.close();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return retour;
	}
}
