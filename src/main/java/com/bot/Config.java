package com.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Config {

	private static Config instance;
	private final File configFile;
	private HashMap<String, String> configTokens;

	public static final String DISCORD_TOKEN = "DISCORD_TOKEN";
	public static final String REDDIT_TOKEN = "REDDIT_TOKEN";
	public static final String OWNER_ID = "OWNER_ID";
	public static final String DISCORD_BOT_ID = "BOT_ID";
	public static final String BOTLIST_API_TOKEN = "BOTLIST_API";
	public static final String BOT_API_TOKEN = "BOT_PW_API";

	public static final String NUM_SHARDS = "NUM_SHARDS";
	public static final String PREFIX = "PREFIX";

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	private Config() {
		configFile = new File("config/Config.conf");
		configTokens = new HashMap<>();
		readConfigFile();
	}

	private void readConfigFile() {
		try {
			Scanner fileScanner = new Scanner(configFile);
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				if (line.startsWith("***")) {
					String key = line.substring(3, line.length() - 3).trim();
					configTokens.put(key, fileScanner.nextLine().trim());
				}
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			System.err.println("File '" + configFile.getAbsolutePath() + "' does not exist or could not be read.");
		}
	}

	public String getConfig(String key) {
		return configTokens.get(key);
	}
}
