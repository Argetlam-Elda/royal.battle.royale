package com.bot.BattleRoyale;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class BattleRoyale extends Thread {

	private MessageChannel channel;

	ArrayList<Member> candidates;

	public BattleRoyale(String[] args, MessageReceivedEvent event) {
		channel = event.getChannel();
		get_candidates(args, event);
	}

	@Override
	public void run() {
		if (this.isInterrupted()) {
			return;
		}

		finalizeRoster();

	}

	private void finalizeRoster() {
		ArrayList<Fighter> fighters = new ArrayList<>();
		for (Member candidate: candidates) {
			Fighter fighter = new Fighter(candidate);
		}
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
		for (Member candidate: candidates) {
			if (candidate.getUser().isBot()) {
				candidates.remove(candidate);
			}
		}
		if (candidates.size() < 3) {
			channel.sendMessage(":x:You need least 3 contestants to hold a battle royale.").queue();
			interrupt();
		}
	}
}
