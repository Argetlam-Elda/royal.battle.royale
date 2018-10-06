package com.bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class AboutCommand implements Command {

	@Override
	public void execute(ArrayList<String> arguments, MessageReceivedEvent event) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(Color.BLACK);
		embedBuilder.setTitle("About Royal Battle Royale Bot");
		embedBuilder.setDescription("This bot is a pre-alpha battle royale game currently in development by Argetlam-Elda and Tyranomaster. Since he is still in development, he may go down occasionally as we add features and fix bugs. Royal Battle Royale is built on JDA, made by dv8tion. For a list of commands, DM him or type '" + BattleBot.getInstance().getConfig(Config.PREFIX) + "help' in a channel he can see");
		embedBuilder.addField("Discord Server", "Royal Battle Royale has a discord server where you can report any bugs you find, make suggestions for new features or improvements, and of course battle to be the very best like no one ever was.\n" + BattleBot.getInstance().getConfig(Config.DISCORD_INVITE_LINK), false);
		embedBuilder.addField("Invite Royal Battle Royale Bot", "To invite him to your server, go to: " + BattleBot.getInstance().getJDA().get(0).asBot().getInviteUrl(), false);

		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}

	@Override
	public String getTutorial() {
		return "TODO";
	}

	@Override
	public String getDescription() {
		return "TODO - What *does* this do?";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String getCommand() {
		return "about";
	}

	@Override
	public String getCommandCategory() {
		return "Meta";
	}
}
