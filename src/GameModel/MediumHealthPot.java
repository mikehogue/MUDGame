package GameModel;

/**
 * A medium healing potion.
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class MediumHealthPot extends Consumable {
	/**
	 * Generates a new MediumHealthPot.
	 */
	public MediumHealthPot()	{
		super("Medium Health Potion", 0, "A Medium-sized red potion. Heals burns and cuts.");
	}

	@Override
	public void use(Player p) {
		p.takeDamage(-70);
		p.dropItem("Medium Health Potion");
	}
	
}
