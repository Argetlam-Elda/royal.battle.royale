package com.bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class HelpCommand implements Command {

	@Override
	public void execute(ArrayList<String> arguments, MessageReceivedEvent event) {
		// TODO - print all other commands with tutorials and descriptions
		// TODO - allow help to be sent in channel if requested
		// TODO - sort commands by category

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(Color.BLACK);
		embedBuilder.setTitle("Command List");

		StringBuilder printout = new StringBuilder();
		for (Command command: BattleBot.getInstance().getParser().getCommands()) {
			printout.append("`");
			printout.append(BattleBot.getInstance().getConfig(Config.PREFIX));
			printout.append(command.getCommand());
			printout.append(" ");
			printout.append(command.getUsage());
			printout.append("`: ");
			printout.append(command.getDescription());
			printout.append("\n");
			embedBuilder.addField(command.getCommandCategory(), "`" + BattleBot.getInstance().getConfig(Config.PREFIX)
					+ command.getCommand()
					+ " " + command.getUsage() + "`: " + command.getDescription(), false);
		}
		MessageChannel channel = event.getAuthor().openPrivateChannel().complete();

		channel.sendMessage(printout.toString()).queue();
		channel.sendMessage(embedBuilder.build()).queue();
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
