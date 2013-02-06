package GameModel;

/**
 * Armor is a subclass of item as well as equipable. When it is equipped by the
 * player it grants a damage reduction bonus. Each piece of armor specifies
 * which equip slot it can be equipped to.
 * 
 * @author Matthew Latura
 * 
 */
public class Armor extends Equipable {
	private static final long serialVersionUID = 1L;
	private int ac;

	/**
	 * Default constructor for item. Sets the name, weight, description, ac, and
	 * equipSlot fields to the passed values
	 * 
	 * @param name
	 *            - name of the armor
	 * @param weight
	 *            - weight of the armor
	 * @param description
	 *            - short description of the armor
	 * @param armorBonus
	 *            - the damage reduction % bonus granted when wearing this Armor
	 * @param equipSlot
	 *            - the equipSlot this armor can be put into
	 */
	public Armor(String name, int weight, String description, int armorBonus,
			EquipSlot equipSlot) {
		super(name, weight, description);
		ac = armorBonus;
		this.equipSlot = equipSlot;
	}

	/**
	 * Returns the armor bonus granted by this item
	 * 
	 * @return The armor bonus granted by this item
	 */
	public int getAC() {
		return ac;
	}

}
