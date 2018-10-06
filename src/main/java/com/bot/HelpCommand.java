package com.bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class HelpCommand implements Command {

	@Override
	public void execute(ArrayList<String> arguments, MessageReceivedEvent event) {
		// TODO - print all other commands with tutorials and descriptions
		// TODO - allow help to be sent in channel if requested
		// TODO - sort commands by category
		// TODO - if RoyalBattleRoyale is ever mentioned, print basic help message

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(Color.BLACK);
		embedBuilder.setTitle("Command List");

		BattleBot.getInstance().getParser().getCommands();
		String printout = new String();
		for (Command command: BattleBot.getInstance().getParser().getCommands()) {
			printout += "`" + BattleBot.getInstance().getConfig(Config.PREFIX)
					+ command.getCommand()
					+ " " + command.getUsage() + "`: " + command.getDescription() + "\n";
			embedBuilder.addField(command.getCommandCategory(), "`" + BattleBot.getInstance().getConfig(Config.PREFIX)
					+ command.getCommand()
					+ " " + command.getUsage() + "`: " + command.getDescription(), false);
		}
		event.getAuthor().openPrivateChannel().complete().sendMessage(printout).queue();
		event.getAuthor().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
		// embedBuilder.setDescription("");

	}

	@Override
	public String getTutorial() {
		return "Just do the thing, and let the bot do the thing.";
	}

	@Override
	public String getDescription() {
		return "Lists all commands, or give a short tutorial of one command.";
	}

	@Override
	public String getUsage() {
		return "<\"\"|command_name_without_prefix>";
	}

	@Override
	public String getCommand() {
		return "help";
	}

	// Category
	@Override
	public String getCommandCategory() {
		return "Meta";
	}
}
