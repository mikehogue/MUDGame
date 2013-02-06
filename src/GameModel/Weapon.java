package GameModel;

/**
 * Weapon is a subclass of Equipable and Item. Weapons grant bonuses to damage for the Player when equipped
 * and also modify his attack delay and range. They can require two hands to hold or just one
 * delay
 * @author Matthew Latura
 *
 */
@SuppressWarnings("serial")
public class Weapon extends Equipable{
	private int weaponDamage;
	private double weaponSpeed;
	private boolean twoHanded;
	private int attackRange;

	/**
	 * Simple constructor for weapons. weaponDamage will be set to a default of 5, weaponSpeed to 3.0, and twoHanded to false
	 * @param name - weapon's name
	 * @param weight - weapon's weight
	 * @param description - weapon's description
	 */
	public Weapon(String name, int weight, String description) {
		super(name, weight, description);
		weaponDamage = 5;
		weaponSpeed = 3.0;
		twoHanded = false;
		attackRange = 1;
	}
	
	/**
	 * This weapon constructor allows the creator to supply not only name, weight, and description but also the damage and 
	 * weapon speed for the weapon. twoHanded is defaulted to false 
	 * @param name - the name of this weapon
	 * @param weight - the weight of this weapon
	 * @param description - weapon's description
	 * @param damage - weapon's base damage
	 * @param speed - weapon's attack delay in seconds
	 */
	public Weapon(String name, int weight, String description, int damage, double speed){
		this(name, weight, description);
		weaponDamage = damage;
		weaponSpeed = speed;
		attackRange = 1;
	}
	
	/**This constructor allows the creator to set the twoHanded property of the weapon in addition to it's name,
	 * weight, description, damage, and speed	 * 
	 * @param name - the name of this weapon
	 * @param weight - the weight of this weapon
	 * @param description - the description of this weapon
	 * @param damage  - the base damage of this weapon
	 * @param speed - the weapon delay in seconds of this weapon
	 * @param twoHand - whether this weapon requires two hands to equip
	 */
	public Weapon(String name, int weight, String description, int damage, double speed, boolean twoHand){
		this(name, weight, description, damage, speed);
		twoHanded = twoHand;
	}
	
	/**
	 * This constructor allows the creator to set the attack range of the weapon in addition to it's name, weight
	 * description, damage, speed, twoHand, and attackRange fields
	  @param name - the name of this weapon
	 * @param weight - the weight of this weapon
	 * @param description - the description of this weapon
	 * @param damage  - the base damage of this weapon
	 * @param speed - the weapon delay in seconds of this weapon
	 * @param twoHand - whether this weapon requires two hands to equip
	 * @param attackRange - The range at which this weapon can attack from
	 */
	public Weapon(String name, int weight, String description, int damage, double speed, boolean twoHand, int attackRange){
		this(name, weight, description, damage, speed, twoHand);
		this.attackRange = attackRange;
	}
	
	/**
	 * @return the weapon's damage
	 */
	public int getDamage(){
		return weaponDamage;
	}
	
	/**
	 * @return the weapon's attack delay in seconds
	 */
	public double getSpeed(){
		return weaponSpeed;
	}
	
	/**
	 * Sets the weapon's twoHanded property to the passed boolean value
	 * @param b - the value to assign to the twoHanded property
	 */
	public void setTwoHanded(boolean b){
		twoHanded = b;
	}
	
	/**
	 * @return whether the weapon requires two hands to equip
	 */
	public boolean isTwoHanded(){
		return twoHanded;
	}
	
	/**
	 * @return what range the weapon can attack from
	 */
	public int getAttackRange(){
		return attackRange;
	}
}
