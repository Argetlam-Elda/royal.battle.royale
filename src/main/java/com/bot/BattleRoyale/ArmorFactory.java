package com.bot.BattleRoyale;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Build armor from the stats given in the config file.
 * @author ArgetlamElda
 */
public class ArmorFactory {

	private static final String CHANCE_RANGE_MIN = "CHANCE RANGE MIN";
	private static final String CHANCE_RANGE_MAX = "CHANCE RANGE MAX";
	private static final String START_ARMOR = "START ARMOR";
	private static final String END_ARMOR = "END ARMOR";

	private static final String NAME = "NAME";
	private static final String DAMAGE_RESIST = "RESIST";
	private static final String BREAK_SAVE = "BREAK SAVE";


	/**
	 * Stores all the data needed for a weapon, including damage and flavor text.
	 */
	public class Armor implements Cloneable {

		/**
		 * The armor's name.
		 */
		private String name;

		/**
		 * The armor's damage resistance.
		 */
		private int resist;

		/**
		 * The armor's cripple resistance.
		 */
		private int crippleSave;

		/**
		 * The upper limit of this armor's spawn range.
		 */
		private int spawnRangeMax;

		/**
		 * The lower limit of this armor's spawn range.
		 */
		private int spawnRangeMin;

		/**
		 * Initialize this armor with no name, 0 resistance, and 0 cripple resistance.
		 */
		private Armor() {
			name = "";
			resist = 0;
			crippleSave = 0;
		}

		/**
		 * Check if spawnChance is within this armor's spawn range.
		 * @param spawnChance - the (usually random) number that determines what armor you get
		 * @return - whether you get this weapon or not
		 */
		private boolean isInSpawnRange(int spawnChance) {
			return (spawnChance < spawnRangeMax && spawnChance >= spawnRangeMin);
		}

		/**
		 * Get this armor's damage resistance.
		 * @return - the damage resistance
		 */
		public int getResist() {
			return resist;
		}

		public String toString() {
			return name;
		}

		/**
		 * Attempt to cripple this armor with the given cripple damage
		 * @param crippleDamage - damage done to the armor's durability
		 * @param rand - random number generator for crippling armor
		 * @return - whether the armor was crippled or not
		 */
		public boolean cripple(int crippleDamage, Random rand) {
			if (rand.nextInt(10) + crippleDamage > 7 + crippleSave) {
				// TODO - what to do on a cripple
				name = "damaged " + name;
				resist = 0;
				return true;
			}
			return false;
		}

		/**
		 * Check if the armor is correctly built or not
		 * @return - this armor has all the right values
		 */
		private boolean isBuilt() {
			return name != null;
		}

		@Override
		public Armor clone() {
			Armor armor = new Armor();
			armor.name = name;
			armor.resist = resist;
			armor.crippleSave = crippleSave;
			return armor;
		}
	}

	private static ArmorFactory instance;

	public static ArmorFactory getInstance() {
		if (instance == null) {
			instance = new ArmorFactory();
		}
		return instance;
	}

	/**
	 * A list of the armors generated from the config file.
	 */
	private ArrayList<Armor> armors;

	/**
	 * The maximum possible spawn range for these armor.
	 */
	private int rangeMax;

	/**
	 * The minimum possible spawn range for these armor.
	 */
	private int rangeMin;

	/**
	 * Initialize the armor factory and read in armor data from the config file.
	 * @throws IllegalStateException - the armor ranges have gaps
	 */
	private ArmorFactory() throws IllegalStateException {
		armors = new ArrayList<>();
		File file = new File("config/Battle_Royale_Configs/Battle_Royale_Armor.txt");
		readWeaponsFromFile(file);
	}

	/**
	 * "Build" a piece of armor from the available armor in the factory.
	 * @return - a randomly selected armor
	 */
	public Armor buildArmor() {
		Random rand = new Random();
		int chance = rand.nextInt(rangeMax - rangeMin) + rangeMin;
		for (Armor armor: armors) {
			if (armor.isInSpawnRange(chance)) {
				return armor.clone();
			}
		}
		return null;
	}

	public int getMaxResist() {
		int resist = 0;
		for (Armor armor: armors) {
			resist = Math.max(armor.resist, resist);
		}
		return resist;
	}

	/**
	 * Go through the given file and generate armor for each stat block.
	 * @param file -  file containing all the armor stat blocks
	 * @throws IllegalStateException - the armor ranges have gaps
	 */
	private void readWeaponsFromFile(File file) throws IllegalStateException {
		try {
			Scanner fileScanner = new Scanner(file);
			boolean rangeMinB = false, rangeMaxB = false;
			while (fileScanner.hasNext() && !(rangeMaxB && rangeMinB)) {
				String line = fileScanner.nextLine();
				if (line.equals(CHANCE_RANGE_MIN)) {
					rangeMin = Integer.parseInt(fileScanner.nextLine());
					rangeMinB = true;
				}
				else if (line.equals(CHANCE_RANGE_MAX)) {
					rangeMax = Integer.parseInt(fileScanner.nextLine());
					rangeMaxB = true;
				}
			}
			while (fileScanner.hasNext()) {
				String line = fileScanner.nextLine();
				if (line.equals(START_ARMOR)) {
					readWeapon(fileScanner);
				}
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int bottomRange = rangeMin;
		while (bottomRange != rangeMax) {
			int tempBottomRange = bottomRange;
			for (Armor armor: armors) {
				if (armor.spawnRangeMin == bottomRange) {
					bottomRange = armor.spawnRangeMax;
					break;
				}
			}
			if (tempBottomRange == bottomRange) {
				throw new IllegalStateException("There is a gap in the ranges of the weapons loaded from " + file.getAbsolutePath());
			}
		}
	}

	/**
	 * Go through the given stat block and generate a piece of armor from the given information.
	 * @param fileScanner - a scanner set to the beginning of the stat block to scan
	 */
	private void readWeapon(@NotNull Scanner fileScanner) {
		HashMap<String, Boolean> check = new HashMap<>();
		check.put(NAME, false);
		check.put(DAMAGE_RESIST, false);
		check.put(BREAK_SAVE, false);
		check.put(CHANCE_RANGE_MAX, false);
		check.put(CHANCE_RANGE_MIN, false);
		Armor armor = new Armor();
		boolean ready;
		while (fileScanner.hasNext()) {
			String line = fileScanner.nextLine();
			if (line.equals(END_ARMOR)) {
				break;
			}
			else if (check.keySet().contains(line)) {
				if (check.get(line)) {
					ready = false;
				}
				switch (line) {
					case NAME:
						armor.name = fileScanner.nextLine();
						break;
					case DAMAGE_RESIST:
						armor.resist = Integer.parseInt(fileScanner.nextLine());
						break;
					case BREAK_SAVE:
						armor.crippleSave = Integer.parseInt(fileScanner.nextLine());
						break;
					case CHANCE_RANGE_MAX:
						armor.spawnRangeMax = Integer.parseInt(fileScanner.nextLine());
						break;
					case CHANCE_RANGE_MIN:
						armor.spawnRangeMin = Integer.parseInt(fileScanner.nextLine());
						break;
					default:
						// make sure nothing here happens
						break;
				}
				check.put(line, true);
			}
			// TODO - get chances of starting with each armor
		}
		// check that all parts of the weapon were added
		ready = armor.isBuilt();
		for (Boolean bool: check.values()) {
			if (!bool) {
				// TODO - not ready armor
				ready = false;
			}
		}
		if (ready) {
			armors.add(armor);
		}
		else {
			System.out.println(check.toString());
		}
	}
}
