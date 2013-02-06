package GameModel;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Player is a subclass of Character, it is the Character type that is controllable by the user during his time in the 
 * Mud Game.
 * @author Matthew Latura
 */
public class Player extends Character {
	
	private static final long serialVersionUID = 1L;
	private Equipable head;
	private Equipable hand1;
	private Equipable hand2;
	private Equipable torso;
	private Equipable legs;
	private String password;
	private int experiencePoints;
	private int statIncreases;
	
	/**
	 * Default constructor takes a String parameter to set the Player's name and a second String parameter is used to set the 
	 * Player's password. Sets most of Players fields to their default starting values.
	 * @param name - the new Player's name
	 * @param pass - the new Player's password
	 */
	public Player(String name, String pass) {
		inventory = new ArrayList<Item>();
		this.setName(name);
		this.password = pass;
		this.setLocation(new Point(10, 0));
		this.setCurrentHealth(150);
		this.setMaxHealth(150);
		neighborDistances = new HashMap<Character, Integer>();
		this.setAttackDelay(1.0);
		this.readyToAttack(true);
		experiencePoints = 0;
		statIncreases = 0;
		
		this.strength = 5;
		this.dexterity = 5;
		this.armor = 0;
		this.precision = 5;
	}
	
	
	/**
	 * Returns a string containing a summary of the Player's inventory and his held items
	 * @return a string containing a summary of the Player's inventory and his held items
	 */
	public String printInventory(){
		String inv = "Equipped Items..." +
		
		"\nHead: ";
		if(head != null){	inv += head.getName();	}
		else{	inv += "None";	}
		
		inv += "\nTorso: ";
		if(torso != null){	inv += torso.getName(); }
		else{	inv += "None";	}
		
		inv += "\nLegs: ";
		if(legs != null){	inv += legs.getName(); }
		else{	inv += "None"; 	}
		
		inv += "\nMain Hand: ";
		if(hand1 != null){	inv += hand1.getName(); }
		else{	inv += "None"; 	}
		
		inv += "\nOff Hand: ";
		if(hand2 != null){ inv += hand2.getName(); }
		else{
			if(hand1 instanceof Weapon){
				if(((Weapon)hand1).isTwoHanded()){
					inv += hand1.getName();
				}
				else{
					inv += "None";
				}
			}
			else{
				inv += "None";
			}
		}
				
		if(inventory.isEmpty()){
			inv += "\n\nYour inventory is empty";
		}
		else{
			inv += "\n\nInventory: ";
			inv += inventory.get(0).getName();
			for(int i = 1; i < inventory.size(); i++){
				inv += ", " + inventory.get(i).getName();
			}
		}		
		return inv;
	}
	
	/**
	 * Returns the Player's password
	 * @return the Player's password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * This method takes a String as the name of an item. It will check if the player has the item and equip it if
	 * it is not already equipped. If the equip slot of the item is already used than it will deequip that item and equip
	 * the new one
	 * @param itemName - the name of the item to attempt to equip
	 * @return Notification containing a Note telling the player the result of his equip request
	 */
	public Notification equipItem(String itemName){
		Notification returnNote = new Notification(this, false);
		
		//Default message
		String message = "You don't appear to have this item in your inventory";
		
		//Checks the Player's inventory for the item
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i).getName().toLowerCase().equals(itemName.toLowerCase())){
				Item item = inventory.get(i);
				
				//Checks to make sure the item is equipable
				if(item instanceof Equipable){
					Equipable equipableItem = (Equipable)item;
					
					//Checks to make sure the item is not already equipped
					if(equipableItem.isEquipped() != true){
						EquipSlot equipSlot = equipableItem.getEquipSlot();
						if(equipSlot == EquipSlot.HAND){
							
							//Equips weapons to Hand1, Hand 2 is also used if the weapon is 2 handed
							if(equipableItem instanceof Weapon){
								Weapon weapon = (Weapon)equipableItem;
								if(weapon.isTwoHanded()){
									unequipHand1();
									unequipHand2();
									hand1 = weapon;
									this.setAttackDelay(weapon.getSpeed());
									inventory.remove(hand1);
									hand1.setEquipped(true);
									attack();
									
								}
								else{
									unequipHand1();
									hand1 = weapon;
									this.setAttackDelay(weapon.getSpeed());
									inventory.remove(hand1);
									hand1.setEquipped(true);
									attack();
								}
								message = "You draw your " + weapon.getName() + " into your hand";
							}
							
							//Equips Armor(Shields) to Hand2 if the player is not holding a 2 handed weapon
							else if(equipableItem instanceof Armor){
								Armor armor = (Armor)equipableItem;
								if(hand1 instanceof Weapon){
									if(((Weapon) hand1).isTwoHanded()){
										message = "You are wielding a two-handed weapon and do not have a free hand to equip " + armor.getName();
									}
									else{
										unequipHand2();
										hand2 = armor;
										inventory.remove(hand2);
										hand2.setEquipped(true);
										this.armor += armor.getAC();
										message = "You draw your " + armor.getName() + " into your off hand";
									}
								}
								else{
									unequipHand2();
									hand2 = armor;
									inventory.remove(hand2);
									hand2.setEquipped(true);
									this.armor += armor.getAC();
									message = "You draw your " + armor.getName() + " into your off hand";
								}
							}
						}
						//Equips Helmets
						else if(equipSlot == EquipSlot.HEAD){
							unequipHead();
							head = equipableItem;
							if(equipableItem instanceof Armor){
								armor += ((Armor) equipableItem).getAC();
							}
							inventory.remove(head);
							head.setEquipped(true);
							message = "You place your " + head.getName() + " atop your head";
						}
						//Equips Leg Armors
						else if(equipSlot == EquipSlot.LEGS){
							unequipLegs();
							legs = equipableItem;
							if(equipableItem instanceof Armor){
								armor += ((Armor) equipableItem).getAC();
							}
							inventory.remove(legs);
							legs.setEquipped(true);
							message = "You affix your " + legs.getName() + " to your legs";
						}
						//Equips Breastplates
						else if(equipSlot == EquipSlot.TORSO){
							unequipTorso();
							torso = equipableItem;
							if(equipableItem instanceof Armor){
								armor += ((Armor) equipableItem).getAC();
							}
							inventory.remove(torso);
							torso.setEquipped(true);
							message = "You affix your " + torso.getName() + " to your chest";
						}
					}
					else{
						message = "This item is already equipped";
					}
				}
				else{
					message = "You can't equip this item";
				}
			}
		}
		returnNote.setString(message);
		
		//Returns the Notification result to be sent to the Player
		return returnNote;
	}
	
	private void unequipHand1(){
		if(hand1 != null){
			hand1.setEquipped(false);
			inventory.add(hand1);
			hand1 = null;
			this.setAttackDelay(1.0);
			this.readyToAttack(true);
		}		
	}
	
	private void unequipHand2(){
		if(hand2 != null){
			hand2.setEquipped(false);
			if(hand2 instanceof Armor){
				armor -= ((Armor) hand2).getAC();
			}
			inventory.add(hand2);
			hand2 = null;
		}		
	}
	
	private void unequipTorso(){
		if(torso != null){
			torso.setEquipped(false);
			inventory.add(torso);
			if(torso instanceof Armor){
				armor -= ((Armor) torso).getAC();
			}
			
			torso = null;
		}
	}
	
	private void unequipLegs(){
		if(legs != null){
			legs.setEquipped(false);
			inventory.add(legs);
			if(legs instanceof Armor){
				armor -= ((Armor) legs).getAC();
			}
			legs = null;
		}
	}
	
	private void unequipHead(){
		if(head != null){
			head.setEquipped(false);
			inventory.add(head);
			if(head instanceof Armor){
				armor -= ((Armor) head).getAC();
			}
			head = null;
		}
	}
	
	/**
	 * Overrides Character's hasItem method(). This method checks the Players inventory in addition to his equip slots
	 * for the Item whos name is passed
	 * @param itemName - the name of the item to check for
	 * @return the Item object if found, null if no item was found matching the passed name
	 */
	@Override
	public Item hasItem(String itemName){
		Item item = null;
		item = super.hasItem(itemName);
		if(item == null){
			if(head != null && head.getName().toLowerCase().equals(itemName.toLowerCase())){
				item = head;
			}
			else if(torso != null && torso.getName().toLowerCase().equals(itemName.toLowerCase())){
				item = torso;
			}
			else if(legs != null && legs.getName().toLowerCase().equals(itemName.toLowerCase())){
				item = legs;
			}
			else if(hand1 != null && hand1.getName().toLowerCase().equals(itemName.toLowerCase())){
				item = hand1;
			}
			else if(hand2 != null && hand2.getName().toLowerCase().equals(itemName.toLowerCase())){
				item = hand2;
			}
		}
		return item;
	}
	
	/**
	 * This method overrides Character's getDescription(). Player descriptions are generated based on what they
	 * currently have equipped.
	 * @return the description of this Player
	 */
	@Override
	public String getDescription(){
		String description = "An intrepid adventurer. ";
		if(head != null){
			description += "He wears a " + head.getName() + " on his head, ";
		}
		if(torso != null){
			description += "a " + torso.getName() + " covers his torso, ";
		}
		if(legs != null && (torso != null || head != null)){
			description += "and he has a pair of " + legs.getName() + " on his legs. ";
		}
		else if(legs != null){
			
			description += "He wears a pair of " + legs.getName() + " on his legs. ";
		}
		if(hand1 != null){
			description += "He is holding a " + hand1.getName();
		}
		if(hand2 != null){
			description += "and a " + hand2.getName() + " in his other hand";
		}
		if(hand1 == null && hand2 == null){
			description += "He holds nothing in his hands.";
		}
		
		return description;
	}
	
	/**
	 * This method overrides Character's getAttackRange() method. It returns the attack range of the player
	 * which is based on the weapon he may have equipped. If no weapons are equipped than attack range defaults to 1
	 * @return attack range based on equipped weapon
	 */
	@Override
	public int getAttackRange() {
		if(hand1 instanceof Weapon){
			return ((Weapon)hand1).getAttackRange();
		}
		else{
			return 1;
		}
	}
	
	/**
	 * This method returns the Player's damage which is calculated based on his Strength and currently equipped Weapon
	 * @return Player's base damage
	 */
	@Override
	public int getDamage() {
		double damage;
		if(hand1 instanceof Weapon){
			damage = ((Weapon)hand1).getDamage();
		}
		else{
			damage = 2;
		}
		if(hand1 != null){
			
			//Scale weapon damage with Player's strength and his weapon's attack delay
			if(hand1.getName() != "Crossbow" && hand1.getName() != "Longbow" && hand1.getName() != "Troll Club"){
				damage += (strength*this.getAttackDelay())/3;
			}
			//Ranged Weapon damage is based on dexterity rather than strength
			else if(hand1.getName() == "Crossbow" || hand1.getName() == "Longbow"){
				damage += dexterity;
			}
		}
		else{
			damage += (strength*this.getAttackDelay())/3;
		}
		
		return (int)damage;
	}

	/**
	 * Player's dodge bonus is based on dexterity
	 * @return Player's dexterity
	 */
	@Override
	public int getDodgeBonus() {
		return dexterity;
	}

	/**
	 * Player's accuracy bonus is based on precision
	 * @return Player's precision
	 */
	@Override
	public int getAccuracyBonus() {
		return precision;
	}

	/**
	 * Players damage reduction is based on armor
	 * @return Player's armor
	 */
	@Override
	public int getDamageReduction() {
		return armor;
	}

	/**
	 * Players attack name is based on his equipped weapon. 
	 * fists is returned if the Player does not have a weapon equipped
	 * @return attack name of Player based on his equipped weapon
	 */
	@Override
	public String getAttackName() {
		if(hand1 instanceof Weapon){
			return hand1.getName();
		}
		else{
			return "fists";
		}
	}
	
	
	/**
	 * Searches for an item in the player's inventory and equip slots based on a passed item name. Removes the item and returns
	 * it if found, returns null if no item is found
	 * @param itemName - The name of the item to look for
	 * @return the Item if removed, null if no item by the name was found
	 */
	public Item dropItem(String itemName){
		Item toDrop = this.hasItem(itemName);
		if(toDrop != null){
			if(hand1 == toDrop){
				unequipHand1();		
			}
			else if(hand2 == toDrop){
				unequipHand2();
			}
			else if(torso == toDrop){
				unequipTorso();
			}
			else if(head == toDrop){
				unequipHead();
			}
			else if(legs == toDrop){
				unequipLegs();
			}
			for(int i = 0; i < inventory.size(); i++){
				if(inventory.get(i).equals(toDrop)){
					inventory.remove(toDrop);
					return toDrop;
				}				
			}
		}
		return null;
	}
	
	/**
	 * Gives the Player the passed amount of experience points. Returns whether or not the Player leveled up. Level
	 * ups occur every 1000 experience points accrued.
	 * @param experienceGained - Amount of experience to grant the player
	 * @return whether the Player leveled up
	 */
	public boolean gainExperience(int experienceGained){
		boolean hasLeveledUp = false;
		experiencePoints += experienceGained;
		while(experiencePoints >= 1000){
			experiencePoints -= 1000;
			statIncreases += 3;
			hasLeveledUp = true;
		}
		return hasLeveledUp;
	}
	
	/**
	 * Returns the amount of experience points the player has
	 * @return the amount of experience points the player has
	 */
	public int getExperience(){
		return experiencePoints;
	}
	
	/**
	 * Returns the amount of stat increases the player has available
	 * @return the amount of stat increases the player has available
	 */
	public int getStatIncreases(){
		return statIncreases;
	}
	
	/**
	 * Increases the player's strength by 1
	 */			
	public void increaseStrength(){
		if(statIncreases > 0){
			statIncreases --;
			this.strength ++;
		}
	}
	
	/**
	 * Increases the player's dexterity by 1
	 */
	public void increaseDexterity(){
		if(statIncreases > 0){
			statIncreases --;
			this.dexterity ++;
		}
	}
	
	/**
	 * Increases the player's current and maximum health by 10
	 */
	public void increaseHealth(){
		if(statIncreases > 0){
			statIncreases --;
			this.maxHealth += 10;
			this.currentHealth += 10;
		}
	}
	
	/**
	 * Increases the player's precision by 1
	 */
	public void increasePrecision(){
		if(statIncreases > 0){
			statIncreases --;
			this.precision ++;
		}
	}

}
