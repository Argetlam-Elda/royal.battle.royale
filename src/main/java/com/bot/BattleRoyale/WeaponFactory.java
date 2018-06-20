package com.bot.BattleRoyale;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import com.bot.BattleRoyale.ArmorFactory.Armor;

public class WeaponFactory {

	public static final String CHANCE_RANGE_MIN = "CHANCE RANGE MIN";
	public static final String CHANCE_RANGE_MAX = "CHANCE RANGE MAX";
	public static final String START_WEAPON = "START WEAPON";
	public static final String END_WEAPON = "END WEAPON";

	public static final String NAME = "NAME";
	public static final String DAMAGE = "DAMAGE";
	public static final String NORMAL_HIT_MESSAGE = "NORMAL HIT MESSAGE";
	public static final String CRITICAL_HIT_MESSAGE = "CRITICAL HIT MESSAGE";
	public static final String SELF_HIT_MESSAGE = "SELF HIT MESSAGE";
	public static final String SUICIDE_MESSAGE = "SUICIDE MESSAGE";
	public static final String CRIPPLE_DAMAGE = "CRIPPLE DAMAGE";


	public class Weapon implements Cloneable {

		private String name;
		private int damage;

		private int spawnRangeMax;
		private int spawnRangeMin;

		private HashMap<WeaponFlavor, String> flavors;

		private int crippleDamage;

		private Weapon() {
			name = "";
			damage = 0;
			crippleDamage = 0;
			flavors = new HashMap<>();
		}

		private boolean isInSpawnRange(int spawnChance) {
			return (spawnChance < spawnRangeMax && spawnChance >= spawnRangeMin);
		}

		public String getFlavor(WeaponFlavor flavorKey) {
			return flavors.get(flavorKey);
		}

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

		public boolean cripple(Armor armor, Random rand) {
			return armor.cripple(crippleDamage, rand);
		}

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

	private ArrayList<Weapon> weapons;

	private int rangeMax;

	private int rangeMin;

	private WeaponFactory() throws IllegalStateException {
		weapons = new ArrayList<>();
		File file = new File("config/Battle_Royale_Configs/Battle_Royale_Weapons.txt");
		readWeaponsFromFile(file);
	}

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

	private void readWeapon(Scanner fileScanner) {
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
