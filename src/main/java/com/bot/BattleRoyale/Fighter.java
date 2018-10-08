package com.bot.BattleRoyale;

import net.dv8tion.jda.core.entities.Member;
import org.jetbrains.annotations.NotNull;

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
	 * Initialize a default fighter, then set it's name to the given member's nickname, or barring that, their name.
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
	 * @param corpse - body to loot
	 * @param rand - number generator to use for looting
	 * @return - a string telling what was looted
	 */
	public String lootFigher(Fighter corpse, Random rand) {
		if (corpse.getHealth() > 0) {
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
			}
			else {
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
	 * @return - the fighter's current health
	 */
	public int getHealth() {
		return hp;
	}

	/**
	 * Get the fighter's most hated enemy.
	 * @return - the fighter's most hated enemy
	 */
	public Fighter getRevengeTarget() {
		return revenge;
	}

	/**
	 * Get the fighter's weapon.
	 * @return - the fighter's weapon
	 */
	public WeaponFactory.Weapon getWeapon() {
		return weapon;
	}

	/**
	 * Get the fighter's armor.
	 * @return - the fighter's armor
	 */
	public ArmorFactory.Armor getArmor() {
		return armor;
	}

	@Override
	public int compareTo(@NotNull Fighter o) {
		return this.name.compareTo(o.name);
	}
}
