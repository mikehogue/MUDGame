package GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Character is an abstract class which is used to represent any player or
 * computer controlled characters that can be found within the MUD.
 * 
 * @author Matthew Latura
 * 
 */
public abstract class Character implements Serializable {

	// FIELDS *********************************************************
	private String name;
	protected int currentHealth;
	protected int maxHealth;
	private Point mapLocation;
	protected ArrayList<Item> inventory;
	private String description;
	protected HashMap<Character, Integer> neighborDistances;
	private double attackDelay;
	private boolean readyToAttack;
	private static final long serialVersionUID = 1L;

	/**
	 * The amount of strength this Character has.
	 */
	public int strength;
	/**
	 * The amount of dexterity this Character has.
	 */
	public int dexterity;
	/**
	 * The amount of armor this Character has.
	 */
	public int armor;
	/**
	 * The amount of precision this Character has.
	 */
	public int precision;

	// METHODS***********************************************************************
	/**
	 * Sets the Character's name
	 * 
	 * @param name
	 *            - the new name to be used by this Character
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns this Character's name
	 * 
	 * @return this Character's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the characters current health to the passed integer value
	 * 
	 * @param health
	 *            - the integer value to set the Character's health to
	 */
	public void setCurrentHealth(int health) {
		this.currentHealth = health;
	}

	/**
	 * Returns the character's current health
	 * 
	 * @return the character's current health
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * Set's the Character's location to the passed Point
	 * 
	 * @param loc
	 *            - the point to set the Character's location to
	 */
	public void setLocation(Point loc) {
		this.mapLocation = loc;
	}

	/**
	 * Returns the Character's location
	 * 
	 * @return the Character's location
	 */
	public Point getLocation() {
		return this.mapLocation;
	}

	/**
	 * Set's the Character's max health to the passed integer value
	 * 
	 * @param max
	 *            - the integer value to set the Character's max health to
	 */
	public void setMaxHealth(int max) {
		this.maxHealth = max;
	}

	/**
	 * Returns the Character's max health
	 * 
	 * @return the Character's max health
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Will subtract the passed integer from the player's current health. If
	 * Character's health cannot fall below zero and cannot go above it's
	 * maximum health.
	 * 
	 * @param damage
	 *            - the integer value to subtract from current health
	 */
	public void takeDamage(int damage) {
		currentHealth -= damage;
		if (currentHealth < 0) {
			currentHealth = 0;
		} else if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}

	/**
	 * Returns whether or not the Character is dead
	 * 
	 * @return if the Character's health equals 0
	 */
	public boolean isDead() {
		return (currentHealth <= 0);
	}

	/**
	 * Returns the Character's inventory
	 * 
	 * @return the inventory of the Character as an ArrayList of Items
	 */
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	/**
	 * Changes the players location to the Point one unit in the passed
	 * Direction. This method will not check for exits or boundaries, these
	 * checks should be done before calling the method
	 * 
	 * @param direction
	 *            - the direction to move the player's location in
	 */
	public void moveRoom(Direction direction) {
		this.mapLocation = direction.move(this.mapLocation);
	}

	/**
	 * Adds the passed Item to the Character's inventory
	 * 
	 * @param i
	 *            - Item to be given to the Character
	 */
	public void giveItem(Item i) {
		this.inventory.add(i);
	}

	/**
	 * Checks the Character's inventory for an Item matching the name passed and
	 * returns the Item object if found, returns null if no Item was found
	 * 
	 * @param itemName
	 *            - the name of the Item to check for
	 * @return the Item object if found, null if no Item was found
	 */
	public Item hasItem(String itemName) {
		Item item = null;
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).getName().toLowerCase()
					.equals(itemName.toLowerCase())) {
				item = inventory.get(i);
				return item;
			}
		}
		return item;
	}

	/**
	 * Returns this Character's description
	 * 
	 * @return the description of this Character
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description field of this Character
	 * 
	 * @param description
	 *            - the description to set for this Character
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Adds a Character to this Character's map of neighbors and sets the
	 * starting distance at 2
	 * 
	 * @param c
	 *            - the Character to add to this Character's map of neighbors
	 */
	public void addNeighbor(Character c) {
		if (c != this) {
			neighborDistances.put(c, 2);
		}
	}

	/**
	 * Clears this Character's map of neighbors
	 */
	public void clearNeighbors() {
		neighborDistances.clear();
	}

	/**
	 * Moves the passed Character a number of units closer to this Character on
	 * his map of neighbors
	 * 
	 * @param c
	 *            - The Character to move closer
	 * @param unitsCloser
	 *            - The number of units to move closer
	 * @return the new distance between the two Characters
	 */
	public int closerTo(Character c, int unitsCloser) {
		if (neighborDistances.get(c) == null)
			return 3;
		int newDistance = neighborDistances.get(c) - unitsCloser;
		if (newDistance < 1) {
			newDistance = 1;
		}
		neighborDistances.put(c, newDistance);
		return newDistance;
	}

	/**
	 * Moves the passed Character a number of units further from this Character
	 * on his map of neighbors
	 * 
	 * @param c
	 *            - The Character to move further
	 * @param unitsFurther
	 *            - The number of units to move further
	 * @return the new distance between the two Characters
	 */
	public int furtherFrom(Character c, int unitsFurther) {
		int newDistance = neighborDistances.get(c) + unitsFurther;
		if (newDistance > 3) {
			newDistance = 3;
		}
		neighborDistances.put(c, newDistance);
		return newDistance;
	}

	/**
	 * Removes a Character from this Character's map of neighbors
	 * 
	 * @param c
	 *            - the Character to remove
	 */
	public void removeNeighbor(Character c) {
		neighborDistances.remove(c);
	}

	/**
	 * Returns the distance between this Character and the one passed
	 * 
	 * @param c
	 *            - The character to check the distance from
	 * @return - The distance between the two Characters, returns 10 if the
	 *         passed Character was not on this Character's list of neighbors
	 */
	public int distanceFrom(Character c) {
		if (neighborDistances.get(c) != null)
			try {
				return neighborDistances.get(c);
			} catch (NullPointerException e) {
				// e.printStackTrace();
				System.out.println("again null");
			}

		return 10;
	}

	/**
	 * Returns the range at which this Character can attack from
	 * 
	 * @return the range at which this Character can attack from
	 */
	abstract public int getAttackRange();

	/**
	 * Returns this Character's damage
	 * 
	 * @return This Character's damage
	 */
	abstract public int getDamage();

	/**
	 * Returns this Character's bonus to dodging attacks
	 * 
	 * @return Returns this Characters bonus to dodging attacks
	 */
	abstract public int getDodgeBonus();

	/**
	 * Returns this Character's accuracy bonus
	 * 
	 * @return this Character's accuracy bonus
	 */
	abstract public int getAccuracyBonus();

	/**
	 * Returns the percent of damage that this Character absorbs
	 * 
	 * @return the percent of damage that this Character absorbs
	 */
	abstract public int getDamageReduction();

	/**
	 * Returns the name of the attack of this Character
	 * 
	 * @return the name of the attack of this Character
	 */
	abstract public String getAttackName();

	/**
	 * Returns whether or not this character is ready to attack
	 * 
	 * @return whether or not this character is ready to attack
	 */
	public boolean readyToAttack() {
		return readyToAttack;
	}

	/**
	 * sets this Character's readyToAttack field to false and then waits for an
	 * amount of time equal to his attack delay in seconds before setting
	 * readyToAttack to true
	 */
	public void attack() {
		readyToAttack = false;
		Thread t = new Thread() {
			public void run() {
				try {
					sleep((int) (attackDelay * 1000));
					readyToAttack = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	/**
	 * Sets this Character's attackDelay field to the passed Double value. The
	 * unit of time used as the delay is seconds
	 * 
	 * @param d
	 *            - the value to set attackDelay to
	 */
	public void setAttackDelay(Double d) {
		this.attackDelay = d;
	}

	/**
	 * Sets the Character's readyToAttack field to the passed boolean value
	 * 
	 * @param b
	 *            - the value to set readyToAttack to
	 */
	public void readyToAttack(boolean b) {
		readyToAttack = b;
	}

	/**
	 * Returns the Character's attack delay in seconds
	 * 
	 * @return the Character's attack delay in seconds
	 */
	public double getAttackDelay() {
		return attackDelay;
	}
}
