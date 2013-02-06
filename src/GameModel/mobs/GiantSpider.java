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
 * This is an aggressive and powerful mob which will attack and move very
 * quickly. She advances on her target 2 spaces at a time if necessary. When the
 * Giant Spider dies several baby spiders spawn from her.
 * 
 * @author Matthew Latura
 * 
 */
@SuppressWarnings("serial")
public class GiantSpider extends Mob {

	private transient Thread t;
	private Player target;
	private GameSimulation gs;

	/**
	 * set the attack delay of this enemy at 3 second. set the enemy to ready to
	 * attack. set the Location, the current and max health of the giant spider
	 * to 140. set the name and description of the Giant spider. initiate the
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
	public GiantSpider(World w, Point p, GameSimulation gs) {
		super(w);
		this.setName("Giant Spider");
		this.setDescription("8 glowing yellow eyes and a tangle of black legs, blotting out the light.");
		this.setLocation(p);
		this.setCurrentHealth(140);
		this.setMaxHealth(140);
		this.gs = gs;
		this.setAttackDelay(1.5);
		t = new Thread(this, "Giant Spider");
		target = null;
		this.setExperienceWorth(500);
		this.inventory.add(ItemFactory.makeArmor("Ancient Leggings"));
		Random rand = new Random();
		if(rand.nextInt(5) == 4){
			this.inventory.add(ItemFactory.makeWeapon("Battle Axe"));
		}

		w.addCharacter(this);
	}

	/**
	 * check if there is a player in the room, if so start advancing toward the
	 * player once close enough it start to attack the player.
	 */
	@Override
	public void run() {
		try {
			while (!this.isDead()) {
				if (!w.getArea(this.getLocation()).containsCharacter(target)) {
					target = null;
				}
				if (target == null) {
					Iterator<Player> i = w.getArea(this.getLocation())
							.getPlayers();
					while (i.hasNext()) {
						target = i.next();
						Thread.sleep(1000);
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
			// Do nothing, giant spider is dead
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
		t=new Thread(this, "Giant Spider");
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
		return 20;
	}

	@Override
	public int getDodgeBonus() {
		return 10;
	}

	@Override
	public int getAccuracyBonus() {
		return 5;
	}

	@Override
	public int getDamageReduction() {
		return 10;
	}

	@Override
	public String getAttackName() {
		return "fangs";
	}

}
