package com.bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public interface Command {

	void execute(String[] args, MessageReceivedEvent event);

	String getTutorial();

	String getDescription();

	ArrayList<String> getCommand();

	String getCommandAdvailability();

}
