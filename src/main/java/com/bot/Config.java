package com.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class stores all information related to starting the bot (eg. discord's tokens) and meta data regarding the bot (e.g. Bot invite link, prefix token).
 *
 * @author ArgetlamElda
 */
public class Config {
	
	/**
	 * The file containing all the configuration data.
	 */
	private final File configFile;
	
	/**
	 * A map of all the config tokens, stored under their relative string keys.
	 */
	private HashMap<String, String> configTokens;
	
	public static final String DISCORD_TOKEN = "DISCORD_TOKEN";
	//	public static final String REDDIT_TOKEN = "REDDIT_TOKEN";
//	public static final String OWNER_ID = "OWNER_ID";
//	public static final String DISCORD_BOT_ID = "BOT_ID";
//	public static final String BOTLIST_API_TOKEN = "BOTLIST_API";
//	public static final String BOT_API_TOKEN = "BOT_PW_API";
//	public static final String BOT_INVITE_LINK = "BOT_INVITE_LINK";
	public static final String DISCORD_INVITE_LINK = "DISCORD_INVITE_LINK";
	
	public static final String NUM_SHARDS = "NUM_SHARDS";
	public static final String PREFIX = "PREFIX";
	
	/**
	 * Build the config
	 */
	Config() {
		configFile = new File("config/Config.conf");
		configTokens = new HashMap<>();
		readConfigFile();
	}
	
	/**
	 * Read in the data stored in the configs and store it in the configToken hashmap.
	 */
	private void readConfigFile() {
		// TODO - add bot name from config
		// TODO - add about spiel from separate config
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
	
	/**
	 * Get a specific token from the config list.
	 *
	 * @param key - location of the desired information
	 * @return - the data corresponding to the key, if it was read from the config file.
	 */
	public String getConfig(String key) {
		return configTokens.get(key);
	}
}
