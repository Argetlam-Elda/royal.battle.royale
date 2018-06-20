package com.bot.BattleRoyale;

import net.dv8tion.jda.core.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class Fighter implements Comparable<Fighter> {

	private String name;

	private int hp;

	private Fighter revenge;

	private WeaponFactory.Weapon weapon;

	private ArmorFactory.Armor armor;

	public Fighter() {
		hp = 100;
		revenge = null;
		name = "";
		weapon = WeaponFactory.getInstance().buildWeapon();
		// armor = ArmorFactory.getInstance().buildArmor();
	}

	public Fighter(Member member) {
		this();
		name = member.getNickname();
		if (name == null) {
			name = member.getEffectiveName();
		}
	}

	public void takeDamage(int damage) {
		hp -= damage;
	}

	public String toString() {
		return name;
	}

	public int getHealth() {
		return hp;
	}

	public Fighter getRevengeTarget() {
		return revenge;
	}

	public WeaponFactory.Weapon getWeapon() {
		return weapon;
	}

	public ArmorFactory.Armor getArmor() {
		return armor;
	}

	@Override
	public int compareTo(@NotNull Fighter o) {
		return this.name.compareTo(o.name);
	}
}
