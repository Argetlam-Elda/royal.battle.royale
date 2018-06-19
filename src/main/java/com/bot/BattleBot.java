package com.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;


public class BattleBot {

	private ShardingHandler shardingHandler;

	public static void main(String[] args) throws Exception {
		BattleBot bot = new BattleBot();
	}

	private class ShardingHandler {
		private ArrayList<JDA> shardList;

		public ShardingHandler(Parser parser) throws Exception {
			shardList = new ArrayList<>();
			JDABuilder shardBuilder = new JDABuilder(AccountType.BOT).setToken(Config.getInstance().getConfig(Config.DISCORD_TOKEN));
			shardBuilder.addEventListener(parser);
			int shardTotal = Integer.parseInt(Config.getInstance().getConfig(Config.NUM_SHARDS));
			for (int i = 0; i < shardTotal; i++) {
				shardBuilder.useSharding(i, shardTotal).buildAsync();
			}
		}
	}

	public BattleBot() {
		Parser parser = new Parser();
		populateCommands(parser);
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

	public static void populateCommands(Parser parser) {
		parser.addCommand(new BattleRoyale());
		return;
	}
}
