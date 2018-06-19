package com.bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class BattleRoyale implements Command {

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("Your mom gay").queue();
	}

	@Override
	public String getTutorial() {
		return "How to use this piece of shit command.";
	}

	@Override
	public String getDescription() {
		return "Describes what this command does.";
	}

	@Override
	public ArrayList<String> getCommand() {
		ArrayList<String> commandVariants = new ArrayList<>();
		commandVariants.add("battleroyale");
		return commandVariants;
	}

	@Override
	public String getCommandAdvailability() {
		return "everyone";
	}
}
