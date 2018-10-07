package com.bot.BattleRoyale;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import com.bot.BattleRoyale.ArmorFactory.Armor;
import org.jetbrains.annotations.NotNull;

/**
 * Build weapons from the stats given in the config file.
 * @author ArgetlamElda
 */
public class WeaponFactory {

	private static final String CHANCE_RANGE_MIN = "CHANCE RANGE MIN";
	private static final String CHANCE_RANGE_MAX = "CHANCE RANGE MAX";
	private static final String START_WEAPON = "START WEAPON";
	private static final String END_WEAPON = "END WEAPON";

	private static final String NAME = "NAME";
	private static final String DAMAGE = "DAMAGE";
	private static final String NORMAL_HIT_MESSAGE = "NORMAL HIT MESSAGE";
	private static final String CRITICAL_HIT_MESSAGE = "CRITICAL HIT MESSAGE";
	private static final String SELF_HIT_MESSAGE = "SELF HIT MESSAGE";
	private static final String SUICIDE_MESSAGE = "SUICIDE MESSAGE";
	private static final String CRIPPLE_DAMAGE = "CRIPPLE DAMAGE";

	/**
	 * Stores all the data needed for a weapon, includeing damage and flavor text.
	 */
	public class Weapon implements Cloneable {

		/**
		 * The weapon's name.
		 */
		private String name;

		/**
		 * The weapon's damage.
		 */
		private int damage;

		/**
		 * The upper limit of this weapon's spawn range.
		 */
		private int spawnRangeMax;

		/**
		 * The lower limit of this weapon's spawn range.
		 */
		private int spawnRangeMin;

		/**
		 * The different flavor texts for this weapon.
		 */
		private HashMap<WeaponFlavor, String> flavors;

		/**
		 * The damage this weapon does to armor.
		 */
		private int crippleDamage;

		/**
		 * Initialize this weapon with no name, 0 damage, 0 cripple damage, and no flavor text.
		 */
		private Weapon() {
			name = "";
			damage = 0;
			crippleDamage = 0;
			flavors = new HashMap<>();
		}

		/**
		 * Check if spawnChane is within this weapon's spawn range.
		 * @param spawnChance - the (usually random) number that determines what weapon you get
		 * @return - whether you get this weapon or not
		 */
		private boolean isInSpawnRange(int spawnChance) {
			return (spawnChance < spawnRangeMax && spawnChance >= spawnRangeMin);
		}

		/**
		 * Get the flavor text for this weapon for the specified kind of hit.
		 * @param flavorKey - the key corresponding to the type of damage done, and thus the type of flavor text needed
		 * @return - flavorKey's corresponding flavor text
		 */
		public String getFlavor(WeaponFlavor flavorKey) {
			return flavors.get(flavorKey);
		}

		public int getFlavorTextLength() {
			int length = 0;
			for (String flavor: flavors.values()) {
				length = Math.max(flavor.length(), length);
			}
			return length;
		}

		/**
		 * Get this weapon's damage.
		 * @return - the weapon's damage
		 */
		public int getDamage() {
			return damage;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public Weapon clone() {
			Weapon weapon = new Weapon();
			weapon.flavors = flavors;
			weapon.damage = damage;
			weapon.name = name;
			weapon.crippleDamage = crippleDamage;
			return weapon;
		}

		/**
		 * Attempt to cripple the given armor with this weapon.
		 * @param armor - to be crippled
		 * @param rand - random number generator for crippling armor
		 * @return - weather the armor was crippled or not
		 */
		public boolean cripple(Armor armor, Random rand) {
			return armor.cripple(crippleDamage, rand);
		}

		/**
		 * Check if this weapon is correctly built or not.
		 * @return - this weapon has all the right values
		 */
		private boolean isBuilt() {
			if (name == null) {
				return false;
			}
			for (WeaponFlavor flavor: WeaponFlavor.values()) {
				if (flavors.get(flavor) ==  null) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * The different types of damage flavor text.
	 */
	public enum WeaponFlavor {
		HIT, CRIT, SELF, SUICIDE
	}

	private static WeaponFactory instance;

	public static WeaponFactory getInstance() {
		if (instance == null) {
			instance = new WeaponFactory();
		}
		return instance;
	}

	/**
	 * A list of the weapons generated from the config file.
	 */
	private ArrayList<Weapon> weapons;

	/**
	 * The maximum possible spawn range for these weapons.
	 */
	private int rangeMax;

	/**
	 * The minimum possible spawn range for these weapons.
	 */
	private int rangeMin;

	/**
	 * Initialize the weapon factory and read in weapon data from the config file.
	 * @throws IllegalStateException - the weapon ranges have gaps
	 */
	private WeaponFactory() throws IllegalStateException {
		weapons = new ArrayList<>();
		File file = new File("config/Battle_Royale_Configs/Battle_Royale_Weapons.txt");
		readWeaponsFromFile(file);
	}

	/**
	 * "Build" a weapon from the available weapons in the factory.
	 * @return - a randomly selected weapon
	 */
	public Weapon buildWeapon() {
		Random rand = new Random();
		int chance = rand.nextInt(rangeMax - rangeMin) + rangeMin;
		for (Weapon weapon: weapons) {
			if (weapon.isInSpawnRange(chance)) {
				return weapon.clone();
			}
		}
		return null;
	}

	/**
	 * Get the highest damage a weapon can do
	 * @return - the highest damage of any weapon in the game
	 */
	public int getMaxDamage() {
		int damage = 0;
		for (Weapon weapon: weapons) {
			damage = Math.max(weapon.damage, damage);
		}
		return damage;
	}

	/**
	 * Go through the given file and generate weapons for each block of stats.
	 * @param file - file containing all the weapon stat blocks
	 * @throws IllegalStateException - the weapon ranges have gaps
	 */
	private void readWeaponsFromFile(File file) throws IllegalStateException {
		try {
			HashMap<String, String> hashMap = new HashMap<>();
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
				if (line.equals(START_WEAPON)) {
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
			for (Weapon weapon: weapons) {
				if (weapon.spawnRangeMin == bottomRange) {
					bottomRange = weapon.spawnRangeMax;
					break;
				}
			}
			if (tempBottomRange == bottomRange) {
				throw new IllegalStateException("There is a gap in the ranges of the weapons loaded from " + file.getAbsolutePath());
			}
		}
	}

	/**
	 * Go through a given stat block and generate a weapon from the given information.
	 * @param fileScanner - a scanner set to the beginning of the stat block to scan
	 */
	private void readWeapon(@NotNull Scanner fileScanner) {
		HashMap<String, Boolean> check = new HashMap<>();
		check.put(NAME, false);
		check.put(DAMAGE, false);
		check.put(CRIPPLE_DAMAGE, false);
		check.put(NORMAL_HIT_MESSAGE, false);
		check.put(CRITICAL_HIT_MESSAGE, false);
		check.put(SELF_HIT_MESSAGE, false);
		check.put(SUICIDE_MESSAGE, false);
		check.put(CHANCE_RANGE_MAX, false);
		check.put(CHANCE_RANGE_MIN, false);
		Weapon weapon = new Weapon();
		boolean ready = true;
		while (fileScanner.hasNext()) {
			String line = fileScanner.nextLine();
			if (line.equals(END_WEAPON)) {
				break;
			}
			else if (check.keySet().contains(line)) {
				if (check.get(line)) {
					ready = false;
				}
				if (line.equals(NAME)) {
					weapon.name = fileScanner.nextLine();
				} else if (line.equals(DAMAGE)) {
					weapon.damage = Integer.parseInt(fileScanner.nextLine());
				} else if (line.equals(CRIPPLE_DAMAGE)) {
					weapon.crippleDamage = Integer.parseInt(fileScanner.nextLine());
				} else if (line.equals(NORMAL_HIT_MESSAGE)) {
					weapon.flavors.put(WeaponFlavor.HIT, fileScanner.nextLine());
				} else if (line.equals(CRITICAL_HIT_MESSAGE)) {
					weapon.flavors.put(WeaponFlavor.CRIT, fileScanner.nextLine());
				} else if (line.equals(SELF_HIT_MESSAGE)) {
					weapon.flavors.put(WeaponFlavor.SELF, fileScanner.nextLine());
				} else if (line.equals(SUICIDE_MESSAGE)) {
					weapon.flavors.put(WeaponFlavor.SUICIDE, fileScanner.nextLine());
				} else if (line.equals(CHANCE_RANGE_MAX)) {
					weapon.spawnRangeMax = Integer.parseInt(fileScanner.nextLine());
				} else if (line.equals(CHANCE_RANGE_MIN)) {
					weapon.spawnRangeMin = Integer.parseInt(fileScanner.nextLine());
				}
				else {
					// In case there is something in the check that's not here yet
					break;
				}
				check.put(line, true);
			}
			// TODO - get chances of starting with each weapon
		}
		// check that all parts of the weapon were added
		ready = weapon.isBuilt();
		for (Boolean bool: check.values()) {
			if (!bool) {
				// TODO - not ready weapon
				ready = false;
			}
		}
		if (ready) {
			weapons.add(weapon);
		}
		else {
			System.out.println(check.toString());
		}
	}

}
