package GameModel.mobs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import GameModel.Area;
import GameModel.Character;
import GameModel.Direction;
import GameModel.GameSimulation;
import GameModel.ItemFactory;
import GameModel.Player;
import GameModel.Point;
import GameModel.World;

/**
 * From Zork: "The grue is a sinister, lurking presence in the dark places of
 * the earth. Its favorite diet is adventurers, but its insatiable appetite is
 * tempered by its fear of light. No grue has ever been seen by the light of
 * day, and few have survived its fearsome jaws to tell the tale."
 * 
 * Fast, high chance to hit, average health, high damage. Will attack the player
 * on sight and chase the player if the player moves to another room after the
 * grue has seen them. Will not go into any sunlit room. Rooms 1-6 are all
 * sunlit, and I'll update this with more as I make them.
 * 
 * @author Mazen Shihab
 * 
 *         Rooms: 1 in room 13 1 in room 16
 */
@SuppressWarnings("serial")
public class Grue extends Mob {
	private transient Thread t;
	private Player target;
	private ArrayList<Direction> dirList;
	private GameSimulation gs;
	boolean hasFollowed;

	/**
	 * set the attack delay of this enemy at 3 seconds. set the enemy to ready
	 * to attack. set the Location. the current and max health of the Grue to
	 * 90. set the name and description of the Grue. initiate the thread. and
	 * add the mob to the world. add the items which the Grue carries. set the
	 * experience Points to 400.
	 * 
	 * @param w
	 *            the world of which the mobs are in.
	 * @param p
	 *            the location of the mob.
	 * @param gs
	 *            the GameSimulation which contain the Mob,
	 * 
	 */
	public Grue(World w, Point p, GameSimulation gs) {
		super(w);
		this.setLocation(p);
		this.gs = gs;
		this.setName("Grue");
		this.setDescription("a sinister, lurking presence in the dark places of the earth.");
		this.setCurrentHealth(90);
		this.setMaxHealth(90);
		this.setExperienceWorth(400);
		target = null;
		t = new Thread(this, "Grue");
		dirList = new ArrayList<Direction>();
		this.setAttackDelay(3.0);
		hasFollowed = false;
		w.addCharacter(this);
		this.inventory.add(ItemFactory.makeArmor("Iron Armor"));
		Random rand = new Random();
		if (rand.nextInt(5) == 4) {
			this.inventory.add(ItemFactory.makeWeapon("Claymore"));
		}

	}

	@Override
	public void attackedBy(Character ch) {
		target = (Player) ch;

	}

	/**
	 * check if there is a player in the room, if so start advancing toward the
	 * player once close enough it start to attack the player. if the player
	 * leaves the room the Grue follow him but only in dark rooms if the player
	 * enters a sunlit room the Grue returns to its original location.
	 */
	@Override
	public void run() {
		try {

			while (this.isDead() != true) {
				Thread.sleep(1000);
				tryToAttack();

				// if (hasFollowed)
				// returnHome();
			}
		} catch (InterruptedException e) {

		}
	}

	private void tryToAttack() {
		Iterator<Player> itr = w.getArea(getLocation()).getPlayers();
		while (itr.hasNext() && this.isDead() != true
				|| ((!itr.hasNext()) && target != null)) {
			if (target == null) {
				target = itr.next();

			}

			if (target != null) {

				while (target != null
						&& (target.getLocation().getX() != this.getLocation()
								.getX() || target.getLocation().getY() != this
								.getLocation().getY())) {
					if (this.isDead())
						return;
					// hasFollowed = true;
					follow();
				}

				if (this.distanceFrom(target) <= getAttackRange()) {
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
						// Do nothing, this thread is meant to be interuppted
					}
					if (target != null
							&& (target.getLocation().getX() == this
									.getLocation().getX() && target
									.getLocation().getY() == this.getLocation()
									.getY()))
						w.advance(this, target);
				}
			}
		}
	}

	private void follow() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// e.printStackTrace();

		}

		if (target.getLocation().getX() > this.getLocation().getX()
				&& target.getLocation().getY() == this.getLocation().getY()
				&& this.getLocation().getX() != 16) {
			if (!isSunlit(Direction.EAST)) {
				if (w.getArea(getLocation()).checkExit(Direction.EAST) == 2) {
					dirList.add(Direction.EAST);
					w.moveCharacter(this, Direction.EAST);
				}
			} else
				returnHome();

		} else if (target.getLocation().getX() < this.getLocation().getX()
				&& target.getLocation().getY() == this.getLocation().getY()
				&& this.getLocation().getX() != 14) {
			if (!isSunlit(Direction.WEST)) {
				if (w.getArea(getLocation()).checkExit(Direction.WEST) == 2)

				{
					dirList.add(Direction.WEST);
					w.moveCharacter(this, Direction.WEST);
				}
			} else
				returnHome();

		} else if (target.getLocation().getY() < this.getLocation().getY()
				&& target.getLocation().getX() == this.getLocation().getX()) {
			if (!isSunlit(Direction.SOUTH)) {
				if (w.getArea(getLocation()).checkExit(Direction.SOUTH) == 2) {
					dirList.add(Direction.SOUTH);
					w.moveCharacter(this, Direction.SOUTH);
				}
			} else
				returnHome();

		} else if (target.getLocation().getY() > this.getLocation().getY()
				&& target.getLocation().getX() == this.getLocation().getX()) {
			if (!isSunlit(Direction.NORTH)) {
				if (w.getArea(getLocation()).checkExit(Direction.NORTH) == 2) {
					dirList.add(Direction.NORTH);
					w.moveCharacter(this, Direction.NORTH);
				}
			} else
				returnHome();

		}

	}

	private boolean isSunlit(Direction dr) {

		Area a;
		switch (dr) {
		case EAST:
			a = w.getArea(new Point(this.getLocation().getX() + 1, this
					.getLocation().getY()));
			if (a != null) {
					if (a.isSunny()) {
						target = null;
						return true;
				}
			}

			/*
			 * if (sunlitRooms.contains(new Point(this.getLocation().getX() + 1,
			 * this.getLocation().getY()))) { target = null; return true; }
			 */
			break;
		case WEST:
			a = w.getArea(new Point(this.getLocation().getX() - 1, this
					.getLocation().getY()));
			if (a != null) {
				if (a.isSunny()) {
				target = null;
				return true;
				}
			}
			/*
			 * if (sunlitRooms.contains(new Point(this.getLocation().getX() - 1,
			 * this.getLocation().getY()))) { target = null; return true; }
			 */
			break;
		case NORTH:
			a = w.getArea(new Point(this.getLocation().getX(), this
					.getLocation().getY() + 1));
			if (a != null) {
				if (a.isSunny()) {
				target = null;
				return true;
				}
			}
			/*
			 * if (sunlitRooms.contains(new Point(this.getLocation().getX(),
			 * this .getLocation().getY() + 1))) { target = null; return true; }
			 */

			/*
			 * if (this.getLocation().getY() + 1 == 6) { target = null; return
			 * true; }
			 */
			break;
		case SOUTH:
			a = w.getArea(new Point(this.getLocation().getX(), this
					.getLocation().getY() - 1));
			if (a != null) {
				if (a.isSunny()) {
				target = null;
				return true;
				}
			}
			/*
			 * Point com=new Point(this.getLocation().getX(),
			 * this.getLocation().getY() - 1); if (sunlitRooms.contains(com)) {
			 * target = null; return true; }
			 */
			/*
			 * if (this.getLocation().getY() - 1 == 1) { target = null; return
			 * true; }
			 */
			break;
		}
		return false;

	}

	private void returnHome() {
		while (!dirList.isEmpty()) {

			w.moveCharacter(this, dirList.get(dirList.size() - 1).opposite());

			dirList.remove(dirList.size() - 1);

		}

	}

	@Override
	public void getGoing() {
		t = new Thread(this, "Grue");
		t.start();

	}

	@Override
	public int getAttackRange() {

		return 1;
	}

	@Override
	public int getDamage() {

		return 22;
	}

	@Override
	public int getDodgeBonus() {

		return 5;
	}

	@Override
	public int getAccuracyBonus() {

		return 10;
	}

	@Override
	public int getDamageReduction() {

		return 5;
	}

	@Override
	public String getAttackName() {

		return "slavering fangs";
	}

	@Override
	public void kill() {
		t.interrupt();
		gs.mobDied(this);
	}

}
