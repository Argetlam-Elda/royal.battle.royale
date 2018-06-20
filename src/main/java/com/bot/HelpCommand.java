package com.bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class HelpCommand implements Command {

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		// TODO - print all other commands with tutorials and descriptions
		BattleBot.getInstance().getParser().getCommands();
		String printout = new String();
		for (Command command: BattleBot.getInstance().getParser().getCommands()) {
			printout += "`" + BattleBot.getInstance().getConfig(Config.PREFIX)
					+ (command.getCommand().size() == 1 ? command.getCommand().get(0) : command.getCommand())
					+ " " + command.getUsage() + "`: " + command.getDescription() + "\n";
		}
		event.getChannel().sendMessage(printout).queue();
	}

	@Override
	public String getTutorial() {
		return "Just do the thing, and let the bot do the thing.";
	}

	@Override
	public String getDescription() {
		return "Displays all commands with a short description of what they do and how to use them, or one command with a short tutorial on how to use it.";
	}

	@Override
	public String getUsage() {
		return "<\"\"|command_name>";
	}

	@Override
	public ArrayList<String> getCommand() {
		return new ArrayList<>(Arrays.asList("help"));
	}

	// Category
	@Override
	public String getCommandCategory() {
		return "Help";
	}
}
