package GameModel;

/**
 * A small healing potion.
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class SmallHealthPot extends Consumable {
	/**
	 * Generates a new SmallHealthPot.
	 */
	public SmallHealthPot()	{
		super("Small Health Potion", 0, "A small red potion. Heals minor wounds.");
	}

	@Override
	public void use(Player p) {
		p.takeDamage(-40);
		p.dropItem("Small Health Potion");
	}
	
}
