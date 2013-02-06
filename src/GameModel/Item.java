package GameModel;

import java.io.Serializable;

/**
 * Item represents consumables, armor, weapons, and environmental items such as keys and bombs.
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class Item implements Serializable {
	private String name;
	private int weight;
	private String description;
	
	/**
	 * Generates a new Item.
	 * @param name
	 * 			The name of the item.
	 * @param weight
	 * 			The weight of the item.
	 * @param description
	 * 			The description of the item.
	 */
	public Item(String name, int weight, String description){
		this.name = name;
		this.weight = weight;
		this.description = description;	
	}
	
	/**
	 * @return 
	 * 			The name of the Item.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return The weight of the Item.
	 */
	public int getWeight(){
		return weight;
	}
	
	/**
	 * @return The description of the Item.
	 */
	public String getDescription(){
		return description;
	}

}
