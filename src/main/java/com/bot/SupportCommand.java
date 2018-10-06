package com.bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SupportCommand implements Command {

	@Override
	public void execute(ArrayList<String> arguments, MessageReceivedEvent event) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(Color.BLACK);
		embedBuilder.setTitle("Support");
		embedBuilder.setDescription("This is a link to the developer's discord server, where you can submit suggestions for improvements to the bot, as well as get help and report bugs.");
		embedBuilder.addField("Link", BattleBot.getInstance().getConfig(Config.DISCORD_INVITE_LINK), false);

		event.getAuthor().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
	}

	@Override
	public String getTutorial() {
		return "It's really not that complicated.";
	}

	@Override
	public String getDescription() {
		return "Sends the user a direct messages with an invite to the support discord server.";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String getCommand() {
		return "support";
	}

	@Override
	public String getCommandCategory() {
		return "Meta";
	}
}
