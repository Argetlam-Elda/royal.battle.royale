package com.bot.BattleRoyale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ArmorFactory {

	private static final String CHANCE_RANGE_MIN = "CHANCE RANGE MIN";
	private static final String CHANCE_RANGE_MAX = "CHANCE RANGE MAX";
	private static final String START_ARMOR = "START ARMOR";
	private static final String END_ARMOR = "END ARMOR";

	private static final String NAME = "NAME";
	private static final String DAMAGE_RESIST = "RESIST";
	private static final String BREAK_SAVE = "BREAK SAVE";


	public class Armor implements Cloneable {

		private String name;
		private int resist;

		private HashMap<ArmorFlavor, String> flavor;

		private int crippleSave;

		private Armor() {
			name = "";
			resist = 0;
			flavor = new HashMap<>();
			crippleSave = 0;
		}

		public String getFlavor(ArmorFlavor flavorKey) {
			return flavor.get(flavorKey);
		}

		public int getResist() {
			return resist;
		}

		public String toString() {
			return name;
		}

		public boolean cripple(int crippleDamage, Random rand) {
			if (rand.nextInt(10) + crippleDamage > 7 + crippleSave) {
				// TODO - what to do on a cripple
				name = "damaged " + name;
				resist = 0;
				return true;
			}
			return false;
		}

	}

	public enum ArmorFlavor {

	}

	private static ArmorFactory instance;

	public static ArmorFactory getInstance() {
		if (instance == null) {
			instance = new ArmorFactory();
		}
		return instance;
	}

	private ArrayList<Armor> armors;

	private ArmorFactory() {
		armors = new ArrayList<>();
		// TODO - init from file
	}

	public Armor buildArmor() {
		return null;
	}

}
