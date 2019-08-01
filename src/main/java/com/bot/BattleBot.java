package com.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

/**
 * A discord bot dedicated to hosting battles, including but not limited to battle royale, role battles, and personal battles.
 *
 * @author ArgetlamElda
 */
public class BattleBot {
	
	private static BattleBot instance;
	
	/**
	 * Maintain/store the shards for this bot.
	 */
	private ShardingHandler shardingHandler;
	
	/**
	 * Message parser for this bot. All messages are sent to it to be handled
	 */
	private Parser parser;
	
	/**
	 * Maintains the configuration data.
	 */
	private Config config;
	
	public static void main(String[] args) {
		BattleBot.getInstance();
	}
	
	/**
	 * Make and run the shards to maintain the bot.
	 */
	private class ShardingHandler {
		
		/**
		 * Store the shards.
		 */
		private ArrayList<JDA> shardList;
		
		/**
		 * Build the shards from the tokens taken from the config handler.
		 *
		 * @param parser - the message handler for these shards
		 * @throws Exception - don't want to handle any problems that happen in here.
		 */
		ShardingHandler(Parser parser) throws Exception {
			shardList = new ArrayList<>();
			JDABuilder shardBuilder = new JDABuilder(AccountType.BOT).setToken(config.getConfig(Config.DISCORD_TOKEN));
			shardBuilder.addEventListener(parser);
			int shardTotal = Integer.parseInt(config.getConfig(Config.NUM_SHARDS));
			for (int i = 0; i < shardTotal; i++) {
				shardList.add(shardBuilder.useSharding(i, shardTotal).buildAsync());
			}
		}
	}
	
	public static BattleBot getInstance() {
		if (instance == null) {
			instance = new BattleBot();
		}
		return instance;
	}
	
	/**
	 * Initialize the config and parser, add all the commands to check against messages, and attempt to start the sharding manager.
	 */
	private BattleBot() {
		config = new Config();
		parser = new Parser();
		populateCommands();
		// TODO - add logger, count command usage
		try {
			shardingHandler = new ShardingHandler(parser);
		} catch (LoginException e) {
			System.err.print("There was a problem loging in.");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add all commands to the parser
	 */
	private void populateCommands() {
		parser.addCommand(new BattleRoyaleCommand());
		parser.addCommand(new HelpCommand());
		parser.addCommand(new InviteCommand());
		parser.addCommand(new SupportCommand());
		parser.addCommand(new AboutCommand());
	}
	
	/**
	 * Get the parser
	 *
	 * @return - parser
	 */
	public Parser getParser() {
		return parser;
	}
	
	/**
	 * Get the shards
	 *
	 * @return - shards
	 */
	public ArrayList<JDA> getJDA() {
		return shardingHandler.shardList;
	}
	
	/**
	 * Get a config token with the given key
	 *
	 * @param key - token key
	 * @return - token with the given key
	 */
	public String getConfig(String key) {
		return config.getConfig(key);
	}
}
