package GameModel;

/**
 * A large healing potion.
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class LargeHealthPot extends Consumable{

	/**
	 * Generates a new LargeHealthPot.
	 */
	public LargeHealthPot() {
		super("Large Health Potion", 0, "A large health potion. For growing back limbs.");
	}

	@Override
	public void use(Player p) {
		p.takeDamage(-100);
		p.dropItem("Large Health Potion");
	}

}
