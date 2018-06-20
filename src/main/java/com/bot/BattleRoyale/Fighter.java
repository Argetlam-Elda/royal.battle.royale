package com.bot.BattleRoyale;

import net.dv8tion.jda.core.entities.Member;

public class Fighter {

	private String name;

	private int hp;

	private Fighter revenge;

	private WeaponFactory.Weapon weapon;

	private ArmorFactory.Armor armor;

	public Fighter() {
		hp = 100;
		revenge = null;
		name = "";
		// WeaponFactory.getInstance().buildWeapon();
		// ArmorFactory.getInstance().buildArmor();
	}

	public Fighter(Member member) {
		this();
		name = member.getNickname();
		if (name == null) {
			name = member.getEffectiveName();
		}
	}
}
