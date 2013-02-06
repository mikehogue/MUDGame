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
 * Fairly brittle, so low health. Does damage comparable with iron sword item.
 * Does not move around much, but attacks any player who enters its room.
 * 
 * @author Mazen Shihab
 * 
 *         Rooms: 2 in room 14 3 in room 18
 */
@SuppressWarnings("serial")
public class SkeletonWarrior extends Mob {

	private transient Thread t;
	private Player target;
	private GameSimulation gs;

	/**
	 * set the attack delay of this enemy at 4 seconds. set the enemy to ready
	 * to attack. set the Location. the current and max health of the SkeletonWarrior to
	 * 40. set the name and description of the SkeletonWarrior. initiate the thread. and
	 * add the mob to the world. add randomly items which the SkeletonWarrior carries. set the
	 * experience Points to 200.
	 * 
	 * @param w
	 *            the world of which the mobs are in.
	 * @param p
	 *            the location of the mob.
	 * @param gs
	 *            the GameSimulation which contain the Mob,
	 * 
	 */
	public SkeletonWarrior(World w, Point p, GameSimulation gs) {
		super(w);
		this.setName("Skeleton Warrior");
		this.gs = gs;
		this.setDescription("A shambling heap of armor scraps with a delicious bony center.");
		this.setLocation(p);
		t = new Thread(this, "Skeleton Warrior");
		target = null;
		this.setExperienceWorth(200);
		this.setCurrentHealth(40);
		this.setMaxHealth(40);
		this.setAttackDelay(4.00);
		this.readyToAttack(true);
		Random rand = new Random();
		this.inventory.add(ItemFactory.makeWeapon("Iron Sword"));
		int randomLoot = rand.nextInt(10);
		if(randomLoot == 0){
			this.inventory.add(ItemFactory.makeArmor("Iron Helmet"));
		}
		else if(randomLoot == 1){
			this.inventory.add(ItemFactory.makeArmor("Iron Armor"));
		}
		else if(randomLoot == 2){
			this.inventory.add(ItemFactory.makeArmor("Iron Leggings"));
		}
		else if(randomLoot == 3){
			this.inventory.add(ItemFactory.makeArmor("Iron Shield"));
		}
		w.addCharacter(this);

	}

	@Override
	public void attackedBy(Character ch) {
		target = (Player) ch;
	}

	/**
	 * check if there is a player in the room, if so start advancing toward the
	 * player once close enough, it start to attack the player. does not leave the room.
	 */
	@Override
	public void run() {
		try {

			while (!this.isDead()) {
				Thread.sleep(1000);
				tryToAttack();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}

	}

	private void tryToAttack() {
		Iterator<Player> itr = w.getArea(getLocation()).getPlayers();
		while (itr.hasNext() && this.isDead() != true) {
			if (target == null) {
				target = itr.next();

			}

			if (target != null) {

				if (target != null
						&& (target.getLocation().getX() != this.getLocation()
								.getX() || target.getLocation().getY() != this
								.getLocation().getY())) {
					target = null;
					return;
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
						
					}
					if ((target.getLocation().getX() != this.getLocation()
							.getX() || target.getLocation().getY() != this
							.getLocation().getY())) {
						target = null;
						return;
					} else if (target != null
							&& (target.getLocation().getX() == this
									.getLocation().getX() && target
									.getLocation().getY() == this.getLocation()
									.getY()))
						w.advance(this, target);
				}
			}
		}
	}

	@Override
	public void getGoing() {
		t = new Thread(this, "Skeleton Warrior");
		t.start();
	}

	@Override
	public int getAttackRange() {

		return 1;
	}

	@Override
	public int getDamage() {

		return 10;
	}

	@Override
	public int getDodgeBonus() {

		return 3;
	}

	@Override
	public int getAccuracyBonus() {

		return 7;
	}

	@Override
	public int getDamageReduction() {

		return 2;
	}

	@Override
	public String getAttackName() {

		return "sword";
	}

	@Override
	public void kill() {
		t.interrupt();
		gs.mobDied(this);
	}

}
