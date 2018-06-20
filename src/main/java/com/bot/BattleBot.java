package com.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;


public class BattleBot {

	private static BattleBot instance;

	private ShardingHandler shardingHandler;

	private Parser parser;

	private Config config;

	public static void main(String[] args) {
		BattleBot.getInstance();
	}

	private class ShardingHandler {
		private ArrayList<JDA> shardList;

		public ShardingHandler(Parser parser) throws Exception {
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

	private BattleBot() {
		config = new Config();
		parser = new Parser();
		populateCommands();
		// TODO - add logger
		try {
			shardingHandler = new ShardingHandler(parser);
		} catch (LoginException e) {
			System.err.print("There was a problem loging in.");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void populateCommands() {
		parser.addCommand(new BattleRoyaleCommand());
		parser.addCommand(new HelpCommand());
		parser.addCommand(new InviteCommand());
		parser.addCommand(new SupportCommand());
		parser.addCommand(new AboutCommand());
		return;
	}

	public Parser getParser() {
		return parser;
	}

	public ArrayList<JDA> getJDA() {
		return shardingHandler.shardList;
	}

	public String getConfig(String key) {
		return config.getConfig(key);
	}
}
