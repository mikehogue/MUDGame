package GameModel.mobs;

import java.util.Iterator;

import GameModel.Character;
import GameModel.GameSimulation;
import GameModel.Player;
import GameModel.Point;
import GameModel.World;

/**
 * This is a smaller version of the Giant Spider, just as aggressive but
 * slightly slower and less powerful It advances on it's target 2 spaces at a
 * time if necessary. The baby spider does not spawn more spiders upon death
 * 
 * @author Matthew Latura
 * 
 */
@SuppressWarnings("serial")
public class BabySpider extends Mob {

	private transient Thread t;
	private Player target;
	private GameSimulation gs;

	/**
	 * set the attack delay of this enemy at 3 second. set the enemy to ready to
	 * attack. set the Location, the current and max health of the baby spider
	 * to 25. set the name and description of the Baby spider. initiate the
	 * thread. and add the mob to the world.
	 * 
	 * @param w
	 *            the world of which the mobs are in.
	 * @param p
	 *            the location of the mob.
	 * @param gs
	 *            the GameSimulation which contain the Mob,
	 * 
	 */
	public BabySpider(World w, Point p, GameSimulation gs) {
		super(w);
		this.setName("Baby Spider");
		this.setDescription("Just like it's mother but creepier and crawlier!");
		this.setLocation(p);
		this.setCurrentHealth(25);
		this.setMaxHealth(25);
		this.gs = gs;
		this.setAttackDelay(3.0);
		t = new Thread(this, "Baby Spider");
		target = null;

		w.addCharacter(this);
	}

	/**
	 * check if there is a player in the room, if so start advancing toward the
	 * player once close enough it start to attack the player.
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
			while (!this.isDead()) {
				if (!w.getArea(this.getLocation()).containsCharacter(target)) {
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
					int distanceFromTarget = this.distanceFrom(target);
					if (distanceFromTarget == 1) {

					} else if (distanceFromTarget == 2
							|| distanceFromTarget == 3) {
						if (target != null
								&& (target.getLocation().getX() == this
										.getLocation().getX() && target
										.getLocation().getY() == this
										.getLocation().getY()))
							w.advance(this, target);
						distanceFromTarget = this.distanceFrom(target);
						if (distanceFromTarget == 2 || distanceFromTarget == 3) {
							if (target != null
									&& (target.getLocation().getX() == this
											.getLocation().getX() && target
											.getLocation().getY() == this
											.getLocation().getY()))
								w.advance(this, target);
						}
					}
					if (target != null
							&& (target.getLocation().getX() == this
									.getLocation().getX() && target
									.getLocation().getY() == this.getLocation()
									.getY()))
						tryAttack();
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			// Do nothing, baby spider is dead
		}
	}

	private void tryAttack() {
		if (target != null && this.readyToAttack()) {
			w.attack(this, target);
		}
	}

	@Override
	public void attackedBy(Character ch) {
		if (target == null && ch instanceof Player) {
			target = (Player) ch;
		}
	}

	@Override
	public void getGoing() {
		t.start();

	}

	@Override
	public void kill() {
		t.interrupt();
		gs.mobDied(this);
	}

	@Override
	public int getAttackRange() {
		return 1;
	}

	@Override
	public int getDamage() {
		return 7;
	}

	@Override
	public int getDodgeBonus() {
		return 15;
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
		return "fangs";
	}

}
