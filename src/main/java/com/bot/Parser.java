package com.bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Parser will store a list of commands and check all incoming messages against it. When a message is a command,
 * the parser will run said command with the given switches.
 * @author ArgetlamElda
 */
public class Parser extends ListenerAdapter {

	/**
	 * A list of commands to check against.
	 */
	private ArrayList<Command> commands;

	/**
	 * Initialize the parser with no commands.
	 */
	Parser() {
		commands = new ArrayList<>();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			// Bots shall not preform commands. Because fuck you, robo uprising!
			if (event.getAuthor().isBot()) {
				return;
			}

			ArrayList<String> contents = new ArrayList<>(Arrays.asList(event.getMessage().getContentRaw().split(" ")));
			// Check if this is a command
			// TODO - check permissions for commands here
			if (contents.get(0).startsWith(BattleBot.getInstance().getConfig(Config.PREFIX))) {
				contents.set(0, contents.get(0).substring(1));
			} else {
				// Message did not start with the command character
				return;
			}
			for (Command command : commands) {
				if (command.getCommand().equals(contents.get(0))) {
					command.execute(contents, event);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a command to the current list of commands.
	 * @param command - command to be added
	 */
	public void addCommand(Command command) {
		if (command == null) {
			return;
		}
		commands.add(command);
	}

	/**
	 * Get the current list of commands.
	 * @return - the list of commands
	 */
	public ArrayList<Command> getCommands() {
		return commands;
	}
}
