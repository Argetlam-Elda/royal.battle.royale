package com.bot.BattleRoyale;

public class WeaponFactory {
	public class Weapon {

	}

	private static WeaponFactory instance;

	public static WeaponFactory getInstance() {
		if (instance == null) {
			instance = new WeaponFactory();
		}
		return instance;
	}

	private WeaponFactory() {

	}
}
