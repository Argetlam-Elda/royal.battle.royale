package com.bot;

import com.sun.prism.paint.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.EmbedType;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Invite implements Command {

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle("Invite");
		embedBuilder.setColor(java.awt.Color.BLACK);
		embedBuilder.setDescription("Use this link to invite Royal Battle Royale to your discord server: " + BattleBot.getInstance().getConfig(Config.BOT_INVITE_LINK));

		
		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}

	@Override
	public String getTutorial() {
		return "There really isn't that much to tell.";
	}

	@Override
	public String getDescription() {
		return "Sends an direct message containing an invite link to the user who runs the command.";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public ArrayList<String> getCommand() {
		return new ArrayList<>(Arrays.asList("invite"));
	}

	@Override
	public String getCommandCategory() {
		return null;
	}
}
