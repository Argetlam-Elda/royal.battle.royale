package com.bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class InviteCommand implements Command {

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(Color.BLACK);
		embedBuilder.setTitle("Invite");
		embedBuilder.setDescription("This link will allow you to add Royal Battle Royale Bot to any servers where you have the 'Manage Server' permission.");
		embedBuilder.addField("Link", BattleBot.getInstance().getJDA().get(0).asBot().getInviteUrl(), false);

		event.getAuthor().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
		event.getMessage().addReaction("U+1F44D");


	}

	@Override
	public String getTutorial() {
		return "There really isn't that much to tell.";
	}

	@Override
	public String getDescription() {
		return "Sends the user a direct message containing an bot invite link.";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String getCommand() {
		return "invite";
	}

	@Override
	public String getCommandCategory() {
		return "Meta";
	}
}
