package com.bot.BattleRoyale;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayList;

public class MessageQueue {

    private MessageChannel channel;

    private String htmlMaster;

    private String htmlHP;

    private String htmlAttack;

    private String htmlKill;

    private ArrayList<String> hpList;

    private ArrayList<String> attackList;

    private ArrayList<String> killList;

    public MessageQueue(MessageChannel chan) {
        htmlMaster = "<!DOCTYPE html>\n<html>\n<title>Battle Time</title>\n<body>";
        channel = chan;

        hpList = new ArrayList<>();
        hpList.add("```    Remaining: ");

        attackList = new ArrayList<>();
        attackList.add("```    Round 1:    Combatants: ");

        killList = new ArrayList<>();
        killList.add("```    Deaths: ");
    }

    public void addHp(String hpString) {
        if (hpList.get(hpList.size() - 1).length() + hpString.length() + 3 >= 2000) {
            hpList.set(hpList.size() - 1, hpList.get(hpList.size() - 1) + hpString);
        }
        else {
            hpList.set(hpList.size() - 1, hpList.get(hpList.size() - 1) + "```");
            hpList.add("```");
        }
        htmlHP += "<p>" + hpString + "</p>\n";
    }

    public void addAttack(String attackString) {
        if (attackList.get(attackList.size() - 1).length() + attackString.length() + 3 >= 2000) {
            attackList.set(attackList.size() - 1, attackList.get(attackList.size() - 1) + attackString);
        }
        else {
            attackList.set(attackList.size() - 1, attackList.get(attackList.size() - 1) + "```");
            hpList.add("```");
        }
        htmlAttack += "<p>" + attackString + "</p>\n";
    }

    public void addKill(String killString) {
        if (attackList.get(attackList.size() - 1).length() + killString.length() + 3 >= 2000) {
            attackList.set(attackList.size() - 1, attackList.get(attackList.size() - 1) + killString);
        }
        else {
            attackList.set(attackList.size() - 1, attackList.get(attackList.size() - 1) + "```");
            hpList.add("```");
        }
        htmlAttack += "<p>" + killString + "</p>\n";
    }

    public void endRound(int roundNum, int startingCombatants, int endingCombatants) {
        attackList.set(attackList.size() - 1, attackList.get(attackList.size() - 1) + "```");
        killList.set(killList.size() - 1, killList.get(killList.size() - 1) + "```");
        while (!attackList.isEmpty()) {
            channel.sendMessage(attackList.remove(0)).queue();
            channel.sendMessage(killList.remove(0)).queue();
        }
        hpList.set(hpList.size() - 1, hpList.get(hpList.size() - 1) + "```");
        while (!hpList.isEmpty()) {
            channel.sendMessage(hpList.remove(0)).queue();
        }
        // TODO - add all html files into the main one
    }

    public void endMatch(Fighter victor, String guildName) {
        channel.sendMessage("```\nBehold your champion, " + victor + " of " + guildName + ", wielding their mighty " + victor.getWeapon() + " and wearing their " + victor.getArmor() +  "!\n```").queue();

    }

}
