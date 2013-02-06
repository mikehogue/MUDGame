package GameModel.mobs;

import java.util.ArrayList;
import java.util.HashMap;

import GameModel.Character;
import GameModel.Item;
import GameModel.World;

/**
 * Mob is Character and implement runnable. Mob is father abstract class for all
 * the mobs in the game. contain the world of which the mobs are in. and store
 * default experience points for the mobs.
 * 
 * @author Mazen Shihab
 * 
 */
@SuppressWarnings("serial")
public abstract class Mob extends Character implements Runnable {
	World w;
	int experienceWorth;

	/**
	 * set a default experience points for all the mobs at 300. set the enemy to
	 * ready to attack.
	 * 
	 * @param w
	 *            set the World for the Mobs.
	 */
	public Mob(World w) {
		this.w = w;
		this.inventory = new ArrayList<Item>();
		this.neighborDistances = new HashMap<Character, Integer>();
		this.readyToAttack(true);
		this.setAttackDelay(3.0);
		experienceWorth = 300;
	}

	/**
	 * abstract method the determine the complex behavior of each of the enemies
	 * 
	 * @param ch
	 *            set the target for the mobs.
	 * 
	 */
	public abstract void attackedBy(Character ch);

	/**
	 * start the thread for the mobs.
	 */
	public abstract void getGoing();

	/**
	 * kill the the thread of the mobs and send a message to GameSimulation to
	 * remove the mob.
	 */
	public abstract void kill();

	@Override
	public boolean isDead() {
		return this.getCurrentHealth() == 0;

	}

	/**
	 * Getter for the experience worth of the MOB.
	 * 
	 * @return The amount of experience the MOB grants the killer on death.
	 */
	public int getExperience() {

		return experienceWorth;
	}

	/**
	 * Setter for the experience worth of the MOB.
	 * 
	 * @param i
	 *            The amount of experience the MOB grants the killer on death.
	 */
	public void setExperienceWorth(int i) {

		experienceWorth = i;
	}

}
