package GameModel.mobs;

import java.util.Iterator;
import java.util.Random;

import GameModel.Character;
import GameModel.Direction;
import GameModel.GameSimulation;
import GameModel.ItemFactory;
import GameModel.Notification;
import GameModel.Player;
import GameModel.Point;
import GameModel.World;

/**
 * Annoying monster. Usually travels in groups, 1-3 goblins + 0-1 shaman.
 * Slightly more damage and health than a rat, but not much. High chance to hit,
 * low damage resistance.
 * 
 * Rooms: 3 goblins in room 12 3 goblins which move between rooms 9-11
 */
@SuppressWarnings("serial")
public class Goblin extends Mob {

	private transient Thread t;
	private int count;
	private Character target;
	private int countSinceAttacked;
	private GameSimulation gs;

	/**
	 * set the attack delay of this enemy at 3.5 seconds. set the enemy to ready
	 * to attack. set the Location, the current and max health of the Goblin to
	 * 40. set the name and description of the Goblin. initiate the thread. and
	 * add the mob to the world. add the items which the Goblin carries. set the
	 * experience Points to 150.
	 * 
	 * @param w
	 *            the world of which the mobs are in.
	 * @param p
	 *            the location of the mob.
	 * @param gs
	 *            the GameSimulation which contain the Mob,
	 * 
	 */
	public Goblin(World w, Point p, GameSimulation gs) {
		super(w);
		this.gs = gs;
		this.setName("Goblin");
		this.setLocation(p);
		this.setDescription("These small, greedy savages are individually weak, but can be dangerous in groups.");
		this.setExperienceWorth(150);
		t = new Thread(this, "Goblin");
		this.setCurrentHealth(40);
		this.setMaxHealth(40);
		w.addCharacter(this);
		target = null;
		this.setAttackDelay(3.50);
		this.readyToAttack(true);
		countSinceAttacked = 10;
		Random rand = new Random();
		int randomLoot = rand.nextInt(10);
		System.out.println("Random Number: " + randomLoot);
		if (randomLoot == 0) {
			this.inventory.add(ItemFactory.makeArmor("Leather Armor"));
		} else if (randomLoot == 1) {
			this.inventory.add(ItemFactory.makeArmor("Leather Helmet"));
		} else if (randomLoot == 2) {
			this.inventory.add(ItemFactory.makeArmor("Leather Leggings"));

		}
		else if(randomLoot == 3){
			this.inventory.add(ItemFactory.makeWeapon("Dagger"));

		}
	}

	@Override
	public void attackedBy(Character ch) {
		target = ch;
		countSinceAttacked = 0;
	}

	/**
	 * 
	 * check if there is a player or a GiantRat in the room, if so start
	 * advancing toward the player or the rat once close enough it start to
	 * attack the target. goblins which move between rooms 9-11. other goblins
	 * in other rooms don't move.
	 * 
	 */
	@Override
	public void run() {
		try {
			while (!this.isDead()) {
				Thread.sleep(1000);
				if (target == null) {
					Iterator<Player> itr = w.getArea(getLocation())
							.getPlayers();
					while (itr.hasNext() && this.isDead() != true
							&& target == null) {
						target = itr.next();
						if (target instanceof Player) {
							if (target.getCurrentHealth() > 40) {
								w.sendNotification(new Notification(
										(Player) target,
										"A goblin nervously eyes you up and down, hesitantly he begins to advance",
										false));
							} else {
								w.sendNotification(new Notification(
										(Player) target,
										"A goblin sees your open wounds and charges in for the kill",
										false));
							}
							Thread.sleep(1000);
						}
					}

					// If no player enemies were found the goblin will check the
					// room for rats... because goblins hate rats!
					Iterator<Mob> mobIterator = w.getArea(getLocation())
							.getMobs();
					while (mobIterator.hasNext() && target == null) {
						Mob m = mobIterator.next();
						if (m instanceof GiantRat) {
							target = m;
						}
					}

				}
				if (target != null) {
					if (!target.getLocation().equalTo(this.getLocation())
							|| target.isDead()) {
						if (target.getLocation().equalTo(this.getLocation())
								&& target.isDead()) {
							w.sendNotification(new Notification(
									w.getArea(this.getLocation()).getPlayers(),
									"The goblin grins widely and stabs the kill one more time just in case.",
									false));
							Thread.sleep(2000);
						}
						target = null;
					} else if (this.distanceFrom(target) == getAttackRange()) {
						w.attack(this, target);
						countSinceAttacked = 0;
					} else {
						w.advance(this, target);
					}
				}

				if (countSinceAttacked >= 20) {

					count += 3;
					if (count > 10) {
						Random r = new Random();
						int randomNumber = r.nextInt(3) + 1;
						if (randomNumber > 1) {
							moveMob();
							target = null;
							count = 0;
						} else {
							if (r.nextInt(2) + 1 == 2) {
								w.sendNotification(new Notification(w.getArea(
										this.getLocation()).getPlayers(),
										"A goblin scratches its butt.", false));
							} else {
								w.sendNotification(new Notification(w.getArea(
										this.getLocation()).getPlayers(),
										"A goblin trips and smacks itself with its spear."));
							}
							count = 0;
						}
					}
				} else {
					countSinceAttacked += 3;
				}
			}
		} catch (InterruptedException e) {
			// Do nothing, the goblin is dead
		}
	}

	/**
	 * goblins which move between rooms 9-11. other goblins in other rooms don't
	 * move.
	 */
	private void moveMob() {
		int locX = getLocation().getX();
		int locY = getLocation().getY();
		if (locX == 9 && locY == 2) {
			return;
		} else if (locX == 9 && locY == 3) {
			Random rand = new Random();
			int percent = rand.nextInt(99);
			if (percent < 50) {
				return;
			} else {
				w.moveCharacter(this, Direction.EAST);
			}
		} else if (locX == 10 && locY == 3) {
			Random rand = new Random();
			int percent = rand.nextInt(99);
			if (percent < 50) {
				w.moveCharacter(this, Direction.WEST);
			} else {
				w.moveCharacter(this, Direction.EAST);
			}
		} else if (locX == 11 && locY == 3) {
			Random rand = new Random();
			int percent = rand.nextInt(99);
			if (percent < 50) {
				w.moveCharacter(this, Direction.WEST);
			} else {
				return;
			}

		}
	}

	@Override
	public void getGoing() {

		t = new Thread(this, "Goblin");
		t.start();
	}

	@Override
	public int getAttackRange() {
		return 1;
	}

	@Override
	public int getDamage() {

		return 5;
	}

	@Override
	public int getDodgeBonus() {

		return 0;
	}

	@Override
	public int getAccuracyBonus() {

		return 2;
	}

	@Override
	public int getDamageReduction() {

		return 0;
	}

	@Override
	public String getAttackName() {
		return "pointy stick";
	}

	@Override
	public void kill() {
		t.interrupt();
		gs.mobDied(this);

	}

}
