package GameModel.mobs;

import java.util.Random;

import GameModel.Character;
import GameModel.Direction;
import GameModel.GameSimulation;
import GameModel.Item;
import GameModel.Notification;
import GameModel.Point;
import GameModel.World;

/**
 * The weakest MOB in the game. Little health, little damage, but often come in
 * groups.
 * 
 * @author Mazen Shihab
 * 
 */
@SuppressWarnings("serial")
public class GiantRat extends Mob {

	private transient Thread t;
	private int secondsUntilNextAction;
	private Character target;
	private int secondsSinceCombat;
	private GameSimulation gs;

	/**
	 * set the attack delay of this enemy at 4 seconds. set the enemy to ready
	 * to attack. set the Location, the current and max health of the Giant Rat
	 * to 20. set the name and description of the GinatRat. initiate the thread.
	 * and add the mob to the world. add the items which the Rat carries. set
	 * the experience to 100.
	 * 
	 * @param w
	 *            the world of which the mobs are in.
	 * @param p
	 *            the location of the mob.
	 * @param gs
	 *            the GameSimulation which contain the Mob,
	 * 
	 */
	public GiantRat(World w, Point p, GameSimulation gs) {
		super(w);

		this.setDescription("Ten times the size of a normal sewer rodent, this vermin looks like he could chew through steel with his stained yellow teeth");
		this.setLocation(p);
		this.setName("Giant Rat");
		this.setCurrentHealth(20);
		this.setMaxHealth(20);
		this.inventory.add(new Item("Rat Tail", 0, "A giant rat tail"));
		this.gs = gs;
		this.setExperienceWorth(100);

		t = new Thread(this, "Giant Rat");

		target = null;
		this.setAttackDelay(4.00);
		this.readyToAttack(true);
		secondsUntilNextAction = 6;
		secondsSinceCombat = 12;

		w.addCharacter(this);
	}

	@Override
	public void attackedBy(Character ch) {
		target = ch;
		secondsSinceCombat = 0;
	}

	/**
	 * add the rat to the world.
	 */
	public void addToWorld() {
		w.addCharacter(this);
	}

	/**
	 * the rats does not attack until they get attack. if they are not in attack
	 * mode they do random movements and random speeches and random movements
	 * after periodic times.
	 */
	@Override
	public void run() {
		try {
			while (!this.isDead()) {
				Thread.sleep(1000);
				if (tryAttack() == false && secondsSinceCombat <= 12) {
					secondsSinceCombat += 1;
				}
				if (secondsSinceCombat >= 12) {
					// If not in combat advance secondsUntilNextAction
					target = null;
					secondsUntilNextAction -= 1;
					if (secondsUntilNextAction <= 0) {
						Random rand = new Random();
						secondsUntilNextAction = rand.nextInt(5) + 8;
						int randomAction = rand.nextInt(10) + 1;
						if (randomAction <= 3) {
							moveMob();
						} else {
							emote();
						}
					}

				}
			}
		} catch (InterruptedException e) {
			// do nothing the rat is dead
		}

	}

	/**
	 * choose a random movement.
	 */
	private void moveMob() {
		boolean hasMoved = false;
		while (!hasMoved) {

			Random r = new Random();
			int numRand = r.nextInt(4) + 1;
			switch (numRand) {
			case 1:
				if (w.getArea(getLocation()).checkExit(Direction.NORTH) == 2) {
					w.moveCharacter(this, Direction.NORTH);
					hasMoved = true;
				}
				break;

			case 2:
				if (w.getArea(getLocation()).checkExit(Direction.EAST) == 2) {
					w.moveCharacter(this, Direction.EAST);
					hasMoved = true;
				}
				break;
			case 3:
				if (w.getArea(getLocation()).checkExit(Direction.SOUTH) == 2) {
					w.moveCharacter(this, Direction.SOUTH);
					hasMoved = true;
				}
				break;
			case 4:
				if (w.getArea(getLocation()).checkExit(Direction.WEST) == 2) {
					w.moveCharacter(this, Direction.WEST);
					hasMoved = true;
				}
				break;
			}
		}
	}

	private boolean tryAttack() {
		// if player in room attack
		boolean attacked = false;
		if (target != null) {
			if (target.getLocation().equalTo(this.getLocation())
					&& target.isDead() == false) {
				if (this.distanceFrom(target) <= this.getAttackRange()
						&& this.readyToAttack()) {
					w.attack(this, target);
					attacked = true;
				} else if (this.distanceFrom(target) > this.getAttackRange()) {
					w.advance(this, target);
				}
			} else {
				if (target.getLocation().equalTo(this.getLocation())
						&& target.isDead()) {
					w.sendNotification(new Notification(
							w.getArea(this.getLocation()).getPlayers(),
							"The rat excitedly sinks it's teeth into the fresh meat",
							false));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// do nothing, this thread is meant to be interrupted
					}
				}
				target = null;
			}
		}

		return attacked;
	}

	/**
	 * choose a random speech and say it in the room.
	 */
	private void emote() {
		Random r = new Random();
		int messageChoice = r.nextInt(4) + 1;
		if (messageChoice == 1) {
			w.sendNotification(new Notification(
					w.getArea(this.getLocation()).getPlayers(),
					"A Giant rat lets out a loud squeak as it searches the area for food",
					false));
		} else if (messageChoice == 2) {
			w.sendNotification(new Notification(w.getArea(this.getLocation())
					.getPlayers(),
					"A Giant rat scratches at a bald patch on it's mangy coat"));
		} else if (messageChoice == 3) {
			w.sendNotification(new Notification(w.getArea(this.getLocation())
					.getPlayers(),
					"A Giant rat coughs up a putrid ball of rotten hair and leftovers"));
		} else if (messageChoice == 4) {
			w.sendNotification(new Notification(w.getArea(this.getLocation())
					.getPlayers(),
					"A Giant rat licks at the oozing boil on it's leg"));
		}
	}

	@Override
	public void getGoing() {

		t = new Thread(this, "Giant Rat");
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
		return 0;
	}

	@Override
	public int getDamageReduction() {

		return 0;
	}

	@Override
	public String getAttackName() {
		return "claws";
	}

	@Override
	public void kill() {
		t.interrupt();
		gs.mobDied(this);
	}

}
