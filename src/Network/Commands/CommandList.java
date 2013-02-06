package Network.Commands;

/**
 * The CommandList is an enum which contains every possible recognized command that a user inputs.
 * It is useful because we can easily check whether their command is contained in this enum:
 * If it is, we can simply use a switch statement to execute the correct command.
 * If it is not, we can alert the user that the command is not possible and to try something else.
 * @author Michael Hogue
 *
 */
public enum CommandList {
		/**
		 * Out of Channel chat message.
		 */
		OOC, 
		/**
		 * Get an Area, Character or Item's description.
		 */
		LOOK, 
		/**
		 * Navigate within the world in a specified direction.
		 */
		MOVE, 		
		/**
		 * Navigate within the world in a specified direction.
		 */
		M, 		
		/**
		 * Navigate within the world in a specified direction.
		 */
		GO, 
		/**
		 * Navigate within the world in the North direction.
		 */
		N, 
		/**
		 * Navigate within the world in the East direction.
		 */
		E, 
		/**
		 * Navigate within the world in the South direction.
		 */
		S, 
		/**
		 * Navigate within the world in the West direction.
		 */
		W, 
		/**
		 * Talk to the Characters in the same Area as you.
		 */
		SAY, 
		/**
		 * Shutdown the server. Takes a password.
		 */
		SHUTDOWN, 
		/**
		 * Pickup an item that is in the same area as you.
		 */
		TAKE, 
		/**
		 * Increase a specified statistic on your player.
		 */
		INCREASE, 
		/**
		 * Pickup an item that is in the same area as you.
		 */
		GET, 
		/**
		 * Pickup an item that is in the same area as you.
		 */
		GRAB, 
		/**
		 * Get a list of your equipped and non-equipped items. 
		 */
		INVENTORY, 
		/**
		 * Equip a specified item.
		 */
		EQUIP, 
		/**
		 * Get a list of all players currently in the game. 
		 */
		WHO, 
		/**
		 * Get a list of all of the possible commands. 
		 */
		COMMANDS, 
		/**
		 * Log out of the game and save your player.
		 */
		QUIT, 
		/**
		 * Attack a specified Character.
		 */
		ATTACK, 
		/**
		 * Tell a specific player a message.
		 */
		TELL, 
		/**
		 * Get a list of all of your statistics.
		 */
		SCORE, 
		/**
		 * Place an item that you have in your inventory or equipped into the Area that you are in.
		 */
		DROP, 
		/**
		 * Use an item, such as a Consumable, or a key.
		 */
		USE, 
		/**
		 * Give a specified item to a specified player.
		 */
		GIVE, 
		/**
		 * Get closer to a specified Character.
		 */
		ADVANCE, 
		/**
		 * Get further from a specified Character.
		 */
		RETREAT, 
		/**
		 * Social command, alert others that you are dancing.
		 */
		DANCE, 
		/**
		 * Social command, give another Character a High-Five
		 */
		HIGHFIVE, 
		/**
		 * Social command, wave at another Character.
		 */
		WAVE, 
		/**
		 * Social command, slap another Character.
		 */
		SLAP, 
		/**
		 * Social command, giggle at another Character or just giggle if no parameter provided.
		 */
		GIGGLE, 
		/**
		 * Abbreviation for the look command; get the Area description. 
		 */
		L, 
		/**
		 * Abbreviation for the Inventory command; get your inventory.
		 */
		INV, 
		/**
		 * Abbreviation for the Attack command; attack a Character.
		 */
		A, 
		/**
		 * Abbreviation for the Advance command; advance on a Character.
		 */
		ADV, 
		/**
		 * Abbreviation for the Advance command; retreat on a Character.
		 */
		RET, 
		/**
		 * Abbreviation for the Equip command; equip an item.
		 */
		EQ, 
		/**
		 * Abbreviation for the Drop command; drop an Item into the Area.
		 */
		D, 
		/**
		 * Abbreviation for the Give command; give a Character an Item.
		 */
		G, 
		/**
		 * Alternate abbreviation for the Attack command; attack a Character.
		 */
		ATT;
	}
