package fr.emmuliette.rune.dataCollect;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.setup.Configuration;

public class DataCollector {
	private static final String API_URL = RuneMain.BASE_URL + "data";
	private static final String SPELL_EXIST_URL = API_URL + "/spellHash/";
	private static final String SPELL_USE_URL = API_URL + "/spellUse/";
	private static final String SPELL_NEW_URL = API_URL + "/spellNew";
	private static DataCollector INSTANCE = new DataCollector();
	
	private DataCollector() {
	}
	
	public static boolean isActive() {
		return Configuration.Client.collectSpellData;
	}
	
	public static DataCollector getInstance() {
		return INSTANCE;
	}
	
	public void sendSpellUse(Spell in, int uses) {
		int hash = in.hashCode();
		Boolean alreadyExist = spellExist(hash);
		if(alreadyExist == null) {
			return;
		}
		if(!alreadyExist) {
			String nbtString = in.toNBT().getAsString();
			sendNewSpell(nbtString, hash);
		}
		Map<String, String> data = new HashMap<String, String>();
		data.put("uses", "" + uses);
		data.put("hash", "" + hash);
		Gson gson = new Gson();
		sendData(SPELL_USE_URL, gson.toJson(data));
	}
	
	private void sendNewSpell(String nbtString, int hash) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("nbt", nbtString);
		data.put("hash", "" + hash);
		Gson gson = new Gson();
		sendData(SPELL_NEW_URL, gson.toJson(data));
	}

	private Boolean spellExist(int hash) {
		URL url;
		try {
			url = new URL(SPELL_EXIST_URL + hash);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			int code = con.getResponseCode();
			switch (code) {
			case 200:
				return true;
			case 201:
				return false;
			default:
				return null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void sendData(String urlString, String jsonData) {
		URL url;
		try {
			url = new URL(API_URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = jsonData.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			con.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
