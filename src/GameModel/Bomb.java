package GameModel;

/**
 * Bombs are room-dependent items. If used in the appropriate rooms, they can
 * uncover hidden items - or hidden mobs.
 * 
 * @author Chris
 * 
 */
public class Bomb extends Item {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a Bomb item.
	 */
	public Bomb() {
		super(
				"Bomb",
				1,
				"Monsters would just walk around it. You could probably hurt some walls, though.");
	}

	/**
	 * If the room defined by p is bombable, it will do the appropriate bomb
	 * effects. Otherwise, nothing will happen apart from a notification.
	 * 
	 * @param p
	 *            The player using the bomb.
	 * @param w
	 *            The world in which the bomb is being used.
	 */
	public void use(Player p, World w) {
		int x = p.getLocation().getX();
		int y = p.getLocation().getY();
		// Sunshine item hidden in the collapsed cliffside.
		if (x == 9 && y == 1) {
			w.getArea(p.getLocation()).addItem(
					ItemFactory.makeConsumable("Small Health Potion"));
			w.getArea(p.getLocation()).addItem(new SunWand(w));
			w.sendNotification(new Notification(
					p,
					"The blast uncovers a small health potion and some sort of wand beneath the rocks!",
					false));
			p.dropItem("Bomb");
		}
		// Medium Health Pot and Claymore hidden in the goblin trash heap.
		else if (x == 10 && y == 3) {
			w.getArea(p.getLocation()).addItem(
					ItemFactory.makeConsumable("Medium Health Potion"));
			w.getArea(p.getLocation()).addItem(
					ItemFactory.makeWeapon("Claymore"));
			w.sendNotification(new Notification(
					p,
					"The blast uncovers a medium health potion and a claymore, buried behind some trash!",
					false));
			p.dropItem("Bomb");
		}
		// 2 Large Health Pots and Ancient Helmet hidden at the end of the long
		// northern passage.
		else if (x == 11 && y == 7) {
			w.getArea(p.getLocation()).addItem(
					ItemFactory.makeConsumable("Large Health Potion"));
			w.getArea(p.getLocation()).addItem(
					ItemFactory.makeConsumable("Large Health Potion"));
			w.getArea(p.getLocation()).addItem(
					ItemFactory.makeArmor("Ancient Helmet"));
			w.sendNotification(new Notification(
					p,
					"The blast uncovers two large health potions and an ancient helmet, hidden against the northern wall!",
					false));
			p.dropItem("Bomb");
		} else {
			w.sendNotification(new Notification(p,
					"The room shakes, but nothing is uncovered.", false));
			p.dropItem("Bomb");
		}
	}

}
