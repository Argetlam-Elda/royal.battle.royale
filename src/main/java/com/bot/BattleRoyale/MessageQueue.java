package com.bot.BattleRoyale;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayList;
import java.util.Comparator;

public class MessageQueue {

    private enum Type {
        hp, attack, kill, attackAndKill
    }

    private class Entry implements Comparable<Entry> {

        private int roundNum;

        private Type catagory;

        private String message;

        @Override
        public int compareTo(Entry o) {
            return (roundNum == o.roundNum ? roundNum - o.roundNum : message.compareTo(o.message));
        }
    }

    private MessageChannel channel;

    private ArrayList<Entry> hpList;
    private ArrayList<Entry> killList;
    private ArrayList<Entry> attackList;
    private ArrayList<Entry> attackAndKillList;

    // Queue by round and type (hp, attack, kill, attack and kill together)

    // Queue with information: (round, attacker, defender, weapon, armor, damage, critical, looting)

    /**
     * Tell if this command is to be verbose, printing out all attacks, kills, and health every round.
     */
    private boolean verbose;

    /**
     * Tell if this command is to be reduxified, only printing the final victor.
     */
    private boolean redux;

    public MessageQueue(MessageChannel channel, boolean verbose, boolean redux) {
        this.channel = channel;
        this.verbose = verbose;
        this.redux = redux;
    }

    public void queueMessage(int round, Fighter attacker, Fighter defender) {

    }
}
