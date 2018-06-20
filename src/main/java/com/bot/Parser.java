package com.bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser extends ListenerAdapter {
	private static Parser ourInstance;

	public static Parser getInstance() {
		if (ourInstance == null) {
			ourInstance = new Parser();
		}
		return ourInstance;
	}

	private ArrayList<Command> commands;

	// private Parser() {
	public Parser() {
		// TODO - does this need to be a singleton?
		commands = new ArrayList<>();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		// No bot commands
		if (event.getAuthor().isBot()) {
			return;
		}

		String[] contents = event.getMessage().getContentRaw().split(" ");

		// Check if this is a command
		// TODO - check permissions for commands here
		if (contents[0].startsWith(BattleBot.getInstance().getConfig(Config.PREFIX))) {
			contents[0] = contents[0].substring(1);
		}
		else {
			// Message did not start with the command character
			return;
		}
		for (Command command: commands) {
			if (command.getCommand().equals(contents[0])) {
				command.execute(Arrays.copyOfRange(contents, 1, contents.length), event);
			}
		}
	}

	public void addCommand(Command command) {
		if (command == null) {
			return;
		}
		commands.add(command);
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}
}
