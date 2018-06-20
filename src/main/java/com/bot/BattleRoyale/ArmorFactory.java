package com.bot.BattleRoyale;

public class ArmorFactory {
	public class Armor {

	}

	private static ArmorFactory instance;

	public static ArmorFactory getInstance() {
		if (instance == null) {
			instance = new ArmorFactory();
		}
		return instance;
	}

	private ArmorFactory() {

	}

}
