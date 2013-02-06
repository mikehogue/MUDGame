package GameModel.mobs;

import java.util.Iterator;

import GameModel.Character;
import GameModel.GameSimulation;
import GameModel.ItemFactory;
import GameModel.Player;
import GameModel.Point;
import GameModel.World;

/**
 * Final boss of the game. Tons of health, high chance to hit, deals tons of
 * damage. If possible, when it takes damage, it ignores something like 10-20%
 * of it, thanks to the protection of thick scales.
 * 
 * 1 dragon in room 35, stationary
 * 
 * 2 attacks, bite: 20 damage, range 1 flame: 25 damage, range 3
 * 
 * @author Mazen Shihab
 * 
 */
@SuppressWarnings("serial")
public class Dragon extends Mob {
	private transient Thread t;
	private Player target;
	private String lastAttack;
	private GameSimulation gs;

	/**
	 * set the attack delay of this enemy at 8 second. set the enemy to ready to
	 * attack. set the Location, the current and max health of the dragon to
	 * 250. set the name and description of the dragon. initiate the thread. and
	 * add the mob to the world. set the default attack to flame. add the items
	 * which the dragon carries. set the experience to 1000.
	 * 
	 * @param w
	 *            the world of which the mobs are in.
	 * @param p
	 *            the location of the mob.
	 * @param gs
	 *            the GameSimulation which contain the Mob,
	 * 
	 */
	public Dragon(World w, Point p, GameSimulation gs) {
		super(w);
		this.gs = gs;
		this.setLocation(p);
		t = new Thread(this, "dragon");
		this.setName("Dragon");
		this.setDescription("A massive, flaming, scaled monstrosity. And you're wearing what basically amounts to an oven.");
		this.setCurrentHealth(250);
		this.setMaxHealth(250);
		t = new Thread(this, "Dragon");
		this.setAttackDelay(8.0);
		target = null;
		lastAttack = "flame";
		this.setExperienceWorth(1000);
		w.addCharacter(this);
		this.inventory.add(ItemFactory.makeArmor("Shield of Hogue"));
		this.inventory.add(ItemFactory.makeWeapon("Axe of Shihab"));
		this.inventory.add(ItemFactory.makeArmor("Breastplate of Conway"));
		this.inventory.add(ItemFactory.makeArmor("Helm of Latura"));

	}

	/*
	 * public Point getLocation() { return new Point(17, 8); }
	 */

	@Override
	public void attackedBy(Character ch) {
		if (target == null) {
			target = (Player) ch;
		}
	}

	/**
	 * check if there is a player in the room, if so start attacking the player.
	 * if the player distance from the dragon is 3 the attack will be flame, if
	 * the distance is 1 the attack will be a bite.
	 */
	@Override
	public void run() {
		try {
			while (!this.isDead()) {
				Thread.sleep(1000);
				if (target != null)
					if (!w.getArea(this.getLocation())
							.containsCharacter(target)) {
						target = null;
					}
				if (target == null) {
					Iterator<Player> i = w.getArea(this.getLocation())
							.getPlayers();
					while (i.hasNext()) {
						target = i.next();
					}
				}
				if (target != null) {
					if (this.distanceFrom(target) == 1) {
						if (this.readyToAttack()) {
							this.setAttackDelay(4.0);
							lastAttack = "bite";
							if (target != null
									&& (target.getLocation().getX() == this
											.getLocation().getX() && target
											.getLocation().getY() == this
											.getLocation().getY()))
								w.attack(this, target);
						}
					} else if (this.distanceFrom(target) > 1
							&& this.readyToAttack()) {
						lastAttack = "flame";
						this.setAttackDelay(6.0);
						if (target != null
								&& (target.getLocation().getX() == this
										.getLocation().getX() && target
										.getLocation().getY() == this
										.getLocation().getY()))
							w.attack(this, target);
					}
				}
			}
		} catch (InterruptedException e) {
			// e.printStackTrace();
			System.out.println("end of life");
		}

	}

	@Override
	public void getGoing() {
		t = new Thread(this, "Dragon");
		t.start();

	}

	@Override
	public int getAttackRange() {
		if (lastAttack.equals("flame")) {
			return 3;
		} else {
			return 1;
		}
	}

	@Override
	public int getDamage() {
		if (lastAttack.equals("flame")) {
			return 40;
		} else {
			return 35;
		}
	}

	@Override
	public int getDodgeBonus() {
		return 10;
	}

	@Override
	public int getAccuracyBonus() {
		if (lastAttack.equals("flame")) {
			return 10;
		} else {
			return 15;
		}

	}

	@Override
	public int getDamageReduction() {

		return 20;
	}

	@Override
	public String getAttackName() {

		return lastAttack;
	}

	@Override
	public void kill() {
		t.interrupt();
		gs.mobDied(this);
	}

}
