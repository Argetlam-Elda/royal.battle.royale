package com.bot;

import com.bot.BattleRoyale.BattleRoyale;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BattleRoyaleCommand implements Command {

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("The battle royale command was run.").queue();
		new BattleRoyale(args, event).start();
	}

	@Override
	public String getTutorial() {
		return "The base command, '" + BattleBot.getInstance().getConfig(Config.PREFIX) + getCommand() + "` will pitch a battle between all members of the guild.";
	}

	@Override
	public String getDescription() {
		return "Pits specified people or all members of a server against each other.";
	}

	@Override
	public String getUsage() {
		return "<\"\">";
	}

	@Override
	public String getCommand() {
		return "battleroyale";
	}

	@Override
	public String getCommandCategory() {
		return "Battle";
	}
}
