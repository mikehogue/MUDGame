package GameModel;

/**
 * Superclass for unequippable items whose use methods only effect the player.
 * 
 * @author Chris
 * 
 */
@SuppressWarnings("serial")
public abstract class Consumable extends Item {

	/**
	 * Creates a new Consumable-type Item with the given name, weight, and
	 * descriptions.
	 * 
	 * @param n
	 *            Name of the item.
	 * @param w
	 *            Weight of the item.
	 * @param d
	 *            Description of the item.
	 */
	public Consumable(String n, int w, String d) {
		super(n, w, d);
	}

	/**
	 * Calls the consumable's use method to effect the player.
	 * 
	 * @param p
	 */
	public abstract void use(Player p);
}
