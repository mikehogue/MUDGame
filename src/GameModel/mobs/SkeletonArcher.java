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
 * Works the same as skeleton warrior, but damage equal to longbow instead of
 * iron sword.
 * 
 * @author Mazen Shihab
 * 
 * Rooms:
 * 1 in room 14
 * 2 in room 18
 */
@SuppressWarnings("serial")
public class SkeletonArcher extends Mob {

	private transient Thread t;
	private Player target;
	private String lastAttack;
	private GameSimulation gs;
	boolean firstTime;
	
	/**
	 * set the attack delay of this enemy at 11 seconds. set the enemy to ready
	 * to attack. set the Location, the current and max health of the SkeletonArcher to 40.
	 * set the name and description of the SkeletonArcher. initiate the thread. and add
	 * the mob to the world. set the default attack to longbow. add the items
	 * which the SkeletonArcher carries. set the experience to 200.
	 * 
	 * @param w
	 *            the world of which the mobs are in.
	 * @param p
	 *            the location of the mob.
	 * @param gs
	 *            the GameSimulation which contain the Mob,
	 * 
	 */
	public SkeletonArcher(World w,Point p,GameSimulation gs) {
		super(w);
		this.setName("Skeleton Archer");
		this.gs=gs;
		this.setLocation(p);
		this.setDescription("It's uncertain how it can pull back that bow with no muscles.");
		t = new Thread(this, "Skeleton Archer");
		target = null;
		this.setExperienceWorth(200);
		this.setCurrentHealth(40);
		this.setMaxHealth(40);
		this.setAttackDelay(11.00);
		this.readyToAttack(true);
		lastAttack="longbow";
		firstTime=true;
		this.inventory.add(ItemFactory.makeWeapon("Longbow"));
		Random rand = new Random();
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
		w.addCharacter(this);
		
		
	}

	@Override
	public void attackedBy(Character ch) {
		target = (Player) ch;
	}

	/**
	 * check if there is a player in the room, if so start attacking the player.
	 * if the player distance from the SkeletonArcher is 3 the attack will be longbow, if
	 * the distance is 1 the attack will be a hand. does not leave the room.
	 */
	@Override
	public void run() {
		
		try {
			while (!this.isDead()){

	Thread.sleep(1000);
				
				if(target!=null)
					if(!w.getArea(this.getLocation()).containsCharacter(target)){
						target = null;
				}
				if(target == null){
					firstTime=true;
					Iterator<Player> i = w.getArea(getLocation()).getPlayers();
					while(i.hasNext()){
						target = i.next();
					}
				}
				if(target != null){
					if(this.distanceFrom(target) == 1){
						if(this.readyToAttack()){
							Random rand = new Random();
							int decision = rand.nextInt(5)+1;
							if(decision <= 3){
								this.setAttackDelay(5.0);
								lastAttack = "hand";
						
								if (target != null
										&& (target.getLocation().getX() == this
												.getLocation().getX() && target
												.getLocation().getY() == this
												.getLocation().getY()))
								w.attack(this, target);
							}
							else{
								if (target != null
										&& (target.getLocation().getX() == this
												.getLocation().getX() && target
												.getLocation().getY() == this
												.getLocation().getY()))
								w.retreat(this, target);
							}
						}						
					}
					else if(this.distanceFrom(target) > 1 && this.readyToAttack()){
						lastAttack = "longbow";
						this.setAttackDelay(11.0);
						if(firstTime){
							Thread.sleep(2500);
							firstTime=false;
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
			
		}
	}

	@Override
	public void getGoing() {
		t = new Thread(this, "Skeleton Archer");
		t.start();
		
	}

	@Override
	public int getAttackRange() {
		if(lastAttack.equals("longbow")){
			return 3;
		}
		else{
			return 1;
		}
	}

	@Override
	public int getDamage() {
		if(lastAttack.equals("longbow")){
			return 11;
		}
		else{
			return 5;
		}
	}

	@Override
	public int getDodgeBonus() {
		
		return 3;
	}

	@Override
	public int getAccuracyBonus() {
		if(lastAttack.equals("longbow")){
			return 7;
		}
		else{
			return 8;
		}
	}

	@Override
	public int getDamageReduction() {
		
		return 2;
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
