package com.bot;

import com.bot.BattleRoyale.BattleRoyale;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class BattleRoyaleCommand implements Command {
	
	@Override
	public void execute(ArrayList<String> arguments, MessageReceivedEvent event) {
		try {
			new BattleRoyale(arguments, event).start();
		} catch (IllegalStateException e) {
			event.getChannel().sendMessage(e.getMessage()).queue();
		}
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
		return "<\"\"|-v|-verbose|-here|-dm|-direct-message|-r|-redux>";
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
