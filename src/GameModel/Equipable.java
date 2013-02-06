package GameModel;

/**
 * Equipable is an abstract subclass of Item that a Player can equip which will
 * grant bonuses to the Player's combat statistics
 * 
 * @author Matthew Latura
 * 
 */
public abstract class Equipable extends Item {
	private static final long serialVersionUID = 1L;
	private boolean isEquipped;
	protected EquipSlot equipSlot;

	/**
	 * The default constructor sets the name, weight, and description of the
	 * Equipable item using the passed parameters. the item will be unequipped
	 * and it's equip slot will be HAND
	 * 
	 * @param n
	 *            - The equipable item's name
	 * @param w
	 *            - The equipable item's weight
	 * @param d
	 *            - The equipable item's description
	 */
	public Equipable(String n, int w, String d) {
		super(n, w, d);
		isEquipped = false;
		equipSlot = EquipSlot.HAND;
	}

	/**
	 * A more powerful constructor. This sets the item's name, weight,
	 * description, and equip slot to the passed values. Is equipped is awlays
	 * set to false
	 * 
	 * @param n
	 *            - The equipable item's name
	 * @param w
	 *            - The equipable item's weight
	 * @param d
	 *            - The equipable item's description
	 * @param e
	 *            - The equipable item's equip slot
	 */
	public Equipable(String n, int w, String d, EquipSlot e) {
		this(n, w, d);
		equipSlot = e;
	}

	/**
	 * sets the equipped status of this equipable item to the passed boolean
	 * value
	 * 
	 * @param b
	 *            - the value to set as the equipped status of this item
	 */
	public void setEquipped(boolean b) {
		isEquipped = b;
	}

	/**
	 * Returns the equipped status of this item
	 * 
	 * @return the equipped status of this item
	 */
	public boolean isEquipped() {
		return isEquipped;
	}

	/**
	 * Returns the equip slot this item belongs in
	 * 
	 * @return the equip slot this item belongs in
	 */
	public EquipSlot getEquipSlot() {
		return equipSlot;
	}

}
