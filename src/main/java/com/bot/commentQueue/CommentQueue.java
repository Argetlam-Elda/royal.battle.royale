package com.bot.commentQueue;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.util.PriorityQueue;


public class CommentQueue extends Thread {

	public enum QueuePriority {
		HIGH(4), NORMAL(2), LOW(1);

		private int multiplier;

		QueuePriority(int multiplier) {
			this.multiplier = multiplier;
		}

		public int value() {
			return multiplier;
		}

	}

	private class QueueNode implements Comparable<QueueNode> {
		private MessageAction messageAction;
		private long startTime;
		private QueuePriority priority;

		private QueueNode(MessageAction messageAction, QueuePriority priority) {
			this.messageAction = messageAction;
			this.priority = priority;
			startTime = System.currentTimeMillis();
		}

		@Override
		public int compareTo(@NotNull QueueNode o) {
			long now = System.currentTimeMillis();
			// TODO - hope this doesn't fail
			return (int) ((int) ((now - startTime) * priority.value()) - ((now - o.startTime) * o.priority.value()));
		}
	}

	private CommentQueue instance;

	public CommentQueue getInstance() {
		if (instance == null) {
			instance = new CommentQueue();
		}
		return instance;
	}

	private PriorityQueue<QueueNode> queue;

	private CommentQueue() {
		queue = new PriorityQueue<>();
	}

	public void addMessage(MessageChannel channel, String message, QueuePriority priority) {
		if (message.length() > 2000) {
			throw new IllegalStateException("Message is to long!!! Cap is 2000 characters.");
		}
		else {
			addMessage(channel.sendMessage(message), priority);
		}
	}

	public void addMessage(MessageAction msgAction, QueuePriority priority) {
		queue.add(new QueueNode(msgAction, priority));
	}

	@Override
	public void run() {
		while (!interrupted()) {
			try {
				if (!queue.isEmpty()) {
					queue.poll().messageAction.complete();
					sleep(100);
				} else {
					sleep(1000);
				}
			} catch (InterruptedException e) {
				// do nothing, because what am I supposed to do.
			}
		}
	}

}
