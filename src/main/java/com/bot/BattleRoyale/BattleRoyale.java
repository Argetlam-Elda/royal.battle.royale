package com.bot.BattleRoyale;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class BattleRoyale extends Thread {

	private MessageChannel channel;

	private ArrayList<Member> candidates;

	private ArrayList<Fighter> fighters;

	private Random rand;

	private Boolean verbose;

	public BattleRoyale(String[] args, MessageReceivedEvent event) {
		rand = new Random();
		verbose = true;
		channel = event.getChannel();
		fighters = new ArrayList<>();
		get_candidates(args, event);
	}

	@Override
	public void run() {
		if (this.isInterrupted()) {
			return;
		}
		// Convert list of members to a list of fighters and equip weapons and armor
		finalizeRoster();

		int roundCount = 0;
		// As long as there is more than one fighter, do another round
		while (fighters.size() > 1) {
			// Get the max name length
			int nameLength = 0;
			for (Fighter fighter: fighters) {
				nameLength = Math.max(fighter.toString().length(), nameLength);
			}
			int startingCombatants = fighters.size();

			// Do a round of battle
			String battleReport = "\tRound " + roundCount + ":\tCombatants: " + startingCombatants + "\n";
			battleReport += enactRound(nameLength);

			sendBattleReport(battleReport);
			sendHPList(nameLength);
		}


		if (fighters.size() == 0) {
			channel.sendMessage("```Loser, loser, chicken loser.\n```").queue();
		}
		else if (fighters.size() == 1) {
			Fighter victor = fighters.get(0);
			// TODO - use guild instead of channel here
			channel.sendMessage("```\nBehold your champion, " + victor + " of " + channel + ", weilding their mighty " + victor.getWeapon() + " and wearing their " + victor.getArmor() +  "!\n```").queue();
		}
		else {
			// TODO - check this never happens
		}
	}

	private void sendHPList(int nameLength) {
		if (!verbose) {
			return;
		}
		String output = "```\nRemaining Combatants: " + fighters.size() + "\n";
		for (Fighter fighter: fighters) {
			String line = String.format("%-" + nameLength + "s: %-2d\n", fighter.toString(), fighter.getHealth());
			if (output.length() + line.length() < 1900) {
				output += line;
			}
			else {
				output += "```";
				channel.sendMessage(output).queue();
				// TODO - wait
				output = "```\n" + line;
			}
		}
		output += "```";
		channel.sendMessage(output).queue();
		// TODO - wait
	}

	private void sendBattleReport(String battleReport) {
		String output = "```\n";
		Scanner reportScanner = new Scanner(battleReport);
		if (!reportScanner.hasNext()) {
			// if there is no battle report
			output += "No fatalities.\n";
		}
		while (reportScanner.hasNext()) {
			String line = reportScanner.nextLine();
			if (output.length() + line.length() < 1900) {
				output += line + "\n";
			}
			else {
				output += "```";
				channel.sendMessage(output).queue();
				// TODO - wait
				output = "```\n" + line + "\n";
			}
		}
		output += "```";
		channel.sendMessage(output).queue();
		// TODO - wait

	}

	private String enactAttack(Fighter attacker, Fighter defender, int nameLength) {
		// TODO - go through and put weapon and armor back in
		// Target of the attack, will be defender unless there is a critical fail
		Fighter target = defender;
		// Was this hit critical? 2/3 conditions is is
		Boolean critical = true;
		// Roll to hit
		int roll = rand.nextInt(20) + 1;
		// Damage total
		int damage = rand.nextInt(10) + roll + 1;

		// Critical fail, hit self
		if (roll == 1) {
			target = attacker;
		}
		else if (roll == 20) {
			damage += 10;
		}
		else {
			critical = false;
		}
		// If there is no target (only if attacker is the last combatant and didn't hit themself
		if (target == null) {
			return "";
		}
		// Target takes damage
		target.takeDamage(Math.max(damage, 0));
		String hitFlavorText = "";
		if (roll == 1) {
			if (target.getHealth() < 1) {
				hitFlavorText = "suicides";
			}
			else {
				hitFlavorText = "self-harms";
			}
		}
		else if (roll == 20) {
			hitFlavorText = "crits";
		}
		else {
			hitFlavorText = "hits";
		}
		String battleReport = "";
		if (verbose) {
			// Print the default format attack
			battleReport += String.format("\n%-" + nameLength + "s %-" + "10" + "s %-" + nameLength + "s with their %-" + "1" + "s for %2d", attacker, hitFlavorText, target, "Fist", damage);
		}
		if (target.getHealth() < 1) {
			fighters.remove(target);
		}
		return battleReport;
	}

	private String enactRound(int nameLength) {
		// Textual record of what happens during each round
		String battleReport = new String();
		// Shuffle the fighter attack order
		ArrayList<Fighter> shuffledFighters = (ArrayList<Fighter>) fighters.clone();
		Collections.shuffle(shuffledFighters);
		for (Fighter attacker: shuffledFighters) {
			Fighter defender = null;
			// if there is a potential for revenge, 50% chance of attacking them
			if (attacker.getRevengeTarget() != null && shuffledFighters.contains(attacker.getRevengeTarget()) && rand.nextBoolean()) {
				defender = attacker.getRevengeTarget();
			}
			else if (fighters.size() == 1) {
				// TODO - make enactAttack()
				// battleReport += enactAttack();
			}
			else {
				while(defender == null || defender == attacker) {
					defender = fighters.get(rand.nextInt(fighters.size()));
				}
			}
			battleReport += enactAttack(attacker, defender, nameLength);
		}
		return battleReport;
	}

	private void finalizeRoster() {
		for (Member candidate: candidates) {
			fighters.add(new Fighter(candidate));
		}
		if (fighters.size() == 0) {
			interrupt();
			return;
		}
		Collections.sort(fighters);
	}

	private void get_candidates(String[] args, MessageReceivedEvent event) {
		if (args.length == 0) {
			// Play with everyone
			candidates = new ArrayList<>(event.getGuild().getMembers());
		}
		else if (event.getMessage().getMentionedMembers().size() > 0) {
			// Play with mentioned people
			candidates = new ArrayList<>(event.getMessage().getMentionedMembers());
		}
		else {
			candidates = new ArrayList<>(event.getGuild().getMembersWithRoles(event.getMessage().getMentionedRoles()));
		}
		// remove bots
		ArrayList<Member> botlessCandidates = (ArrayList<Member>) candidates.clone();
		for (Member candidate: candidates) {
			if (candidate.getUser().isBot()) {
				botlessCandidates.remove(candidate);
			}
		}
		candidates = botlessCandidates;
		if (candidates.size() < 3) {
			channel.sendMessage(":x:You need least 3 contestants to hold a battle royale.").queue();
			interrupt();
		}
	}
}
