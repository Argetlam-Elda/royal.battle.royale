package com.bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class BattleRoyaleCommand implements Command {

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("The battle royale command was run.").queue();

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
	public ArrayList<String> getCommand() {
		return new ArrayList<>(Arrays.asList("battleroyale"));
	}

	@Override
	public String getCommandCategory() {
		return "everyone";
	}
}
