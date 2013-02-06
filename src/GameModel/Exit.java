package GameModel;

import java.io.Serializable;

/**
 * Exit is used to represent a way out of an Area. They can be locked or
 * unlocked to limit movement between Areas
 * 
 * @author Matthew Latura
 * 
 */
public class Exit implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean locked;
	private Item key;

	/**
	 * Default constructor. The locked field of the Exit is set to the passed
	 * boolean value and if an Item is passed it will be set as the key that can
	 * unlock this Exit.
	 * 
	 * @param b
	 *            - Whether or not the Exit is locked
	 * @param i
	 *            - The Item used as a key to unlock this Exit
	 */
	public Exit(boolean b, Item i) {
		locked = b;
		if (i != null) {
			key = i;
		}
	}

	/**
	 * Locks this Exit
	 */
	public void lock() {
		locked = true;
	}

	/**
	 * Unlocks this Exit
	 */
	public void unlock() {
		locked = false;
	}

	/**
	 * Returns the Item that has been set as the key for this Exit
	 * 
	 * @return the Item that has been set as the key for this Exit
	 */
	public Item getKey() {
		return key;
	}

	/**
	 * Returns whether or not this Exit is locked
	 * 
	 * @return whether or not this Exit is locked
	 */
	public boolean isLocked() {
		return locked;
	}

}
