package GameModel;

/**
 * A Sun Wand is used to change the light of a room. If there is already sunlight in the room,
 * do not do anything. However, if the room is dark, light the room up for 10 seconds. 
 * @author Mike Hogue
 *
 */
@SuppressWarnings("serial")
public class SunWand extends Consumable {

	private World w;
	/**
	 * Construct a Sun Wand by setting the name and description. 
	 * @param w
	 * The World that the Sun Wand will be used in.
	 */
	public SunWand(World w) {
		super("Sun Wand", 0, "This mysterious item can turn even the darkest rooms sunny.");
		this.w = w;
	}

	@Override
	public void use(Player p) {
		w.getArea(p.getLocation()).setSunny(true, w, p);
		p.dropItem("Sun Wand");
	}
}
