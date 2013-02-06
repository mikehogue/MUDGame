package GameModel.mobs;

import java.util.Iterator;
import java.util.Random;

import GameModel.Character;
import GameModel.Direction;
import GameModel.GameSimulation;
import GameModel.Notification;
import GameModel.Player;
import GameModel.Point;
import GameModel.SmallHealthPot;
import GameModel.World;

/**
 * Same damage resistance as goblin, slightly less health. Does the same regular
 * attack damage, but can heal a damaged mob in the room instead of attacking
 * (with a cooldown). Possibly other spells depending on what our combat system
 * looks like.
 * 
 * Rooms: 2 goblin shamans in room 12 1 goblin shaman which patrols from rooms
 * 9-11
 */
@SuppressWarnings("serial")
public class GoblinShaman extends Mob {
	private transient Thread t;
	private Player target;
	private int countSinceAttacked;
	private int count;
	private GameSimulation gs;

	/**
	 * set the attack delay of this enemy at 8 seconds. set the enemy to ready
	 * to attack. set the Location. the current and max health of the GoblinShaman to
	 * 35. set the name and description of the GoblinShaman. initiate the thread. and
	 * add the mob to the world. add the items which the GoblinShaman carries. set the
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
	public GoblinShaman(World w, Point p, GameSimulation gs) {
		super(w);
		this.gs = gs;
		this.setName("Goblin Shaman");
		this.setLocation(p);
		t = new Thread(this, "Goblin Shaman");
		this.setDescription("These small, greedy savages can be dangerous in groups, Heal other Goblins");
		this.setCurrentHealth(35);
		this.setMaxHealth(35);
		this.setAttackDelay(8.00);
		this.readyToAttack(true);
		this.setExperienceWorth(200);
		w.addCharacter(this);
		target = null;
		countSinceAttacked = 10;
		Random rand = new Random();
		if(rand.nextInt(5) > 2){
			this.inventory.add(new SmallHealthPot());
		}
		

	}

	@Override
	public void attackedBy(Character ch) {
		target = (Player) ch;
		countSinceAttacked = 0;

	}

	/**
	 * 
	 * check if there is a player  in the room. if so it might choose between to start
	 * advancing toward the player or start to heal other mobs. when advanced and close enough it start to
	 * attack the target. goblinShamans which move between rooms 9-11. other Shamans
	 * in other rooms don't move.
	 * 
	 */
	@Override
	public void run() {
		try {

			while (!this.isDead()) {
				Thread.sleep(3000);
				
				Iterator<Player> itr = w.getArea(getLocation()).getPlayers();
				while (itr.hasNext() && this.isDead() != true) {
					if (target == null)
						target = itr.next();

					if (target != null) {

						if (target.getLocation().getX() != this.getLocation()
								.getX()
								|| target.getLocation().getY() != this
										.getLocation().getY())
							target = null;
						else if (this.distanceFrom(target) <= getAttackRange()) {
							if (target != null
									&& (target.getLocation().getX() == this
											.getLocation().getX() && target
											.getLocation().getY() == this
											.getLocation().getY())) {
								w.attack(this, target);
								countSinceAttacked = 0;
							}
						} else {
							Random r = new Random();
							int randomNumber = r.nextInt(6) + 1;
							if (randomNumber == 1 || randomNumber == 3) {
								w.sendNotification(new Notification(w.getArea(
										this.getLocation()).getPlayers(),
										"A shaman says: \"Stinking human! Kill it!\"", false));
								Iterator<Mob> z = w.getArea(getLocation())
										.getMobs();
								while (z.hasNext()) {
									z.next().takeDamage(-5);

								}
							} else if (randomNumber == 2 || randomNumber == 4) {
								w.sendNotification(new Notification(w.getArea(
										this.getLocation()).getPlayers(),
										"A shaman says: \"Come gobs! Come! I has healings!\""));
								Iterator<Mob> z = w.getArea(getLocation())
										.getMobs();
								while (z.hasNext()) {
									z.next().takeDamage(-4);

								}

							} else if (randomNumber == 6)
								Thread.sleep(3000);
							if (target != null
									&& (target.getLocation().getX() == this
											.getLocation().getX() && target
											.getLocation().getY() == this
											.getLocation().getY()))
								w.advance(this, target);
						}
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
										"A shaman tries to eat his staff. He fails.", false));
								Iterator<Mob> z = w.getArea(getLocation())
										.getMobs();
								while (z.hasNext()) {
									z.next().takeDamage(-5);

								}
							} else {
								w.sendNotification(new Notification(w.getArea(
										this.getLocation()).getPlayers(),
										"A shaman eats a mushroom, and starts giggling uncontrollably."));
								Iterator<Mob> z = w.getArea(getLocation())
										.getMobs();
								while (z.hasNext()) {
									z.next().takeDamage(-4);

								}
							}
							count = 0;
						}
					}
				} else {
					countSinceAttacked += 3;
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			//System.out.println("end of life");
		}
	}

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
		t = new Thread(this, "Goblin Shaman");
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
		return "staff";
	}

	@Override
	public void kill() {
		t.interrupt();
		gs.mobDied(this);
	}

}
