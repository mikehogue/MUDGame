package Network.Commands;

import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the Commands that can be executed by a Client. The Commands Command will alert the Client
 * of every possible Command that they can execute on the server. 
 * @author Michael Hogue
 *
 */
public class Commands implements Command {

	/** 
	 * Build a list of every command possible, with arguments, and send the Client a Note
	 * containing the list of commands. 
	 */
	public void execute(Server s, String message, ClientManager cm) {
		String commands = "Commands\n\nooc <message>: send message to global chat\n" +
				"move <n,s,e,w>: move in the specified compass direction\n" +
				"          (aka: go <n,s,e,w>)\n" +
				"look: Look around the area\n" +
				"say <message>: Send message to local room chat\n" +
				"tell <player> <message>: Send message privately to player\n" +
				"who: List the players currently logged into the server\n" +
				"attack <name>: Start combat with <name>\n" +
				"equip <item>: Equip item from your inventory\n" +
				"give <item> <player>: Give specified item to player in your room\n" +
				"drop <item>: Drops item from your inventory\n" +
				"take <item> <mob>: Picks up an item in the room. If a Mob is specified, it takes the Item from the Mob\n" +
				"inventory: List all of the items in your inventory\n" +
				"shutdown <password>: Shutdown the games server\n" +
				"commands: List all of the possible commands on the server\n" + 
				"quit: Logout of the game and go back to the Title Screen\n" + 
				"increase <stat>: Increase the specified stat. Usable after reaching 1000 experience\n" +
				"score: List the statistics of your Player\n" + 
				"use <item>: Use an item, such as a Key, Bomb or Potion, on your Player or the environment\n" +
				"advance <character>: Advance on a Player or Mob for combat\n" + 
				"retreat <character>: Retreat further from a Player or Mob for combat\n" + 
				"dance: Alert the Players in your room that you are dancing\n" + 
				"highfive <Player>: Alert the Players in your room that you have high-fived a Player\n" + 
				"wave <Player>: Alert the Players in your room that you have waved at a Player\n" + 
				"slap <Player>: Alert the Players in your room that you have slapped a Player\n" + 
				"giggle <Player>: Alert the Players in your room that you have giggled at a Player\n";
		cm.send(new Note(commands.substring(0, commands.length() - 2), false));
	}
	
}
