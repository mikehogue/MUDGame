package GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import GameModel.mobs.Mob;

/**
 * An Area is an area of the map which is sort of like a room, but not
 * necessarily indoors. The Area contains occupants, an inventory, and possible
 * exits. The room can also be sunny or not, which can effect certain mobs.
 * 
 * @author Matt Latura
 * 
 */
@SuppressWarnings("serial")
public class Area implements Serializable {
	// INSTANCE VARIABLES***************************************************
	@SuppressWarnings("unused")
	private String name;
	private String description;
	private Exit northExit;
	private Exit eastExit;
	private Exit southExit;
	private Exit westExit;
	private ArrayList<Character> occupants;
	private ArrayList<Item> inventory;
	private boolean isSunny = false;

	/**
	 * The method will change the room to be a Sunny Room. Grue will not enter
	 * sunny rooms, and if they try to enter a room that is sunny, they will
	 * return to their original location.
	 * 
	 * @param sunny
	 *            True to set the room to sunny, false to set it to dark.
	 * @param w
	 *            The World that the Player is using the Sun Wand in.
	 * @param p
	 *            The Player using the Sun Wand.
	 */
	public void setSunny(boolean sunny, World w, Player p) {
		final boolean wasSunny = isSunny;
		final World world = w;
		@SuppressWarnings("unused")
		final Player player = p;

		if (wasSunny) {
			w.sendNotification(new Notification(
					p,
					"There was a bright flash of light, but nothing seemed to change.",
					false));
			return;
		}

		isSunny = sunny;
		w.sendNotification(new Notification(
				this.getPlayers(),
				"A bright aura of sunlight illuminates the area from the tip of the wand. Creatures of the dark will now think twice before entering here",
				false));
		Thread t = new Thread() {
			public void run() {
				try {
					sleep(10000);
					world.sendNotification(new Notification(
							getPlayers(),
							"The bright light has faded. Be aware of your surroundings.",
							false));
					isSunny = wasSunny;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();

	}

	/**
	 * Check if there is sunlight in the room.
	 * 
	 * @return The result of whether or not the room is sunny.
	 */
	public boolean isSunny() {
		return isSunny;
	}

	/**
	 * Used to set rooms that will always be sunny.
	 */
	public void setSunny() {
		this.isSunny = true;
	}

	// METHODS***************************************************************
	/**
	 * Construct a new Area and set the name and description. Construct a new
	 * ArrayList for the occupants and inventory.
	 * 
	 * @param name
	 *            The name of the Area.
	 * @param description
	 *            The description of the Area.
	 */
	public Area(String name, String description) {
		this.name = name;
		this.description = description;
		occupants = new ArrayList<Character>();
		inventory = new ArrayList<Item>();
	}

	/**
	 * Adds the passed exit to the room in the Direction passed
	 * 
	 * @param ex
	 * @param dir
	 */
	public void addExit(Exit ex, Direction dir) {
		if (dir == Direction.NORTH)
			northExit = ex;
		else if (dir == Direction.EAST)
			eastExit = ex;
		else if (dir == Direction.SOUTH)
			southExit = ex;
		else
			westExit = ex;
	}

	/**
	 * Returns an int based on the status of the exit in the passed Direction. 0
	 * = there is no exit in this direction 1 = There is a locked exit in this
	 * direction 2 = There is an unlocked exit in this direction
	 * 
	 * @return int Returns 1 if locked and 2 otherwise.
	 * @param dir
	 *            The direction of this Area that we wish to check the status of
	 *            the exit.
	 * 
	 */
	public int checkExit(Direction dir) {
		if (dir == Direction.NORTH) {
			if (northExit != null) {
				if (northExit.isLocked()) {
					return 1;
				} else {
					return 2;
				}
			}
		} else if (dir == Direction.EAST) {
			if (eastExit != null) {
				if (eastExit.isLocked()) {
					return 1;
				} else {
					return 2;
				}
			}
		} else if (dir == Direction.SOUTH) {
			if (southExit != null) {
				if (southExit.isLocked()) {
					return 1;
				} else {
					return 2;
				}
			}
		} else if (westExit != null) {
			if (westExit.isLocked()) {
				return 1;
			} else {
				return 2;
			}
		}
		return 0;
	}

	/**
	 * Remove an occupant from this Area.
	 * 
	 * @param ch
	 *            The character that we wish to remove from the Area.
	 * @return True if the character was removed, false if the Area did not
	 *         contain the character.
	 */
	public boolean removeOccupant(Character ch) {
		if (occupants.contains(ch)) {
			occupants.remove(ch);
			return true;
		}
		return false;
	}

	/**
	 * Remove an item from this Area.
	 * 
	 * @param i
	 *            The item that we wish to remove from the Area.
	 * @return True if the item was removed, false if the Area did not contain
	 *         the item.
	 */
	public boolean removeItem(Item i) {
		if (inventory.contains(i)) {
			inventory.remove(i);
			return true;
		}
		return false;
	}

	/**
	 * Add an Item to the Area.
	 * 
	 * @param i
	 *            The item that should be added to the Area.
	 */
	public void addItem(Item i) {
		inventory.add(i);
	}

	/**
	 * Add a Character to the Area.
	 * 
	 * @param ch
	 *            The character to be added to the Area.
	 */
	public void addOccupant(Character ch) {
		occupants.add(ch);
	}

	/**
	 * Get the Description of the room. Used when Players look at the Area, or
	 * enter a new Area.
	 * 
	 * @param character
	 *            - The character that requested the description.
	 * @return The rooms description, including all items and mobs in the room.
	 */
	public String getDescription(Character character) {
		String fullDescription = description;

		// Adds items in area to description
		fullDescription += "\n\nItems in the area: ";
		if (!inventory.isEmpty()) {
			fullDescription += inventory.get(0).getName();
			for (int i = 1; i < inventory.size(); i++) {
				if (inventory.get(i) != null)
					fullDescription += ", " + inventory.get(i).getName();
			}
		} else {
			fullDescription += "None";
		}

		// Adds mobs in area to description
		fullDescription += "\nMobs in the area: ";
		/*String mobs = "";
		if (!occupants.isEmpty()) {
			this.get
			mobs += occupants.get(0).getName();
			for (int i = 1; i < occupants.size(); i++) {
				mobs += ", " + occupants.get(i).getName();
			}
		} else {
			fullDescription += "None";
		}

		String temp = mobs;
		if (mobs.equals(character.getName())) {
			temp = "None";
		} else {
			temp = temp.replace(character.getName() + ", ", "");
			temp = temp.replace(", " + character.getName(), "");
		}*/
		
		String temp = "";
		if (!occupants.isEmpty()) {
			Iterator<Player> players = this.getAllPlayersExcept(character);
			while (players.hasNext()) {
				temp += players.next().getName() + ", ";
			}
			Iterator<Mob> mobs = this.getMobs();
			while (mobs.hasNext()) {
				temp += mobs.next().getName() + ", ";
			}
			if (temp.length() >= 2)
				temp = temp.substring(0, temp.length() - 2);
			else
				temp = "None";
		} else {
			temp = "None";
		}
		
		String roomExits="\n"+"Room Exits: ";
		
			if(northExit!=null)
				roomExits += "North, ";
			if(eastExit!=null)
				roomExits += "East, ";
			if(southExit!=null)
				roomExits += "South, ";
			if(westExit!=null)
				roomExits += "West, ";
		roomExits= roomExits.substring(0, roomExits.length()-2)+".";
		// fullDescription += temp;
		return fullDescription + temp + roomExits;
	}

	/**
	 * Get all of the Players that are inside of the Area.
	 * 
	 * @return Iterator of all of the Players in the Area.
	 */
	public Iterator<Player> getPlayers() {
		ArrayList<Player> playersInRoom = new ArrayList<Player>();
		for (int i = 0; i < occupants.size(); i++) {
			if (occupants.get(i) instanceof Player) {
				playersInRoom.add((Player) occupants.get(i));
			}
		}
		return playersInRoom.iterator();
	}

	/**
	 * Check whether or not the Area has a Character.
	 * 
	 * @param ch
	 *            the Character that we are checking if they exist in the Area.
	 * @return True if the Area has the character, else false.
	 */
	public boolean containsCharacter(Character ch) {
		return occupants.contains(ch);
	}

	/**
	 * Get an Iterator of all items that are contained in the Area.
	 * 
	 * @return An Iterator contanining all items in the Area.
	 */
	public Iterator<Item> getItems() {
		return inventory.iterator();
	}

	/**
	 * Check if the Area has items in its inventory.
	 * 
	 * @return True if there are items, false else.
	 */
	public boolean hasItems() {
		return (!inventory.isEmpty());
	}

	/**
	 * Remove an Item from this Area.
	 * 
	 * @param itemName
	 *            Item name that we wish to remove from the room.
	 * @return True if the Item was removed, false else.
	 */
	public Item removeItem(String itemName) {
		Item toReturn = null;
		for (int i = 0; i < this.inventory.size(); i++) {
			if (this.inventory.get(i) != null) {
				if (this.inventory.get(i).getName().toLowerCase()
						.equals((String) itemName)) {
					toReturn = this.inventory.get(i);
					this.inventory.remove(toReturn);
					return toReturn;
				}
			}
		}
		return toReturn;
	}

	/**
	 * Get all of the Mobs that are inside of the Area.
	 * 
	 * @return An Iterator containing every Mob in the Area.
	 */
	public Iterator<Mob> getMobs() {
		ArrayList<Mob> mobs = new ArrayList<Mob>();
		for (int i = 0; i < this.occupants.size(); i++) {
			if (this.occupants.get(i) instanceof Mob) {
				mobs.add((Mob) this.occupants.get(i));
			}
		}
		return mobs.iterator();
	}

	/**
	 * Get all of the Players inside of the room except the passed in Character.
	 * 
	 * @param ch1
	 *            Character to exclude from the Iterator.
	 * @return Iterator containing all Players inside of the Area, except ch1.
	 */
	public Iterator<Player> getAllPlayersExcept(Character ch1) {
		ArrayList<Player> players = new ArrayList<Player>();
		for (int i = 0; i < occupants.size(); i++) {
			if (occupants.get(i) instanceof Player && occupants.get(i) != ch1) {
				players.add((Player) occupants.get(i));
			}
		}
		return players.iterator();
	}

	/**
	 * Get all of the Players inside of the room except the passed in
	 * Characters.
	 * 
	 * @param ch1
	 *            Character to exclude from the Iterator.
	 * @param ch2
	 *            Second Character to exclude from the Iterator.
	 * @return Iterator containing all Players inside of the Area, except ch1
	 *         and ch2.
	 */
	public Iterator<Player> getAllPlayersExcept(Character ch1, Character ch2) {
		ArrayList<Player> players = new ArrayList<Player>();
		for (int i = 0; i < occupants.size(); i++) {
			if ((occupants.get(i) instanceof Player)
					&& (occupants.get(i) != ch1) && (occupants.get(i) != ch2)) {
				players.add((Player) occupants.get(i));
			}
		}
		return players.iterator();
	}

	/**
	 * Check if the room has an Item.
	 * 
	 * @param itemName
	 *            Item that we wish to check if it is in the Area.
	 * @return Instance of the Item if true, null if not.
	 */
	public Item hasItem(String itemName) {
		Item item = null;
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).getName().toLowerCase()
					.equals(itemName.toLowerCase())) {
				item = inventory.get(i);
				return item;
			}
		}
		return item;
	}

	/**
	 * Check if an Area contains a Character.
	 * 
	 * @param characterName
	 *            Name of the Character to check if they are in the room or not.
	 * @return The Character if they are in the Area, or null if they are not.
	 */
	public Character hasCharacter(String characterName) {
		Character ch = null;
		for (int i = 0; i < occupants.size(); i++) {
			if (occupants.get(i).getName().toLowerCase()
					.equals(characterName.toLowerCase())) {
				ch = occupants.get(i);
				return ch;
			}
		}
		return ch;
	}

	/**
	 * Place an item into the Area.
	 * 
	 * @param i
	 *            Item to place into the Area.
	 */
	public void putItem(Item i) {
		inventory.add(i);
	}

	/**
	 * Get all of the occupants inside of the Area.
	 * 
	 * @return An Iterator containing all occupants.
	 */
	public Iterator<Character> getOccupants() {
		return occupants.iterator();
	}

	/**
	 * Attempt to unlock a door.
	 * 
	 * @param i
	 *            The attempted Key to open the door.
	 * @return The direction of the exit that was unlocked if key worked, null
	 *         if it did not.
	 */
	public Direction unlockDoor(Item i) {
		if (this.checkExit(Direction.NORTH) == 1) {
			if (this.northExit.getKey().getName().equals(i.getName())) {
				this.northExit.unlock();
				return Direction.NORTH;
			}
		} else if (this.checkExit(Direction.EAST) == 1) {
			if (this.eastExit.getKey().getName().equals(i.getName())) {
				this.eastExit.unlock();
				return Direction.EAST;
			}
		} else if (this.checkExit(Direction.SOUTH) == 1) {
			if (this.southExit.getKey().getName().equals(i.getName())) {
				this.southExit.unlock();
				return Direction.SOUTH;
			}
		} else if (this.checkExit(Direction.WEST) == 1) {
			if (this.westExit.getKey().getName().equals(i.getName())) {
				this.westExit.unlock();
				return Direction.WEST;
			}
		}
		return null;
	}
}
