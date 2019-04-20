package com.bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

/**
 * An interface for building commands, with the different actions they can take.
 */
public interface Command {

    /**
     * Preform the action needed by this command
     * @param args - any switches passed into the command, or other random text
     * @param event - the event that caused this command to be run
     */
    void execute(ArrayList<String> args, MessageReceivedEvent event);

    /**
     * Get a text tutorial explaining how to use this
     * @return - the tutorial for this command
     */
    String getTutorial();

    /**
     * Get a text description explaining what the command will do if run
     * @return - the description of this command
     */
    String getDescription();

    /**
     * Get a text representation of this command and its different flags/usages.
     * @return - the usage cases for this command
     */
    String getUsage();

    /**
     * Get a text name of the command.
     * @return - the command's name
     */
    String getCommand();

    /**
     * Get a text containing this command's tags
     * @return - the commands tags
     */
    String getCommandCategory();

}
