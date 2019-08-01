package com.bot.BattleRoyale;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This stores all kinds of data about the fighters, and preforms actions such as attacking and defending.
 */
public class Fighter implements Comparable<Fighter> {
	
	/**
	 * The fighter's name. Duh.
	 */
	private String name;
	
	/**
	 * How much hp the fighter has left.
	 */
	private int hp;
	
	/**
	 * The (other) fighter this fighter wants to get revenge on.
	 */
	private Fighter revenge;
	
	/**
	 * The fighter's weapon.
	 */
	private WeaponFactory.Weapon weapon;
	
	/**
	 * The fighter's armor.
	 */
	private ArmorFactory.Armor armor;
	
	/**
	 * Initialize a default fighter, with 100 hp, no revenge target, no name, and random weapon and armor.
	 */
	private Fighter() {
		hp = 100;
		revenge = null;
		name = "";
		weapon = WeaponFactory.getInstance().buildWeapon();
		armor = ArmorFactory.getInstance().buildArmor();
	}
	
	/**
	 * Build a list of fighters from the switches passed in and the members of the guild.
	 *
	 * @param event - the message event containing the command to start this battle
	 * @return - an alphabetically sorted list of the fighters
	 */
	public static ArrayList<Fighter> getFighters(MessageReceivedEvent event) {
		ArrayList<Member> candidates;
		// Play with mentioned people
		if (!event.getMessage().getMentionedMembers().isEmpty()) {
			candidates = new ArrayList<>(event.getMessage().getMentionedMembers());
		}
		// Play with players that have the mentioned rolls
		else if (!event.getMessage().getMentionedRoles().isEmpty()) {
			candidates = new ArrayList<>(event.getGuild().getMembersWithRoles(event.getMessage().getMentionedRoles()));
		} else {
			candidates = new ArrayList<>(event.getGuild().getMembers());
		}
		// remove bots
		for (int i = 0; i < candidates.size(); i++) {
			if (candidates.get(i).getUser().isBot()) {
				candidates.remove(i);
				i--;
			}
		}
		if (candidates.size() < 3) {
			throw new IllegalStateException(":x:You need least 3 contestants to hold a battle royale.:x:");
		}
		ArrayList<Fighter> fighters = new ArrayList<>();
		for (Member candidate : candidates) {
			fighters.add(new Fighter(candidate));
		}
		if (fighters.size() == 0) {
			throw new IllegalStateException("Shit's whack yo. There were 3 or more contestants, but none of them wanted to be fighters.");
		}
		Collections.sort(fighters);
		return fighters;
	}
	
	/**
	 * Initialize a default fighter, then set it's name to the given member's nickname, or barring that, their name.
	 *
	 * @param member - the member to name this fighter after
	 */
	Fighter(Member member) {
		this();
		name = member.getNickname();
		if (name == null) {
			name = member.getEffectiveName();
		}
	}
	
	/**
	 * Subtract the incoming damage from the fighters hp pool, after reducing by armor resistance.
	 *
	 * @param damage - how much damage to take
	 */
	public int takeDamage(int damage) {
		damage -= armor.getResist();
		damage = Math.max(damage, 0);
		hp -= damage;
		return damage;
	}
	
	/**
	 * If the corpse is dead, try and take its stuff.
	 *
	 * @param corpse - body to loot
	 * @param rand   - number generator to use for looting
	 * @return - a string telling what was looted
	 */
	public String lootFigher(Fighter corpse, Random rand) {
		if (!corpse.isDead()) {
			return "";
		}
		boolean w = false;
		String lootRecord = "";
		if (rand.nextInt(WeaponFactory.getInstance().getMaxDamage()) + 1 < corpse.weapon.getDamage() - weapon.getDamage()) {
			weapon = corpse.weapon;
			w = true;
			lootRecord = ", and loots their " + weapon.toString();
		}
		if (rand.nextInt(ArmorFactory.getInstance().getMaxResist()) + 1 < corpse.armor.getResist() - armor.getResist()) {
			armor = corpse.armor;
			if (w) {
				lootRecord += " and " + armor.toString();
			} else {
				lootRecord = ", and loots their " + armor.toString();
			}
		}
		return lootRecord;
	}
	
	public String toString() {
		return name;
	}
	
	/**
	 * Get the fighter's remaining health.
	 *
	 * @return - the fighter's current health
	 */
	public int getHealth() {
		return hp;
	}
	
	/**
	 * Returns true if the fighter is still alive, false otherwise.
	 *
	 * @return - true where hp is above 0
	 */
	public boolean isDead() {
		return hp <= 0;
	}
	
	/**
	 * Get the fighter's most hated enemy.
	 *
	 * @return - the fighter's most hated enemy
	 */
	public Fighter getRevengeTarget() {
		return revenge;
	}
	
	/**
	 * Get the fighter's weapon.
	 *
	 * @return - the fighter's weapon
	 */
	public WeaponFactory.Weapon getWeapon() {
		return weapon;
	}
	
	/**
	 * Get the fighter's armor.
	 *
	 * @return - the fighter's armor
	 */
	public ArmorFactory.Armor getArmor() {
		return armor;
	}
	
	@Override
	public int compareTo(Fighter o) {
		// TODO - check hp comparison is correct
		return (name.compareTo(o.name) == 0 ? hp - o.hp : name.compareTo(o.name));
	}
}
