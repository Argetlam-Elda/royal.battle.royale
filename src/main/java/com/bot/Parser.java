package com.bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

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

		// Check if this is a command, and execute it if it is
		// TODO - check permissions for commands here
		for (Command command: commands) {
			if (command.getCommand().contains(contents[0])) {
				command.execute(contents, event);
			}
		}
	}

	public void addCommand(Command command) {
		if (command == null) {
			return;
		}
		commands.add(command);
		// commands.sort();
	}
}
