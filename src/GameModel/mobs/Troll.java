package GameModel.mobs;

import java.util.Iterator;

import GameModel.Character;
import GameModel.Direction;
import GameModel.GameSimulation;
import GameModel.Item;
import GameModel.ItemFactory;
import GameModel.Player;
import GameModel.Point;
import GameModel.World;

/**
 * 
 * Massive, slow-moving, stupid brute with a club. Low chance to hit, but
 * second-highest damage in the game, after dragon. Hard to kill, because of
 * buckets of health. Trolls usually regenerate, modeled by recovering small
 * amounts of health over time. I'm thinking only 1-2 of these in the game.
 * 
 * Image: http://gorrem.deviantart.com/art/LOTRO-Cave-Troll-98170046. Note that
 * the little sticks tied to its club are full-sized spears, for size reference.
 * 
 * @author Mazen Shihab
 * 
 *         Rooms: Moves clockwise in a circle around rooms 27-34
 */
@SuppressWarnings("serial")
public class Troll extends Mob {

	private transient Thread t;
	private int selfHealing;
	private Player target;
	private int roomsTraveledSinceAttacked;
	private int secondsUntilMoving;
	private GameSimulation gs;
	private boolean successfulFollow;

	/**
	 * set the attack delay of this enemy at 9 seconds. set the enemy to ready
	 * to attack. set the Location. the current and max health of the Troll to
	 * 90. set the name and description of the Troll. initiate the thread. and
	 * add the mob to the world. add the items which the Troll carries. set the
	 * experience Points to 650.
	 * 
	 * @param w
	 *            the world of which the mobs are in.
	 * @param p
	 *            the location of the mob.
	 * @param gs
	 *            the GameSimulation which contain the Mob,
	 * 
	 */
	public Troll(World w, Point p, GameSimulation gs) {
		super(w);
		selfHealing = 0;
		this.gs = gs;
		this.setName("Troll");
		this.setLocation(p);
		this.setDescription("Hideous creatures with four fingers and four toes and a tail resembling that of a cow. The trolls live hundreds of years.");
		this.setCurrentHealth(180);
		this.setMaxHealth(180);
		target = null;
		t = new Thread(this, "Troll");
		this.setAttackDelay(9.0);
		this.readyToAttack(true);
		roomsTraveledSinceAttacked = 3;
		secondsUntilMoving = 0;
		successfulFollow = false;
		w.addCharacter(this);
		this.setExperienceWorth(650);
		this.inventory.add(ItemFactory.makeArmor("Ancient Armor"));
		this.inventory.add(ItemFactory.makeWeapon("Troll Club"));
		this.inventory
				.add(new Item("Lever Key", 2,
						"A large, crude key made from iron. It must fit a pretty big lock."));
	}

	@Override
	public void attackedBy(Character ch) {
		target = (Player) ch;
		roomsTraveledSinceAttacked = 0;
	}

	/**
	 * 
	 * check if there is a player in the room, if so start advancing toward the
	 * player once close enough it start to attack the player. if the player
	 * leaves the room the Troll follow him but only up to three rooms that the
	 * troll does not see the players in them. if the player leaves rooms 27-34
	 * the Troll will not follow. when not in a fight the Troll heal itself
	 * periodically.
	 * 
	 */
	@Override
	public void run() {
		try {
			while (!this.isDead()) {

				Thread.sleep(1000);
				selfHealing += 1;
				if (selfHealing > 9) {
					this.takeDamage(-5);
					selfHealing = 0;
				}
				if (this.isDead())
					return;
				tryToAttack();
				if (roomsTraveledSinceAttacked > 2 && secondsUntilMoving > 8) {

					Iterator<Player> itr = w.getArea(getLocation())
							.getPlayers();
					if (!itr.hasNext()) {
						moveMob();
						secondsUntilMoving = 0;
					}
				}
				secondsUntilMoving += 2;
			}
		} catch (InterruptedException e) {
			// e.printStackTrace();
			System.out.println("end of life");
		}

	}

	private void tryToAttack() {
		Iterator<Player> itr = w.getArea(getLocation()).getPlayers();
		while (itr.hasNext() && this.isDead() != true
				|| ((!itr.hasNext()) && target != null)) {
			if (target == null) {
				target = itr.next();
				roomsTraveledSinceAttacked = 0;
			}

			if (target != null) {
				while (target.getLocation().getX() != this.getLocation().getX()
						|| target.getLocation().getY() != this.getLocation()
								.getY() && (roomsTraveledSinceAttacked < 3)) {
					if (this.isDead())
						return;
					follow();
					if (!successfulFollow) {
						target = null;
						return;
					}
				}
				if (roomsTraveledSinceAttacked > 2) {
					if (target != null
							&& (target.getLocation().getX() == this
									.getLocation().getX() && target
									.getLocation().getY() == this.getLocation()
									.getY())) {
						roomsTraveledSinceAttacked--;
					} else {
						target = null;
						return;
					}
				} else if (this.distanceFrom(target) <= getAttackRange()) {
					if (target != null
							&& (target.getLocation().getX() == this
									.getLocation().getX() && target
									.getLocation().getY() == this.getLocation()
									.getY()))
						w.attack(this, target);
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

						System.out.println("end of life");

					}
					if (this.isDead())
						return;
					if (target != null
							&& (target.getLocation().getX() == this
									.getLocation().getX() && target
									.getLocation().getY() == this.getLocation()
									.getY()))
						w.advance(this, target);
				}
			}
		}
		roomsTraveledSinceAttacked = 3;
	}

	private void follow() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// e.printStackTrace();

		}
		if (roomsTraveledSinceAttacked < 3) {
			if (target.getLocation().getX() > this.getLocation().getX()
					&& target.getLocation().getY() == this.getLocation().getY()
					&& this.getLocation().getX() != 16) {
				roomsTraveledSinceAttacked++;
				w.moveCharacter(this, Direction.EAST);
				successfulFollow = true;

			} else if (target.getLocation().getX() < this.getLocation().getX()
					&& target.getLocation().getY() == this.getLocation().getY()
					&& this.getLocation().getX() != 14) {
				roomsTraveledSinceAttacked++;
				w.moveCharacter(this, Direction.WEST);
				successfulFollow = true;
			} else if (target.getLocation().getY() < this.getLocation().getY()
					&& target.getLocation().getX() == this.getLocation().getX()) {
				roomsTraveledSinceAttacked++;
				w.moveCharacter(this, Direction.SOUTH);
				successfulFollow = true;
			} else if (target.getLocation().getY() > this.getLocation().getY()
					&& target.getLocation().getX() == this.getLocation().getX()) {
				roomsTraveledSinceAttacked++;
				w.moveCharacter(this, Direction.NORTH);
				successfulFollow = true;

			} else
				successfulFollow = false;
		}

	}

	/**
	 * Moves clockwise in a circle around rooms 27-34
	 */

	private void moveMob() {
		if (this.getLocation().getX() == 14
				&& (this.getLocation().getY() == 7 || this.getLocation().getY() == 8)) {
			w.moveCharacter(this, Direction.NORTH);
		} else if ((this.getLocation().getX() == 14 || this.getLocation()
				.getX() == 15) && this.getLocation().getY() == 9) {
			w.moveCharacter(this, Direction.EAST);
		} else if ((this.getLocation().getX() == 16 || this.getLocation()
				.getX() == 15) && this.getLocation().getY() == 7) {
			w.moveCharacter(this, Direction.WEST);
		} else if (this.getLocation().getX() == 16
				&& (this.getLocation().getY() == 8 || this.getLocation().getY() == 9)) {
			w.moveCharacter(this, Direction.SOUTH);
		}

	}

	@Override
	public void getGoing() {
		t = new Thread(this, "Troll");
		t.start();

	}

	@Override
	public int getAttackRange() {

		return 1;
	}

	@Override
	public int getDamage() {

		return 30;
	}

	@Override
	public int getDodgeBonus() {

		return 2;
	}

	@Override
	public int getAccuracyBonus() {

		return 0;
	}

	@Override
	public int getDamageReduction() {

		return 10;
	}

	@Override
	public String getAttackName() {

		return "club";
	}

	@Override
	public void kill() {

		t.interrupt();
		gs.mobDied(this);
	}

}
