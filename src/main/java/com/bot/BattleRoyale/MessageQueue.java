package com.bot.BattleRoyale;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayList;
import java.util.Collections;

public class MessageQueue {

    private class MessageNode implements Comparable<MessageNode> {

        private String message;

        private int roundNum;

        private int queueOrder;

        private MessageNode(int roundNum, int queueOrder, String message) {
            this.message = message;
            this.roundNum = roundNum;
            this.queueOrder = queueOrder;
        }

        @Override
        public int compareTo(MessageNode o) {
            int comp = roundNum - o.roundNum;
            return (comp == 0 ? queueOrder - o.queueOrder : comp);
        }
    }

    private class AttackMessage {
        private int roundNum;
        private int queueNum;
        private Fighter attacker;
        private Fighter defender;
        private int damage;
        private boolean critical;
        private boolean fail;
        private boolean destroyedArmor;
        private boolean lastFighter;
        private boolean defenderIsDead;

        private String lootMessage;

        private AttackMessage(int roundNum, int queueNum, Fighter attacker, Fighter defender, int damage, boolean critical, boolean fail, boolean destroyedArmor, boolean lastFighter, boolean defenderIsDead, String lootMessage) {
            this.roundNum = roundNum;
            this.queueNum = queueNum;
            this.attacker = attacker;
            this.defender = defender;
            this.damage = damage;
            this.critical = critical;
            this.fail = fail;
            this.destroyedArmor = destroyedArmor;
            this.lastFighter = lastFighter;
            this.defenderIsDead = defenderIsDead;
            this.lootMessage = lootMessage;
        }
    }

    private class HPMessage implements Comparable<HPMessage> {

        private int roundNum;

        private Fighter fighter;

        private HPMessage(int roundNum, Fighter fighter) {
            this.roundNum = roundNum;
            this.fighter = fighter;
        }

        @Override
        public int compareTo(HPMessage o) {
            return fighter.toString().compareTo(o.fighter.toString());
        }
    }

    private MessageChannel channel;

    private ArrayList<AttackMessage> attackList;
    private ArrayList<HPMessage> hpList;

    private ArrayList<MessageNode> attackMessages;
    private ArrayList<MessageNode> killMessages;
    private ArrayList<MessageNode> hpMessages;

    private int nameLength;
    private int hpNameLength;
    private int hitFlavorTextLength;
    private int weaponNameLength;
    private int queueNum;

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

        nameLength = 0;
        hpNameLength = 0;
        hitFlavorTextLength = 0;
        weaponNameLength = 0;
        queueNum = 1;

        attackList = new ArrayList<>();
        hpList = new ArrayList<>();
        attackMessages = new ArrayList<>();
        killMessages = new ArrayList<>();
        hpMessages = new ArrayList<>();
    }

    public void endMatch(ArrayList<Fighter> victor, String guildName) {
        if (victor.isEmpty()) {
            channel.sendMessage("```Loser, loser, chicken loser.k\n```").queue();
        }
        else if (victor.size() == 1) {
            if (redux) {
                // do nothing
            }
            else if (verbose) {
                ArrayList<MessageNode> messages = new ArrayList<>();
                messages.addAll(killMessages);
                messages.addAll(attackMessages);
                messages.addAll(hpMessages);
                Collections.sort(messages);
                StringBuilder commentString = new StringBuilder("```");
                for (MessageNode message : messages) {
                    if (commentString.length() + message.message.length() >= 1990) {
                        commentString.append("```");
                        channel.sendMessage(commentString.toString()).queue();
                        commentString = new StringBuilder("```");
                    }
                    commentString.append(message.message);
                }
                commentString.append("```");
                channel.sendMessage(commentString.toString()).queue();
            }
            else {
                StringBuilder commentString = new StringBuilder("```");
                for (MessageNode message : killMessages) {
                    if (commentString.length() + message.message.length() >= 1990) {
                        commentString.append("```");
                        channel.sendMessage(commentString.toString()).queue();
                        commentString = new StringBuilder("```");
                    }
                    commentString.append(message.message);
                }
                commentString.append("```");
                channel.sendMessage(commentString.toString()).queue();
            }
            channel.sendMessage("```Behold your champion, " + victor.get(0) + " of " + guildName + ".```").queue();
        }
        else {
            channel.sendMessage("```\nBehold your champions, " + victor.toString() + ". Idk what happened, but I guess you all win.\n```").queue();
        }
    }

    public void endRound(int roundNum, int startingFighterCount, ArrayList<Fighter> fighters) {
        killMessages.add(new MessageNode(roundNum, 0, "\n\tRound " + roundNum + ":\tCombatants:" + startingFighterCount));
        // At beginning:  "\tRound " + roundCount + ":\tCombatants: " + startingCombatants + "\n"
        for (Fighter fighter : fighters) {
            queueHpMessage(roundNum, fighter);
        }
        // Do attacks, crits, and kills
        boolean isKill = false;
        for (AttackMessage data : attackList) {
            if (roundNum != data.roundNum) {
                System.err.println("MessageQueue Error: game round (" + roundNum + ") and message round (" + data.roundNum + ") are not the same.");
            }
            if (!killMessage(data).isEmpty()) {
                isKill = true;
                killMessages.add(new MessageNode(data.roundNum, data.queueNum + 1, killMessage(data)));
            }
            attackMessages.add(new MessageNode(data.roundNum, data.queueNum, attackMessage(data)));
        }
        if (!isKill && !verbose) {
            killMessages.add(new MessageNode(roundNum, queueNum, "\nNo fatalities."));
        }
        hpMessages.add(new MessageNode(roundNum, queueNum, "\n\tRemaining Contestants: " + fighters.size() + "\n"));
        for (HPMessage message : hpList) {
            hpMessages.add(new MessageNode(roundNum, queueNum, hpMessage(message)));
            queueNum++;
        }
        attackList = new ArrayList<>();
        hpList = new ArrayList<>();
        // do hp list

        nameLength = 0;
        hpNameLength = 0;
        hitFlavorTextLength = 0;
        weaponNameLength = 0;
        queueNum = 1;
    }

    private void queueHpMessage(int roundNum, Fighter fighter) {
        hpNameLength = Math.max(hpNameLength, fighter.toString().length());
        hpList.add(new HPMessage(roundNum, fighter));
    }

    public void queueAttackMessage(int roundNum, Fighter attacker, Fighter defender, int damage, boolean critical, boolean fail, boolean destroyedArmor, boolean lastFighter, String lootMessage) {
        nameLength = Math.max(nameLength, attacker.toString().length());
        hitFlavorTextLength = Math.max(hitFlavorTextLength, attacker.getWeapon().getFlavorTextLength());
        weaponNameLength = Math.max(weaponNameLength, attacker.getWeapon().toString().length());
        attackList.add(new AttackMessage(roundNum, queueNum, attacker, defender, damage, critical, fail, destroyedArmor, lastFighter, defender.isDead(), lootMessage));
        queueNum++;
        if (defender.isDead()) {
            queueNum++;
        }
        queueNum++;
    }

    private String attackMessage(AttackMessage data) {
        String specialMessages = "";
        String attackMessage = "";

        String hitFlavorText;
        // on a critical success
        if (data.critical) {
            hitFlavorText = data.attacker.getWeapon().getFlavor(WeaponFactory.WeaponFlavor.CRIT);
            specialMessages = " Critical Hit!\n";
            if (data.destroyedArmor) {
                specialMessages += data.attacker + " destroyed " + data.defender + "'s " + data.defender.getArmor() + " with their " + data.attacker.getWeapon() + ".\n";
            }
        }
        // on a critical fail
        else if (data.fail) {
            specialMessages = " Critical fail!\n";
            if (data.defender.isDead()) {
                hitFlavorText = data.attacker.getWeapon().getFlavor(WeaponFactory.WeaponFlavor.SUICIDE);
            } else {
                hitFlavorText = data.attacker.getWeapon().getFlavor(WeaponFactory.WeaponFlavor.SELF);
                if (data.lastFighter) {
                    specialMessages += data.attacker + ", seeing no more opponents before them, attempts to end it all, but fails. Not that we expected anything more from them anyway.";
                }
            }
        }
        // on a regular hit
        else {
            hitFlavorText = data.attacker.getWeapon().getFlavor(WeaponFactory.WeaponFlavor.HIT);
            specialMessages = "\n";
        }
        // Store the default format attack
        String format = "%-" + nameLength + "s %-" + hitFlavorTextLength + "s %-" + nameLength + "s with their %-" + weaponNameLength + "s for %2d";
        attackMessage = String.format(format, data.attacker, hitFlavorText, (data.defender == data.attacker ? "themself" : data.defender), data.attacker.getWeapon(), data.damage);
        return specialMessages + attackMessage;
    }

    private String killMessage(AttackMessage data) {
        String killMessage = "";
        if (data.defenderIsDead) {
            if (data.fail) {
                if (data.lastFighter) {
                    killMessage = "\n" + data.attacker + ", seeing no more opponents before them, decides to end it all.";
                }
                else {
                    killMessage = "\n" + data.attacker + " kills themselves out of shame.";
                }
            }
            else if (data.critical) {
                killMessage = "\n" + data.attacker + " fucking murders " + data.defender + " with their " + data.attacker.getWeapon() + data.lootMessage + "!";
            }
            else {
                killMessage = "\n" + data.attacker + " kills " + data.defender + " with their " + data.attacker.getWeapon() + data.lootMessage + "!";
            }
        }
        return killMessage;
    }

    private String hpMessage(HPMessage message) {
        return String.format("%-" + hpNameLength + "s: %-2d\n", message.fighter, message.fighter.getHealth());
    }
}
