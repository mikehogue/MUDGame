package GameModel.mobs;

import java.util.Iterator;
import java.util.Random;

import GameModel.Character;
import GameModel.GameSimulation;
import GameModel.ItemFactory;
import GameModel.Player;
import GameModel.Point;
import GameModel.World;

/**
 * Tiny, mischievious creature. Very little health (probably between goblin and
 * rat), but very small and fast; hardest mob in the game to hit, but dies fast
 * when you do. Deals more damage than a skeleton, but not as much as a grue or
 * troll. High hit rate against the player.
 * 
 * @author Chris
 * 
 *         Rooms: 3 in room 24
 * 
 *         2 attacks claw short range not a lot of damage fire ball from
 *         distance more damage
 */
@SuppressWarnings("serial")
public class Imp extends Mob {

	private transient Thread t;
	private String lastAttack;
	private Player target;
	private GameSimulation gs;
	boolean firstTime;

	/**
	 * set the attack delay of this enemy at 5 seconds. set the enemy to ready
	 * to attack. set the Location, the current and max health of the Imp to 35.
	 * set the name and description of the Imp. initiate the thread. and add
	 * the mob to the world. set the default attack to fireball. add the items
	 * which the Imp carries. set the experience to 300.
	 * 
	 * @param w
	 *            the world of which the mobs are in.
	 * @param p
	 *            the location of the mob.
	 * @param gs
	 *            the GameSimulation which contain the Mob,
	 * 
	 */
	public Imp(World w, Point p, GameSimulation gs) {
		super(w);
		this.setName("Imp");
		this.gs = gs;
		this.setLocation(p);
		this.setDescription("A small winged creature with red skin and yellow horns. Flames engulf his needlelike fingers");
		this.setCurrentHealth(35);
		this.setMaxHealth(35);
		this.setAttackDelay(5.0);
		this.readyToAttack(true);
		t = new Thread(this, "Imp");
		target = null;
		lastAttack = "fireball";
		this.setExperienceWorth(300);
		firstTime = true;
		w.addCharacter(this);
		Random rand = new Random();
		if (rand.nextInt(3) > 1) {
			this.inventory.add(ItemFactory
					.makeConsumable("Large Health Potion"));
		} else {
			if (rand.nextInt(3) > 1) {
				this.inventory.add(ItemFactory
						.makeConsumable("Medium Health Potion"));
			}
		}

	}

	/**
	 * check if there is a player in the room, if so start attacking the player.
	 * if the player distance from the Imp is 2 the attack will be fireball, if
	 * the distance is 1 the attack will be a firey claws. sometimes the Imp
	 * will retreat from distance 1.
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
					firstTime = true;
					Iterator<Player> i = w.getArea(getLocation()).getPlayers();
					while (i.hasNext()) {
						target = i.next();
					}
				}
				if (target != null) {
					if (this.distanceFrom(target) == 1) {
						if (this.readyToAttack()) {
							Random rand = new Random();
							int decision = rand.nextInt(5) + 1;
							if (decision <= 3) {
								this.setAttackDelay(3.0);
								lastAttack = "firey claws";
								if (target != null
										&& (target.getLocation().getX() == this
												.getLocation().getX() && target
												.getLocation().getY() == this
												.getLocation().getY()))
									w.attack(this, target);
							} else {
								if (target != null
										&& (target.getLocation().getX() == this
												.getLocation().getX() && target
												.getLocation().getY() == this
												.getLocation().getY()))
									w.retreat(this, target);
							}
						}
					} else if (this.distanceFrom(target) > 1
							&& this.readyToAttack()) {
						lastAttack = "fireball";
						this.setAttackDelay(5.0);
						if (firstTime) {
							Thread.sleep(1500);
							firstTime = false;
						}
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
			// Do nothing the imp is dead
		}
	}

	@Override
	public void attackedBy(Character ch) {

		target = (Player) ch;

	}

	@Override
	public void getGoing() {
		t = new Thread(this, "Imp");
		t.start();
	}

	@Override
	public int getAttackRange() {
		if (lastAttack.equals("fireball")) {
			return 2;
		} else {
			return 1;
		}
	}

	@Override
	public int getDamage() {
		if (lastAttack.equals("fireball")) {
			return 18;
		} else {
			return 12;
		}
	}

	@Override
	public int getDodgeBonus() {
		return 30;
	}

	@Override
	public int getAccuracyBonus() {
		return 5;
	}

	@Override
	public int getDamageReduction() {
		return 0;
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
